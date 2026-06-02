// Custom Cursor Logic
        const cursor = document.getElementById('customCursor'); const cursorHexagon = document.getElementById('customCursorHexagon');
        let cx = 0, cy = 0, tx = 0, ty = 0, ang = 0;
        window.addEventListener('mousemove', e => { tx = e.clientX; ty = e.clientY; cursor.style.display = 'block'; });
        function animCursor() {
            cx += (tx - cx) * 0.16; cy += (ty - cy) * 0.16; cursor.style.left = cx+'px'; cursor.style.top = cy+'px';
            if(!cursorHexagon.classList.contains('hovering')) {
                if(Math.hypot(tx-cx, ty-cy)>1.5) ang = Math.atan2(ty-cy, tx-cx)*180/Math.PI;
                cursorHexagon.style.transform = `rotate(${ang+90}deg)`;
            }
            requestAnimationFrame(animCursor);
        }
        animCursor();
        document.addEventListener('mouseover', e => { if(e.target.closest('a, button, input, label')) cursorHexagon.classList.add('hovering'); });
        document.addEventListener('mouseout', e => { if(e.target.closest('a, button, input, label') && (!e.relatedTarget || !e.relatedTarget.closest('a, button, input, label'))) cursorHexagon.classList.remove('hovering'); });

        // Checkout Submit Simulation
        const form = document.getElementById('checkoutForm');
        const container = document.getElementById('checkoutContainer');
        const overlay = document.getElementById('successOverlay');

        form.addEventListener('submit', (e) => {
            e.preventDefault(); // Chặn gửi form thực tế
            
            // Fade out form
            container.style.opacity = '0';
            
            // Fade in success message sau 500ms
            setTimeout(() => {
                container.style.display = 'none';
                overlay.classList.add('active');
                window.scrollTo({top: 0, behavior: 'smooth'}); // Cuộn lên đầu
            }, 500);
        });