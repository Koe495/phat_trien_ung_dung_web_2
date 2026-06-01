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
                cursor.style.top = `${cursorY}px`;
                
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

            /* --- 2. Toggle Password Visibility --- */
            const passwordInput = document.getElementById('password');
            const togglePasswordBtn = document.getElementById('togglePasswordBtn');
            const eyeIcon = document.getElementById('eyeIcon');

            const eyeOffPath = `<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>`;
            const eyeOnPath = `<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>`;

            togglePasswordBtn.addEventListener('click', () => {
                const isPassword = passwordInput.type === 'password';
                passwordInput.type = isPassword ? 'text' : 'password';
                eyeIcon.innerHTML = isPassword ? eyeOffPath : eyeOnPath;
                togglePasswordBtn.setAttribute('aria-label', isPassword ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
            });

            /* --- 3. Dynamic Notched Width Calculation --- */
            function adjustNotchWidth(inputId, labelId, notchId) {
                const input = document.getElementById(inputId);
                const label = document.getElementById(labelId);
                const notch = document.getElementById(notchId);

                function setWidth() {
                    if (input.value !== "" || document.activeElement === input) {
                        const width = label.offsetWidth * 0.75;
                        notch.style.width = `${width + 8}px`; 
                    } else {
                        notch.style.width = '0px';
                    }
                }

                input.addEventListener('focus', setWidth);
                input.addEventListener('blur', setWidth);
                input.addEventListener('input', setWidth);
                
                setTimeout(setWidth, 200);
            }

            adjustNotchWidth('lastName', 'label-lastName', 'notch-lastName');
            adjustNotchWidth('firstName', 'label-firstName', 'notch-firstName');
            adjustNotchWidth('phone', 'label-phone', 'notch-phone');
            adjustNotchWidth('email', 'label-email', 'notch-email');
            adjustNotchWidth('password', 'label-password', 'notch-password');

        });