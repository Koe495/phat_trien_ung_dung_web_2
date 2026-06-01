document.addEventListener('DOMContentLoaded', () => {

            let isScrollingToTarget = false; 
            let scrollEndTimeout = null;

            /* --- 1. Expandable Search Bar --- */
            const searchWrapper = document.getElementById('searchWrapper');
            const searchBtn = document.getElementById('searchBtn');
            const searchInput = document.getElementById('searchInput');

            searchBtn.addEventListener('click', (e) => {
                e.stopPropagation(); 
                if (!searchWrapper.classList.contains('expanded')) {
                    searchWrapper.classList.add('expanded');
                    setTimeout(() => { searchInput.focus(); }, 50);
                }
            });

            document.addEventListener('click', (e) => {
                if (searchWrapper.classList.contains('expanded') && !searchWrapper.contains(e.target)) {
                    searchWrapper.classList.remove('expanded');
                    searchInput.value = ''; 
                }
            });

            /* --- 2. Custom Trailing Cursor --- */
            const cursor = document.getElementById('customCursor');
            const cursorHexagon = document.getElementById('customCursorHexagon');
            let mouseX = 0, mouseY = 0;   
            let cursorX = 0, cursorY = 0; 
            let lastAngle = 0;            

            window.addEventListener('mousemove', (e) => {
                mouseX = e.clientX; mouseY = e.clientY;
                if (cursor && cursor.style.display !== 'block') cursor.style.display = 'block';
            });

            function animateCursor() {
                const dx = mouseX - cursorX; const dy = mouseY - cursorY;
                cursorX += dx * 0.16; cursorY += dy * 0.16;
                cursor.style.left = `${cursorX}px`; cursor.style.top = `${cursorY}px`;
                
                if (!cursorHexagon.classList.contains('hovering')) {
                    const distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance > 1.5) {
                        const angle = Math.atan2(dy, dx) * 180 / Math.PI;
                        lastAngle = angle; cursorHexagon.style.transform = `rotate(${angle + 90}deg)`;
                    } else {
                        cursorHexagon.style.transform = `rotate(${lastAngle + 90}deg)`;
                    }
                }
                requestAnimationFrame(animateCursor);
            }
            requestAnimationFrame(animateCursor);

            const clickableSelector = 'a, button, [role="button"], .card, .support-topic-card, .faq-question, .search-input, .big-search-input';
            document.addEventListener('mouseover', (e) => {
                const target = e.target.closest(clickableSelector);
                if (target) { cursorHexagon.classList.add('hovering'); cursorHexagon.style.transform = ''; }
            });
            document.addEventListener('mouseout', (e) => {
                const target = e.target.closest(clickableSelector);
                if (target) {
                    const toElement = e.relatedTarget;
                    if (!toElement || !toElement.closest(clickableSelector)) cursorHexagon.classList.remove('hovering');
                }
            });

            /* --- 3. Sidebar Rail Logic & Pill --- */
            const body = document.body;
            const railToggle = document.getElementById('railToggle');
            const railItems = document.querySelectorAll('.rail-item');
            const slidingPill = document.getElementById('slidingPill');
            
            railToggle.addEventListener('click', () => {
                body.classList.toggle('rail-collapsed'); setTimeout(updatePillPosition, 400); 
            });

            function updatePillPosition() {
                const activeItem = document.querySelector('.rail-item.active');
                if (!activeItem) return;
                const topPos = activeItem.offsetTop; const itemHeight = activeItem.offsetHeight;
                const rootStyles = getComputedStyle(document.documentElement);
                const pillHeight = parseInt(rootStyles.getPropertyValue('--pill-height')) || 36;
                const offset = topPos + (itemHeight - pillHeight) / 2;
                slidingPill.style.transform = `translateY(${offset}px)`;
            }

            window.addEventListener('load', updatePillPosition);
            window.addEventListener('resize', updatePillPosition);

            /* --- 4. Sidebar Smooth Scroll & Scroll Spy --- */
            const sections = document.querySelectorAll('main > section[id]');
            const observerOptions = { root: null, rootMargin: '-30% 0px -60% 0px', threshold: 0 };
            
            const observer = new IntersectionObserver((entries) => {
                if (isScrollingToTarget) return;
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const id = entry.target.getAttribute('id');
                        const correspondingRailItem = document.querySelector(`.rail-item[href="#${id}"]`);
                        if (correspondingRailItem && !correspondingRailItem.classList.contains('active')) {
                            railItems.forEach(nav => nav.classList.remove('active'));
                            correspondingRailItem.classList.add('active');
                            updatePillPosition();
                        }
                    }
                });
            }, observerOptions);
            sections.forEach(section => observer.observe(section));

            function getAbsoluteFlowTop(targetElement) {
                let top = document.querySelector('.main-header').offsetHeight || 60;
                const mainSections = document.querySelectorAll('.main-content > section');
                for (let sec of mainSections) {
                    if (sec === targetElement) break;
                    top += sec.offsetHeight;
                }
                return top;
            }

            document.querySelectorAll('.rail-item[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function(e) {
                    const targetId = this.getAttribute('href');
                    if (targetId === '#') return;
                    const targetElement = document.querySelector(targetId);
                    
                    if (targetElement) {
                        e.preventDefault(); 
                        isScrollingToTarget = true; 
                        
                        document.querySelectorAll('.rail-item').forEach(nav => nav.classList.remove('active'));
                        this.classList.add('active');
                        updatePillPosition();

                        const headerHeight = 60; 
                        window.scrollTo({
                            top: targetElement.offsetTop - headerHeight, 
                            behavior: 'smooth'
                        });

                        if (scrollEndTimeout) clearTimeout(scrollEndTimeout);
                        scrollEndTimeout = setTimeout(() => { isScrollingToTarget = false; }, 800); 
                    }
                });
            });

            /* --- 5. FAQ Accordion Logic --- */
            const faqQuestions = document.querySelectorAll('.faq-question');
            faqQuestions.forEach(question => {
                question.addEventListener('click', () => {
                    const item = question.closest('.faq-item');
                    const answer = item.querySelector('.faq-answer');
                    
                    // Toggle current item
                    item.classList.toggle('active');
                    
                    if (item.classList.contains('active')) {
                        answer.style.maxHeight = answer.scrollHeight + "px";
                    } else {
                        answer.style.maxHeight = 0;
                    }
                    
                    // Optional: Close others
                    // const otherItems = document.querySelectorAll('.faq-item');
                    // otherItems.forEach(other => {
                    //    if(other !== item && other.classList.contains('active')) {
                    //        other.classList.remove('active');
                    //        other.querySelector('.faq-answer').style.maxHeight = 0;
                    //    }
                    // });
                });
            });

        });