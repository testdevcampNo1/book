// 챗봇 버튼과 모달 요소 가져오기
const chatbotButton = document.getElementById('chatbot-button');
const chatbotModal = document.getElementById('chatbot-modal');
const closeBtn = document.getElementsByClassName('chatbot-close')[0];

// 챗봇 버튼 클릭 시 모달 열기
chatbotButton.onclick = function () {
    chatbotModal.style.display = 'block';
}

// 모달의 닫기 버튼 클릭 시 모달 닫기
closeBtn.onclick = function () {
    chatbotModal.style.display = 'none';
}