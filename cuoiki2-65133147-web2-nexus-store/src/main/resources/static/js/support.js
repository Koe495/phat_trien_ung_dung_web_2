document.addEventListener('DOMContentLoaded', () => {

            let isScrollingToTarget = false; 
            let scrollEndTimeout = null;

            /* Search được xử lý bởi search.js */

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

            /* --- 6. Help Center Search: lọc Chủ đề + FAQ theo từ khóa --- */
            const helpSearchInput = document.querySelector('.big-search-input');
            if (helpSearchInput) {
                const topicsSection = document.getElementById('topics');
                const faqSection    = document.getElementById('faq');
                const topicCards    = Array.from(document.querySelectorAll('.support-topic-card'));
                const faqItemsAll   = Array.from(document.querySelectorAll('.faq-item'));

                // Bỏ dấu tiếng Việt + lowercase để tìm không phân biệt dấu (giữ nguyên độ dài chuỗi)
                const norm = (s) => (s || '')
                    .toLowerCase()
                    .normalize('NFD')
                    .replace(/[̀-ͯ]/g, '')
                    .replace(/đ/g, 'd');

                // Tách phần chữ của câu hỏi FAQ ra <span> riêng (giữ nguyên icon mũi tên)
                faqItemsAll.forEach(item => {
                    const btn = item.querySelector('.faq-question');
                    const svg = btn.querySelector('svg');
                    const text = btn.textContent.trim();
                    btn.textContent = '';
                    const span = document.createElement('span');
                    span.className = 'faq-q-text';
                    span.textContent = text;
                    btn.appendChild(span);
                    if (svg) btn.appendChild(svg);
                    item._qText = text;
                    const ansInner = item.querySelector('.faq-answer-inner');
                    item._aText = ansInner ? ansInner.textContent.trim() : '';
                });

                // Lưu text gốc của thẻ chủ đề
                topicCards.forEach(card => {
                    const h = card.querySelector('h3');
                    const p = card.querySelector('p');
                    card._hText = h ? h.textContent : '';
                    card._pText = p ? p.textContent : '';
                });

                // Thông báo "không có kết quả"
                const noResult = document.createElement('p');
                noResult.className = 'support-no-results';
                noResult.style.display = 'none';
                noResult.innerHTML = 'Không tìm thấy nội dung phù hợp. Bạn thử từ khóa khác, '
                                   + 'hoặc hỏi <strong>Trợ lý Nexus</strong> ở góc phải màn hình nhé.';
                faqSection.querySelector('.section-container').appendChild(noResult);

                // Tô sáng từ khóa trong 1 phần tử văn bản
                function highlight(el, original, q) {
                    if (!el) return;
                    if (!q) { el.textContent = original; return; }
                    const idx = norm(original).indexOf(norm(q));
                    if (idx === -1) { el.textContent = original; return; }
                    el.innerHTML = original.substring(0, idx)
                        + '<mark class="support-hl">' + original.substring(idx, idx + q.length) + '</mark>'
                        + original.substring(idx + q.length);
                }

                function runSearch(raw) {
                    const q  = raw.trim();
                    const nq = norm(q);

                    let topicMatches = 0;
                    topicCards.forEach(card => {
                        const match = !nq || norm(card._hText + ' ' + card._pText).includes(nq);
                        card.classList.toggle('support-hidden', !match);
                        if (match) topicMatches++;
                        highlight(card.querySelector('h3'), card._hText, q);
                        highlight(card.querySelector('p'),  card._pText, q);
                    });

                    let faqMatches = 0;
                    faqItemsAll.forEach(item => {
                        const match = !nq || norm(item._qText + ' ' + item._aText).includes(nq);
                        item.classList.toggle('support-hidden', !match);
                        if (match) faqMatches++;
                        highlight(item.querySelector('.faq-q-text'), item._qText, q);

                        const answer = item.querySelector('.faq-answer');
                        if (q && match) {                 // đang tìm & khớp -> tự mở
                            item.classList.add('active');
                            answer.style.maxHeight = answer.scrollHeight + 'px';
                        } else {                          // không khớp hoặc ô tìm trống -> đóng lại
                            item.classList.remove('active');
                            answer.style.maxHeight = 0;
                        }
                    });

                    topicsSection.style.display = (q && topicMatches === 0) ? 'none' : '';
                    noResult.style.display      = (q && faqMatches === 0) ? 'block' : 'none';
                }

                let searchTimer;
                helpSearchInput.addEventListener('input', (e) => {
                    clearTimeout(searchTimer);
                    const val = e.target.value;
                    searchTimer = setTimeout(() => runSearch(val), 180);
                });

                // Enter -> cuộn xuống khu vực kết quả
                helpSearchInput.addEventListener('keydown', (e) => {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        const target = topicsSection.style.display === 'none' ? faqSection : topicsSection;
                        window.scrollTo({ top: target.offsetTop - 60, behavior: 'smooth' });
                    }
                });
            }

        });