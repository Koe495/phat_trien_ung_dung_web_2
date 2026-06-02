/**
 * search.js — Shared expandable search bar with fuzzy dropdown
 * Dùng chung cho index, products, support
 * #searchWrapper, #searchInput, #searchBtn, #searchDropdown trong HTML
 */
(function () {
    const searchWrapper  = document.getElementById('searchWrapper');
    const searchInput    = document.getElementById('searchInput');
    const searchBtn      = document.getElementById('searchBtn');
    const searchDropdown = document.getElementById('searchDropdown');

    if (!searchWrapper || !searchInput || !searchDropdown) return;

    let timer = null;

    /* --- Mở / đóng expandable bar --- */
    searchBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        const isOpen = searchWrapper.classList.toggle('expanded');
        if (isOpen) {
            searchInput.focus();
        } else {
            searchInput.value = '';
            hideDropdown();
        }
    });

    /* --- Gõ vào input → debounce 250ms → gọi API --- */
    searchInput.addEventListener('input', () => {
        const q = searchInput.value.trim();
        clearTimeout(timer);

        if (q.length < 1) { hideDropdown(); return; }

        timer = setTimeout(() => fetchResults(q), 250);
    });

    /* --- Đóng khi click ra ngoài --- */
    document.addEventListener('click', (e) => {
        if (!searchWrapper.contains(e.target)) {
            hideDropdown();
            // Không thu gọn bar nếu vẫn có text
            if (!searchInput.value.trim()) {
                searchWrapper.classList.remove('expanded');
            }
        }
    });

    /* --- Đóng khi Escape --- */
    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            hideDropdown();
            searchInput.blur();
            searchWrapper.classList.remove('expanded');
        }
    });

    /* --- Fetch + render --- */
    async function fetchResults(q) {
        try {
            const res  = await fetch(`/api/search?q=${encodeURIComponent(q)}`);
            const data = await res.json();
            renderDropdown(data, q);
        } catch (err) {
            console.error('Search error:', err);
        }
    }

    function renderDropdown(results, q) {
        if (!results.length) {
            searchDropdown.innerHTML = `<div class="search-no-result">Không tìm thấy sản phẩm phù hợp.</div>`;
            showDropdown();
            return;
        }

        // Hiển thị tối đa 10, scroll được, highlight match
        searchDropdown.innerHTML = results.map(p => `
            <a href="/product/${p.slug}" class="search-result-item">
                <div class="search-result-thumb" style="background:${p.thumbnailBg || '#e5e5e7'}"></div>
                <div class="search-result-info">
                    <div class="search-result-name">${highlight(p.name, q)}</div>
                    <div class="search-result-meta">${p.category} · ${formatPrice(p.price)}</div>
                </div>
                <svg class="search-result-arrow" xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="9 18 15 12 9 6"></polyline>
                </svg>
            </a>
        `).join('');

        showDropdown();
    }

    /* Highlight ký tự khớp trong tên */
    function highlight(text, q) {
        if (!q) return text;
        const kw    = q.toLowerCase();
        let result  = '';
        let ti      = 0;
        const lower = text.toLowerCase();

        // Nếu keyword là chuỗi con liên tục → highlight đoạn đó
        const idx = lower.indexOf(kw);
        if (idx !== -1) {
            return text.slice(0, idx)
                + `<mark>${text.slice(idx, idx + kw.length)}</mark>`
                + text.slice(idx + kw.length);
        }

        // Fuzzy highlight từng ký tự khớp
        const matched = new Set();
        let ki = 0;
        for (ti = 0; ti < lower.length && ki < kw.length; ti++) {
            if (lower[ti] === kw[ki]) { matched.add(ti); ki++; }
        }
        return text.split('').map((ch, i) =>
            matched.has(i) ? `<mark>${ch}</mark>` : ch
        ).join('');
    }

    function formatPrice(price) {
        return Number(price).toLocaleString('vi-VN') + '₫';
    }

    function showDropdown() {
        searchDropdown.classList.add('visible');
    }

    function hideDropdown() {
        searchDropdown.classList.remove('visible');
        searchDropdown.innerHTML = '';
    }
})();