/* =========================================================================
   AI Chat Widget — Trợ lý Nexus
   Gọi POST /api/support/chat để nhận câu trả lời từ Groq Llama.
   ========================================================================= */
document.addEventListener('DOMContentLoaded', () => {

    const root      = document.getElementById('aiChat');
    if (!root) return;

    const toggleBtn = document.getElementById('aiChatToggle');
    const closeBtn  = document.getElementById('aiChatClose');
    const resetBtn  = document.getElementById('aiChatReset');
    const body      = document.getElementById('aiChatBody');
    const form      = document.getElementById('aiChatForm');
    const input     = document.getElementById('aiChatInput');
    const sendBtn   = document.getElementById('aiChatSend');

    let greeted = false;
    let waiting = false;

    /* --- Mở / đóng khung chat --- */
    function openChat() {
        root.classList.add('open');
        if (!greeted) {
            showGreeting();
            greeted = true;
        }
        setTimeout(() => input.focus(), 300);
    }
    function closeChat() { root.classList.remove('open'); }

    toggleBtn.addEventListener('click', () => {
        root.classList.contains('open') ? closeChat() : openChat();
    });
    closeBtn.addEventListener('click', closeChat);

    /* --- Lời chào + gợi ý câu hỏi --- */
    function showGreeting() {
        addBotMessage('Xin chào 👋 Mình là Trợ lý Nexus. Mình có thể giúp gì cho bạn về sản phẩm, đơn hàng, bảo hành hay đổi trả?');

        const suggestions = [
            'Chính sách bảo hành thế nào?',
            'Đổi trả sản phẩm ra sao?',
            'Tư vấn điện thoại cho mình',
            'Có những phương thức thanh toán nào?'
        ];
        const wrap = document.createElement('div');
        wrap.className = 'ai-chat-suggestions';
        suggestions.forEach(text => {
            const chip = document.createElement('button');
            chip.type = 'button';
            chip.className = 'ai-chip';
            chip.textContent = text;
            chip.addEventListener('click', () => {
                wrap.remove();
                sendMessage(text);
            });
            wrap.appendChild(chip);
        });
        body.appendChild(wrap);
        scrollToBottom();
    }

    /* --- Thêm tin nhắn vào khung --- */
    function addUserMessage(text) {
        const el = document.createElement('div');
        el.className = 'ai-msg user';
        el.textContent = text;
        body.appendChild(el);
        scrollToBottom();
    }
    function addBotMessage(text) {
        const el = document.createElement('div');
        el.className = 'ai-msg bot';
        el.textContent = text;
        body.appendChild(el);
        scrollToBottom();
    }

    /* --- Hiệu ứng "đang gõ" --- */
    function showTyping() {
        const el = document.createElement('div');
        el.className = 'ai-msg bot ai-typing';
        el.id = 'aiTyping';
        el.innerHTML = '<span></span><span></span><span></span>';
        body.appendChild(el);
        scrollToBottom();
    }
    function hideTyping() {
        const el = document.getElementById('aiTyping');
        if (el) el.remove();
    }

    function scrollToBottom() {
        body.scrollTop = body.scrollHeight;
    }

    /* --- Gửi câu hỏi tới server --- */
    async function sendMessage(text) {
        text = (text || '').trim();
        if (!text || waiting) return;

        addUserMessage(text);
        input.value = '';
        setWaiting(true);
        showTyping();

        try {
            const res = await fetch('/api/support/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text })
            });
            const data = await res.json();
            hideTyping();
            addBotMessage(data.reply || 'Xin lỗi, mình chưa nhận được câu trả lời. Bạn thử lại nhé.');
        } catch (err) {
            hideTyping();
            addBotMessage('Mất kết nối tới máy chủ. Bạn vui lòng kiểm tra mạng và thử lại.');
        } finally {
            setWaiting(false);
            input.focus();
        }
    }

    function setWaiting(state) {
        waiting = state;
        if (sendBtn) sendBtn.disabled = state;
    }

    /* --- Submit form --- */
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        sendMessage(input.value);
    });

    /* --- Bắt đầu hội thoại mới --- */
    if (resetBtn) {
        resetBtn.addEventListener('click', async () => {
            try { await fetch('/api/support/chat/reset', { method: 'POST' }); } catch (_) {}
            body.innerHTML = '';
            greeted = false;
            showGreeting();
            greeted = true;
        });
    }
});
