// 챗봇 버튼과 모달 요소 가져오기
const chatbotButton = document.getElementById('chatbot-button');
const chatbotModal = document.getElementById('chatbot-modal');
const closeBtn = document.getElementsByClassName('chatbot-close')[0];
const sendBtn = document.getElementById('send-button');

// 챗봇 버튼 클릭 시 모달 열기
chatbotButton.onclick = function () {
    chatbotModal.style.display = 'block';
}

// 모달의 닫기 버튼 클릭 시 모달 닫기
closeBtn.onclick = function () {
    chatbotModal.style.display = 'none';
}
sendBtn.onclick = function () {
    console.log('click');
    const userInput = document.getElementById('user-input').value;
    console.log("userInput : ", userInput);

    if (userInput.trim() !== '') {
        // 메시지 전송
        const requestDiv = document.createElement('div');
        requestDiv.textContent = "고객: " + userInput;
        document.querySelector('.chatbot-modal-content').appendChild(requestDiv);

        // 사용자가 입력한 내용 초기화
        document.getElementById('user-input').value = '';

        // 응답 요청
        $.ajax({
            url: '/order/bot',
            type: 'GET',
            data: {prompt: userInput},
            success: function (res) {
                // 응답 화면에 노출
                const outputDiv = document.createElement('div');
                outputDiv.textContent = "정석문고: " + res;
                document.querySelector('.chatbot-modal-content').appendChild(outputDiv);

                // 사용자가 입력한 내용 초기화
                document.getElementById('user-input').value = '';
            },
            error: function (e) {
                console.error(e)
            }
        });
    }
}
