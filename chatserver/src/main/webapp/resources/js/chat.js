
let stompClient = null;

function connect() {
    let socket = new SockJS("/socket/add");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame){
        console.log("connected : " + frame);
        stompClient.subscribe("/topic/messages", function(result) {
            console.log("result : " + result);
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    console.log("disconnected!!");
}



function send() {
    stompClient.send("/app/add", {}, "test message");
}


function appendMessage(msg){

}

connect();
