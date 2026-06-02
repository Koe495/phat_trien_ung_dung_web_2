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
            if (Math.sqrt(dx*dx+dy*dy) > 1.5) lastAngle = Math.atan2(dy,dx)*180/Math.PI;
            cursorHexagon.style.transform = `rotate(${lastAngle+90}deg)`;
        }
        requestAnimationFrame(animateCursor);
    })();

    const clickable = 'a, button, [role="button"], input, label';
    document.addEventListener('mouseover', e => { if (e.target.closest(clickable)) { cursorHexagon.classList.add('hovering'); cursorHexagon.style.transform=''; } });
    document.addEventListener('mouseout',  e => { if (e.target.closest(clickable)) { const to=e.relatedTarget; if(!to||!to.closest(clickable)) cursorHexagon.classList.remove('hovering'); } });

    /* --- 2. Notched Outline Width --- */
    function adjustNotchWidth(inputId, labelId, notchId) {
        const input = document.getElementById(inputId);
        const label = document.getElementById(labelId);
        const notch = document.getElementById(notchId);
        if (!input || !label || !notch) return;

        function setWidth() {
            if (input.value !== '' || document.activeElement === input) {
                notch.style.width = `${label.offsetWidth * 0.75 + 8}px`;
            } else {
                notch.style.width = '0px';
            }
        }
        input.addEventListener('focus', setWidth);
        input.addEventListener('blur',  setWidth);
        input.addEventListener('input', setWidth);
        setTimeout(setWidth, 100);
    }

    adjustNotchWidth('lastName',  'label-lastName',  'notch-lastName');
    adjustNotchWidth('firstName', 'label-firstName', 'notch-firstName');
    adjustNotchWidth('email',     'label-email',     'notch-email');
    adjustNotchWidth('phone',     'label-phone',     'notch-phone');
    adjustNotchWidth('address',   'label-address',   'notch-address');
});