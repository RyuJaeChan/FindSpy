let stompClient = null;


function connect() {
    let socket = new SockJS("./sock");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;   //console log off

    stompClient.connect({}, function(frame){
        console.log("connected : " + frame);
        stompClient.subscribe("/sub/gameroom/", function(result) {
            console.log("result : " + result);
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    console.log("disconnected!!");
}

function send() {
    stompClient.send("/app/send", {}, "test message");
}


function appendMessage(msg){

}

connect();
