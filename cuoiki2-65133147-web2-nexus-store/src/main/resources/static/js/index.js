document.addEventListener('DOMContentLoaded', () => {
            
            /* --- Cờ kiểm soát cuộn trang để khóa hoạt động ghim Indicator --- */
            let isScrollingToTarget = false; 
            let scrollEndTimeout = null;

            /* --- Xử lý tính năng Tìm kiếm mở rộng (Expandable Search Bar) --- */
            const searchWrapper = document.getElementById('searchWrapper');
            const searchBtn = document.getElementById('searchBtn');
            const searchInput = document.getElementById('searchInput');

            searchBtn.addEventListener('click', (e) => {
                e.stopPropagation(); 
                if (!searchWrapper.classList.contains('expanded')) {
                    searchWrapper.classList.add('expanded');
                    setTimeout(() => {
                        searchInput.focus(); 
                    }, 50);
                }
            });

            // Thu hẹp search bar khi nhấn ra vùng trống
            document.addEventListener('click', (e) => {
                if (searchWrapper.classList.contains('expanded') && !searchWrapper.contains(e.target)) {
                    searchWrapper.classList.remove('expanded');
                    searchInput.value = ''; 
                }
            });


            /* --- 1. Collapsible Nav Rail Logic --- */
            const body = document.body;
            const railToggle = document.getElementById('railToggle');
            
            railToggle.addEventListener('click', () => {
                body.classList.toggle('rail-collapsed');
                setTimeout(updatePillPosition, 400); 
            });


            /* --- 2. Floating Rail Sliding Pill Logic --- */
            const railItems = document.querySelectorAll('.rail-item');
            const slidingPill = document.getElementById('slidingPill');

            function updatePillPosition() {
                const activeItem = document.querySelector('.rail-item.active');
                if (!activeItem) return;

                const topPos = activeItem.offsetTop;
                const itemHeight = activeItem.offsetHeight;
                
                const rootStyles = getComputedStyle(document.documentElement);
                const pillHeight = parseInt(rootStyles.getPropertyValue('--pill-height')) || 36;
                
                const offset = topPos + (itemHeight - pillHeight) / 2;
                slidingPill.style.transform = `translateY(${offset}px)`;
            }

            window.addEventListener('load', updatePillPosition);
            window.addEventListener('resize', updatePillPosition);


            /* --- 3. Scroll Spy (Chỉ cập nhật Sidebar Rail khi KHÔNG trong quá trình click scroll tự động) --- */
            const sections = document.querySelectorAll('section[id]');
            
            const observerOptions = {
                root: null,
                rootMargin: '-20% 0px -75% 0px', 
                threshold: 0
            };

            const observer = new IntersectionObserver((entries) => {
                if (isScrollingToTarget) return;

                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const id = entry.target.getAttribute('id');
                        
                        // Chỉ cập nhật trạng thái Sidebar Rail
                        const correspondingRailItem = document.querySelector(`.rail-item[href="#${id}"]`);
                        if (correspondingRailItem && !correspondingRailItem.classList.contains('active')) {
                            railItems.forEach(nav => nav.classList.remove('active'));
                            correspondingNavItem = correspondingRailItem;
                            correspondingRailItem.classList.add('active');
                            updatePillPosition();
                        }
                    }
                });
            }, observerOptions);

            sections.forEach(section => observer.observe(section));


            /* --- 4. Huge Tab Navigation Logic (Hero Section) --- */
            const tabBtns = document.querySelectorAll('.tab-btn');
            const tabPanes = document.querySelectorAll('.tab-pane');

            tabBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    const targetId = this.getAttribute('data-target');
                    
                    tabBtns.forEach(b => b.classList.remove('active'));
                    this.classList.add('active');

                    tabPanes.forEach(pane => {
                        pane.classList.remove('active');
                    });

                    const targetPane = document.getElementById(targetId);
                    if (targetPane) {
                        targetPane.classList.add('active');
                    }
                });
            });

            /* --- 5. Custom Trailing Directed Cursor Logic (Hexagon) --- */
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

            const clickableSelector = 'a, button, [role="button"], .card, .tab-btn, .rail-item, .btn-buy, .link-learn, .search-input';

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

            /* --- 6. Xử lý Card Stacking Bị Cắt Nội Dung (Dynamic Sticky Top) --- */
            const stackedCards = document.querySelectorAll('.stacked-card');
            
            function adjustStackedCards() {
                const headerHeight = 60; 
                const windowHeight = window.innerHeight;
                
                stackedCards.forEach(card => {
                    const cardHeight = card.offsetHeight;
                    
                    if (cardHeight > (windowHeight - headerHeight)) {
                        card.style.top = `calc(100vh - ${cardHeight}px)`;
                    } else {
                        card.style.top = `${headerHeight}px`;
                    }
                });
            }

            if (window.ResizeObserver) {
                const resizeObserver = new ResizeObserver(adjustStackedCards);
                stackedCards.forEach(card => resizeObserver.observe(card));
                window.addEventListener('resize', adjustStackedCards);
            } else {
                window.addEventListener('load', adjustStackedCards);
                window.addEventListener('resize', adjustStackedCards);
            }

            /* --- 7. Fix Lỗi Cuộn Trang & Nhảy Loạn Active Indicator --- */
            
            // Hàm tính toán vị trí tuyệt đối (Flow Position) chuẩn xác bất chấp hiệu ứng Sticky ghim ảo
            function getAbsoluteFlowTop(targetElement) {
                let top = document.querySelector('.main-header').offsetHeight || 60;
                const mainSections = document.querySelectorAll('.main-content > section');
                
                for (let sec of mainSections) {
                    if (sec === targetElement) break;
                    top += sec.offsetHeight;
                }
                return top;
            }

            // Áp dụng tính năng mỏ neo gỡ rối cho các nút trong Sidebar Rail
            document.querySelectorAll('.rail-item[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function(e) {
                    const targetId = this.getAttribute('href');
                    if (targetId === '#') return;
                    
                    const targetElement = document.querySelector(targetId);
                    if (targetElement) {
                        e.preventDefault(); 
                        
                        // Bật cờ khóa Scroll Spy tạm thời trong lúc cuộn trang
                        isScrollingToTarget = true; 
                        
                        // Chủ động đặt trước Active Class cho item được nhấn để khóa cố định vị trí
                        document.querySelectorAll('.rail-item').forEach(nav => nav.classList.remove('active'));
                        this.classList.add('active');
                        updatePillPosition();

                        const targetTop = getAbsoluteFlowTop(targetElement);
                        const headerHeight = 60; 
                        
                        window.scrollTo({
                            top: targetTop - headerHeight + 5, 
                            behavior: 'smooth'
                        });

                        // Phát hiện hoàn tất cuộn để tắt cờ khóa
                        if (scrollEndTimeout) clearTimeout(scrollEndTimeout);
                        scrollEndTimeout = setTimeout(() => {
                            isScrollingToTarget = false;
                        }, 800); // 800ms đủ để hiệu ứng cuộn mượt hoàn tất
                    }
                });
            });

            // Lắng nghe sự kiện scroll kết thúc tự nhiên của trình duyệt (nếu được hỗ trợ) làm backup
            window.addEventListener('scrollend', () => {
                isScrollingToTarget = false;
                updatePillPosition();
            });

        });