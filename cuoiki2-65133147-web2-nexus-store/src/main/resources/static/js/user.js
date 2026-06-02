document.addEventListener('DOMContentLoaded', () => {

    /* --- 1. Custom Trailing Hexagon Cursor --- */
    const cursor = document.getElementById('customCursor');
    const cursorHexagon = document.getElementById('customCursorHexagon');

    let mouseX = 0, mouseY = 0;
    let cursorX = 0, cursorY = 0;
    let lastAngle = 0;

    window.addEventListener('mousemove', (e) => {
        mouseX = e.clientX;
        mouseY = e.clientY;
        if (cursor && cursor.style.display !== 'block') {
            cursor.style.display = 'block';
        }
    });

    function animateCursor() {
        const dx = mouseX - cursorX;
        const dy = mouseY - cursorY;

        cursorX += dx * 0.16;
        cursorY += dy * 0.16;

        cursor.style.left = `${cursorX}px`;
        cursor.style.top  = `${cursorY}px`;

        if (!cursorHexagon.classList.contains('hovering')) {
            const distance = Math.sqrt(dx * dx + dy * dy);
            if (distance > 1.5) {
                const angle = Math.atan2(dy, dx) * 180 / Math.PI;
                lastAngle = angle;
                cursorHexagon.style.transform = `rotate(${angle + 90}deg)`;
            } else {
                cursorHexagon.style.transform = `rotate(${lastAngle + 90}deg)`;
            }
        }

        requestAnimationFrame(animateCursor);
    }
    requestAnimationFrame(animateCursor);

    const clickableSelector = 'a, button, [role="button"], input';

    document.addEventListener('mouseover', (e) => {
        const target = e.target.closest(clickableSelector);
        if (target) {
            cursorHexagon.classList.add('hovering');
            cursorHexagon.style.transform = '';
        }
    });

    document.addEventListener('mouseout', (e) => {
        const target = e.target.closest(clickableSelector);
        if (target) {
            const toElement = e.relatedTarget;
            if (!toElement || !toElement.closest(clickableSelector)) {
                cursorHexagon.classList.remove('hovering');
            }
        }
    });

    /* --- 2. Tab Switching --- */
    const tabLinks = document.querySelectorAll('.sidebar-link[data-target]');
    const tabPanes = document.querySelectorAll('.tab-pane');

    tabLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            tabLinks.forEach(btn => btn.classList.remove('active'));
            tabPanes.forEach(pane => {
                pane.classList.remove('active');
                // Re-trigger animation
                pane.style.animation = 'none';
                pane.offsetHeight; // reflow
                pane.style.animation = null;
            });

            this.classList.add('active');
            const targetPane = document.getElementById(this.getAttribute('data-target'));
            if (targetPane) targetPane.classList.add('active');
        });
    });

    /* --- 3. Dynamic Notched Width Calculation --- */
    function adjustNotchWidth(inputId, labelId, notchId) {
        const input  = document.getElementById(inputId);
        const label  = document.getElementById(labelId);
        const notch  = document.getElementById(notchId);

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

    // Profile form
    adjustNotchWidth('lastName',        'label-lastName',        'notch-lastName');
    adjustNotchWidth('firstName',       'label-firstName',       'notch-firstName');
    adjustNotchWidth('email',           'label-email',           'notch-email');
    adjustNotchWidth('phone',           'label-phone',           'notch-phone');

    // Security form
    adjustNotchWidth('currentPassword', 'label-currentPassword', 'notch-currentPassword');
    adjustNotchWidth('newPassword',     'label-newPassword',     'notch-newPassword');
    adjustNotchWidth('confirmPassword', 'label-confirmPassword', 'notch-confirmPassword');

    /* --- 4. Toggle Password Visibility --- */
    const eyeOffPath = `<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>`;
    const eyeOnPath  = `<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>`;

    document.querySelectorAll('.toggle-password').forEach(btn => {
        btn.addEventListener('click', function () {
            const inputId = this.getAttribute('data-target');
            const input   = document.getElementById(inputId);
            const svg     = this.querySelector('svg');

            if (!input) return;
            const isPassword = input.type === 'password';
            input.type     = isPassword ? 'text' : 'password';
            svg.innerHTML  = isPassword ? eyeOffPath : eyeOnPath;
            this.setAttribute('aria-label', isPassword ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
        });
    });

    /* --- 5. Password Confirm Validation --- */
    const newPasswordInput  = document.getElementById('newPassword');
    const confirmInput      = document.getElementById('confirmPassword');
    const pwError           = document.getElementById('pwError');

    if (confirmInput && newPasswordInput) {
        confirmInput.addEventListener('input', () => {
            if (confirmInput.value === newPasswordInput.value) {
                pwError.style.display = 'none';
            } else if (confirmInput.value.length > 0) {
                pwError.textContent   = 'Mật khẩu không khớp.';
                pwError.style.display = 'block';
            } else {
                pwError.style.display = 'none';
            }
        });
    }

    const passwordForm = document.getElementById('passwordForm');
    if (passwordForm) {
        passwordForm.addEventListener('submit', (e) => {
            if (newPasswordInput.value !== confirmInput.value) {
                e.preventDefault();
                pwError.textContent   = 'Mật khẩu không khớp.';
                pwError.style.display = 'block';
                confirmInput.focus();
            }
        });
    }

    /* --- 6. Payment Tab Toggle --- */
    const paymentTypeBtns = document.querySelectorAll('.payment-type-btn');
    const paymentSections = document.querySelectorAll('.payment-section');

    paymentTypeBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            paymentTypeBtns.forEach(b => b.classList.remove('active'));
            paymentSections.forEach(s => s.classList.remove('active'));
            this.classList.add('active');
            const target = document.getElementById(this.getAttribute('data-payment') + '-section');
            if (target) target.classList.add('active');
        });
    });

    // Notch cho form thanh toán
    adjustNotchWidth('cardNumber',  'label-cardNumber',  'notch-cardNumber');
    adjustNotchWidth('cardHolder',  'label-cardHolder',  'notch-cardHolder');
    adjustNotchWidth('cardExpiry',  'label-cardExpiry',  'notch-cardExpiry');
    adjustNotchWidth('cardCvv',     'label-cardCvv',     'notch-cardCvv');
    adjustNotchWidth('bankName',    'label-bankName',    'notch-bankName');
    adjustNotchWidth('bankAccount', 'label-bankAccount', 'notch-bankAccount');
    adjustNotchWidth('bankOwner',   'label-bankOwner',   'notch-bankOwner');
    adjustNotchWidth('bankBranch',  'label-bankBranch',  'notch-bankBranch');

    /* --- 7. Live Card Preview --- */
    const cardNumberInput   = document.getElementById('cardNumber');
    const cardHolderInput   = document.getElementById('cardHolder');
    const cardExpiryInput   = document.getElementById('cardExpiry');
    const cardNumberDisplay = document.getElementById('cardNumberDisplay');
    const cardHolderDisplay = document.getElementById('cardHolderDisplay');
    const cardExpiryDisplay = document.getElementById('cardExpiryDisplay');
    const cardNetwork       = document.getElementById('cardNetwork');

    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', function () {
            let val = this.value.replace(/\D/g, '').slice(0, 16);
            this.value = val.replace(/(.{4})/g, '$1 ').trim();

            const raw = val.padEnd(16, '•');
            cardNumberDisplay.textContent =
                raw.slice(0,4) + ' ' + raw.slice(4,8) + ' ' + raw.slice(8,12) + ' ' + raw.slice(12,16);

            // Nhận diện mạng thẻ
            cardNetwork.className = 'card-network';
            cardNetwork.textContent = '';
            if (/^4/.test(val)) {
                cardNetwork.classList.add('visa');
                cardNetwork.textContent = 'VISA';
            } else if (/^5[1-5]/.test(val) || /^2[2-7]/.test(val)) {
                cardNetwork.classList.add('mc');
            } else if (/^3[47]/.test(val)) {
                cardNetwork.textContent = 'AMEX';
            } else if (/^9704/.test(val)) {
                cardNetwork.textContent = 'Napas';
            }
        });
    }

    if (cardHolderInput) {
        cardHolderInput.addEventListener('input', function () {
            cardHolderDisplay.textContent = this.value.toUpperCase() || 'TÊN CHỦ THẺ';
        });
    }

    if (cardExpiryInput) {
        cardExpiryInput.addEventListener('input', function () {
            let val = this.value.replace(/\D/g, '').slice(0, 4);
            if (val.length >= 3) val = val.slice(0,2) + '/' + val.slice(2);
            this.value = val;
            cardExpiryDisplay.textContent = val || 'MM/YY';
        });
    }

});