/*
var websocket = null;

function connect() {
    var socket = new SockJS('/ws'); // WebSocket endpoint 설정
    websocket = Stomp.over(socket);
    var token = getAuthToken();
    websocket.connect({}, onConnected, onError);
}
function getAuthToken() {
    // 쿠키 이름을 수정해야 할 수 있습니다.
    var cookieName = 'authToken';

    // 쿠키에서 토큰을 가져오기 위한 코드
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}

function onConnected() {
    console.log('Connected to WebSocket');
    websocket.subscribe('/topic/public', onMessageReceived);

    // '보내기' 버튼 이벤트 연결
    document.getElementById('send-button').addEventListener('click', sendMessage);
}

function onError(error) {
    console.error('WebSocket Error: ' + error);
}

function sendMessage() {
    var messageInput = document.getElementById('chat-input');
    var messageContent = messageInput.value.trim();

    if(messageContent && websocket) {
        var chatMessage = {
            content: messageContent,
            // user: "사용자 정보" // 필요한 경우 사용자 정보 추가

        };
        websocket.send("/chat/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';


    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (message.user && message.user.username) {
        var chatBox = document.getElementById('chat-box');
        var messageElement = document.createElement('div');
        messageElement.innerText = message.user.username + ': ' + message.content;
        chatBox.appendChild(messageElement);
    } else {
        console.error('Received message does not contain user information.');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    connect();
});*/
