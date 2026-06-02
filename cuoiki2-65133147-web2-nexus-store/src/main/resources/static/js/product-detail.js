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
        document.addEventListener('mouseover', e => { if(e.target.closest('a, button, label, input')) cursorHexagon.classList.add('hovering'); });
        document.addEventListener('mouseout', e => { if(e.target.closest('a, button, label, input') && (!e.relatedTarget || !e.relatedTarget.closest('a, button, label, input'))) cursorHexagon.classList.remove('hovering'); });

        // Builder Logic
        const colorInputs = document.querySelectorAll('input[name="color"]');
        const storageInputs = document.querySelectorAll('input[name="storage"]');
        const productImage = document.getElementById('productImage');
        const colorNameDisplay = document.getElementById('colorNameDisplay');
        const totalPriceDisplay = document.getElementById('totalPriceDisplay');

        function updateBuilder() {
            const activeColor = document.querySelector('input[name="color"]:checked');
            const activeStorage = document.querySelector('input[name="storage"]:checked');
            
            // Update Image Color
            productImage.style.background = activeColor.getAttribute('data-bg');
            colorNameDisplay.textContent = activeColor.getAttribute('data-name');
            
            // Update Price format
            const price = parseInt(activeStorage.getAttribute('data-price')).toLocaleString('vi-VN');
            totalPriceDisplay.textContent = `${price}₫`;
        }

        colorInputs.forEach(input => input.addEventListener('change', updateBuilder));
        storageInputs.forEach(input => input.addEventListener('change', updateBuilder));
		document.querySelectorAll('input[name="color"]').forEach(radio => {
		    radio.addEventListener('change', function() {
		        document.getElementById('selectedColorId').value = this.value;
		    });
		});