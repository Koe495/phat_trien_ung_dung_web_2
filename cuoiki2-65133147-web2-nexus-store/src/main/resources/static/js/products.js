// Constants configuration
        const CONFIG = {
            HEADER_HEIGHT: 60,
            LAYOUT_MAX_WIDTH: 1400,
            SCROLL_GAP: 24, // 1.5rem
            CURSOR_LERP: 0.16
        };

        /**
         * Lớp xử lý hiệu ứng Con trỏ chuột Lục giác
         */
        class CustomCursor {
            constructor() {
                this.cursor = document.getElementById('customCursor');
                this.hexagon = document.getElementById('customCursorHexagon');
                this.x = 0; this.y = 0;
                this.targetX = 0; this.targetY = 0;
                this.lastAngle = 0;
                this.clickableSelector = 'a, button, [role="button"], .product-card, .floating-scroll-btn, .search-input, .color-swatch';
                
                if (window.matchMedia('(pointer: fine)').matches) {
                    this.init();
                }
            }

            init() {
                window.addEventListener('mousemove', (e) => {
                    this.targetX = e.clientX;
                    this.targetY = e.clientY;
                    if (this.cursor.style.display !== 'block') this.cursor.style.display = 'block';
                });

                document.addEventListener('mouseover', (e) => {
                    if (e.target.closest(this.clickableSelector)) {
                        this.hexagon.classList.add('hovering');
                        this.hexagon.style.transform = '';
                    }
                });

                document.addEventListener('mouseout', (e) => {
                    const target = e.target.closest(this.clickableSelector);
                    if (target && (!e.relatedTarget || !e.relatedTarget.closest(this.clickableSelector))) {
                        this.hexagon.classList.remove('hovering');
                    }
                });

                this.animate();
            }

            animate() {
                const dx = this.targetX - this.x;
                const dy = this.targetY - this.y;
                
                this.x += dx * CONFIG.CURSOR_LERP;
                this.y += dy * CONFIG.CURSOR_LERP;
                
                this.cursor.style.left = `${this.x}px`;
                this.cursor.style.top = `${this.y}px`;
                
                if (!this.hexagon.classList.contains('hovering')) {
                    if (Math.hypot(dx, dy) > 1.5) {
                        this.lastAngle = Math.atan2(dy, dx) * 180 / Math.PI;
                    }
                    this.hexagon.style.transform = `rotate(${this.lastAngle + 90}deg)`;
                }
                
                requestAnimationFrame(this.animate.bind(this));
            }
        }

        /* Search được xử lý bởi search.js */

        /**
         * Lớp xử lý Thanh điều hướng (Sidebar Rail & Smooth Scroll)
         */
        class Navigation {
            constructor() {
                this.body = document.body;
                this.railToggle = document.getElementById('railToggle');
                this.railItems = document.querySelectorAll('.rail-item');
                this.slidingPill = document.getElementById('slidingPill');
                this.isScrolling = false;
                this.scrollTimeout = null;
                
                if(this.railToggle) this.init();
            }

            init() {
                this.railToggle.addEventListener('click', () => {
                    this.body.classList.toggle('rail-collapsed');
                    setTimeout(() => this.updatePill(), 400); 
                });

                document.querySelectorAll('.rail-item[href^="#"]').forEach(anchor => {
                    anchor.addEventListener('click', (e) => this.handleNavClick(e, anchor));
                });

                const observer = new IntersectionObserver((entries) => {
                    if (this.isScrolling) return;
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            const id = entry.target.getAttribute('id');
                            const activeNav = document.querySelector(`.rail-item[href="#${id}"]`);
                            if (activeNav && !activeNav.classList.contains('active')) {
                                this.setActiveNav(activeNav);
                            }
                        }
                    });
                }, { root: null, rootMargin: '-30% 0px -60% 0px', threshold: 0 });

                document.querySelectorAll('main > section[id]').forEach(sec => observer.observe(sec));

                window.addEventListener('load', () => this.updatePill());
                window.addEventListener('resize', () => this.updatePill());
            }

            handleNavClick(e, anchor) {
                const targetId = anchor.getAttribute('href');
                if (targetId === '#') return;
                
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    e.preventDefault(); 
                    this.isScrolling = true; 
                    
                    this.setActiveNav(anchor);

                    window.scrollTo({
                        top: targetElement.offsetTop - CONFIG.HEADER_HEIGHT, 
                        behavior: 'smooth'
                    });

                    if (this.scrollTimeout) clearTimeout(this.scrollTimeout);
                    this.scrollTimeout = setTimeout(() => { this.isScrolling = false; }, 800); 
                }
            }

            setActiveNav(element) {
                this.railItems.forEach(nav => nav.classList.remove('active'));
                element.classList.add('active');
                this.updatePill();
            }

            updatePill() {
                const activeItem = document.querySelector('.rail-item.active');
                if (!activeItem || !this.slidingPill) return;
                
                const pillH = parseInt(getComputedStyle(document.documentElement).getPropertyValue('--pill-height')) || 36;
                const offset = activeItem.offsetTop + (activeItem.offsetHeight - pillH) / 2;
                this.slidingPill.style.transform = `translateY(${offset}px)`;
            }
        }

        /**
         * Lớp xử lý thanh cuộn ngang sản phẩm & Dynamic Bleed
         */
        class HorizontalScroller {
            constructor(sectionElement) {
                this.section = sectionElement;
                this.scroller = sectionElement.querySelector('.product-scroller-inner');
                this.outer = sectionElement.querySelector('.product-scroller-outer');
                this.prevBtn = sectionElement.querySelector('.prev-btn');
                this.nextBtn = sectionElement.querySelector('.next-btn');
                
                if (this.scroller && this.outer) this.init();
            }

            init() {
                this.nextBtn?.addEventListener('click', () => this.scrollByCard(1));
                this.prevBtn?.addEventListener('click', () => this.scrollByCard(-1));

                this.scroller.addEventListener('scroll', () => this.updateUI());
                window.addEventListener('resize', () => this.updateUI());

                setTimeout(() => this.updateUI(), 150);
            }

            scrollByCard(direction) {
                const card = this.scroller.querySelector('.product-card');
                const scrollAmount = ((card ? card.offsetWidth : 320) + CONFIG.SCROLL_GAP) * direction; 
                this.scroller.scrollBy({ left: scrollAmount, behavior: 'smooth' });
            }

            updateUI() {
                const sLeft = this.scroller.scrollLeft;
                const sMax = this.scroller.scrollWidth - this.scroller.clientWidth;

                if (this.prevBtn) this.prevBtn.disabled = sLeft <= 2;
                if (this.nextBtn) this.nextBtn.disabled = sLeft >= sMax - 2;

                const winW = window.innerWidth;
                let padX = winW >= 1024 ? 64 : 32; 
                if (winW > CONFIG.LAYOUT_MAX_WIDTH) {
                    padX = (winW - CONFIG.LAYOUT_MAX_WIDTH) / 2 + padX;
                }
                
                const clipLeft = sLeft <= 2 ? padX : 0;
                const clipRight = sLeft >= sMax - 2 ? padX : 0;
                this.outer.style.clipPath = `inset(0px ${clipRight}px 0px ${clipLeft}px)`;
            }
        }

        /**
         * Lớp xử lý hiệu ứng Scroll Reveal
         */
        class ScrollReveal {
            constructor() {
                this.elements = document.querySelectorAll('.reveal-section');
                this.init();
            }

            init() {
                const observer = new IntersectionObserver((entries, obs) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.classList.add('visible');
                            obs.unobserve(entry.target); 
                        }
                    });
                }, { root: null, rootMargin: '0px 0px -10% 0px', threshold: 0.05 });

                this.elements.forEach(el => observer.observe(el));
            }
        }

        // ==========================================================================
        // KHỞI TẠO APP
        // ==========================================================================
        const app = {
            init() {
                new CustomCursor();
                new Navigation();
                new ScrollReveal();
                
                document.querySelectorAll('.category-section').forEach(section => {
                    new HorizontalScroller(section);
                });
            }
        };

        app.init();