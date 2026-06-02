document.addEventListener('DOMContentLoaded', () => {

    /* --- 1. Custom Cursor --- */
    const cursor        = document.getElementById('customCursor');
    const cursorHexagon = document.getElementById('customCursorHexagon');
    let mouseX = 0, mouseY = 0, cursorX = 0, cursorY = 0, lastAngle = 0;

    window.addEventListener('mousemove', (e) => {
        mouseX = e.clientX; mouseY = e.clientY;
        if (cursor && cursor.style.display !== 'block') cursor.style.display = 'block';
    });

    (function animateCursor() {
        const dx = mouseX - cursorX, dy = mouseY - cursorY;
        cursorX += dx * 0.16; cursorY += dy * 0.16;
        cursor.style.left = `${cursorX}px`; cursor.style.top = `${cursorY}px`;
        if (!cursorHexagon.classList.contains('hovering')) {
            if (Math.sqrt(dx*dx+dy*dy) > 1.5) {
                lastAngle = Math.atan2(dy, dx) * 180 / Math.PI;
            }
            cursorHexagon.style.transform = `rotate(${lastAngle + 90}deg)`;
        }
        requestAnimationFrame(animateCursor);
    })();

    const clickable = 'a, button, [role="button"], input, label';
    document.addEventListener('mouseover', e => { if (e.target.closest(clickable)) { cursorHexagon.classList.add('hovering'); cursorHexagon.style.transform = ''; } });
    document.addEventListener('mouseout',  e => { if (e.target.closest(clickable)) { const to = e.relatedTarget; if (!to || !to.closest(clickable)) cursorHexagon.classList.remove('hovering'); } });

    /* --- 2. Checkbox logic + live subtotal --- */
    const selectAll      = document.getElementById('selectAllCheckbox');
    const checkboxes     = document.querySelectorAll('.item-checkbox');
    const btnTop         = document.getElementById('btnCheckoutSelected');
    const btnSummary     = document.getElementById('btnCheckoutSummary');
    const summarySubtotal = document.getElementById('summarySubtotal');
    const summaryTotal    = document.getElementById('summaryTotal');
    const summaryNote     = document.getElementById('summaryNote');

    // Bảng giá: { itemId -> price * qty }
    const priceData = {};
    document.querySelectorAll('.item-price-data').forEach(el => {
        priceData[el.dataset.id] = parseFloat(el.dataset.price) * parseInt(el.dataset.qty);
    });

    function formatVND(n) {
        return n.toLocaleString('vi-VN') + '₫';
    }

    function updateUI() {
        const checked = [...checkboxes].filter(c => c.checked);
        const hasAny  = checked.length > 0;

        // Buttons
        if (btnTop)     btnTop.disabled     = !hasAny;
        if (btnSummary) btnSummary.disabled = !hasAny;

        // Note
        if (summaryNote) {
            summaryNote.textContent = hasAny
                ? `${checked.length} sản phẩm đã chọn`
                : 'Chọn sản phẩm để thanh toán';
        }

        // Recalculate subtotal from selected items
        let total = 0;
        checked.forEach(c => {
            const itemId = c.closest('.cart-item').dataset.id;
            total += priceData[itemId] || 0;
        });

        const formatted = formatVND(total);
        if (summarySubtotal) summarySubtotal.textContent = formatted;
        if (summaryTotal)    summaryTotal.textContent    = formatted;

        // Select-all state
        if (selectAll) {
            selectAll.checked       = checked.length === checkboxes.length && checkboxes.length > 0;
            selectAll.indeterminate = hasAny && checked.length < checkboxes.length;
        }
    }

    // Select all toggle
    if (selectAll) {
        selectAll.addEventListener('change', () => {
            checkboxes.forEach(c => c.checked = selectAll.checked);
            updateUI();
        });
    }

    // Individual checkboxes
    checkboxes.forEach(c => c.addEventListener('change', updateUI));

    // Init
    updateUI();
});