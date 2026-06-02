const tabs = document.querySelectorAll('.tabs .tab');
    const sections = document.querySelectorAll('.section');
    function showSection(targetId) {
        tabs.forEach(t => t.classList.remove('active'));
        sections.forEach(section => section.style.display = 'none');
        const activeTab = document.querySelector('.tabs .tab[href="#' + targetId + '"]');
        if (activeTab) activeTab.classList.add('active');
        document.getElementById(targetId).style.display = 'block';
    }

    tabs.forEach(tab => {
        tab.addEventListener('click', event => {
            event.preventDefault();
            const target = tab.getAttribute('href').substring(1);
            showSection(target);
        });
    });

    if (location.hash) {
        const target = location.hash.substring(1);
        showSection(target);
    } else {
        showSection('users');
    }