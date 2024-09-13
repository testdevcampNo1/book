// 챗봇 버튼과 모달 요소 가져오기
const chatbotButton = document.getElementById('chatbot-button');
const chatbotModal = document.getElementById('chatbot-modal');
const closeBtn = document.getElementsByClassName('chatbot-close')[0];

// 챗봇 버튼 클릭 시 모달 열기
chatbotButton.onclick = function () {
    chatbotModal.style.display = 'block';

    // 모달이 열릴 때 iframe 내부 스크롤을 가장 아래로 이동시키기
    const iframe = document.querySelector(".chatbot-frame");

    // iframe이 로드된 후에 실행
    iframe.onload = function() {
        const iframeDocument = iframe.contentDocument || iframe.contentWindow.document;
        const chatbox = iframeDocument.getElementById('chatbox');

        if (chatbox) {
            // 스크롤을 맨 아래로 이동
            // chatbox.scrollTop = chatbox.scrollHeight;
            chatbox.scrollTop = 0;
        }
    }
}

// 모달의 닫기 버튼 클릭 시 모달 닫기
closeBtn.onclick = function () {
    chatbotModal.style.display = 'none';
}