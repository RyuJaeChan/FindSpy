let stompClient = null;

let socketClient = {
    socket: null,
    stompClient: null,

    /*
    *   - paramObj
    *   pathUrl         : connect Url
    *   roomId          : game room id
    *   callBack        : message 수신 처리
    *   failCallBack    : disconnect 실패 처리
    */
    initialize: function (paramObj) {
        console.log("socketClient init");
 
        // /**
        this.socket = new SockJS(paramObj.pathUrl + "/sock");
        
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = () => {};  //console log off

        this.stompClient.connect({},
            function (frame) {
                console.log("connected : " + frame);                
                setTimeout(()=>{
                    console.log("join message : " + JSON.stringify({message:paramObj.roomId}));
                    this.stompClient.send("/game/join", {}, JSON.stringify({message:paramObj.roomId}));
                }, 0);
                
                this.stompClient.subscribe("/sub/gameroom/" + paramObj.roomId, function (message) {
                    let messageObj = JSON.parse(message.body);

                    //console.log("message.gameroomId : " + messageObj.gameroomId);
                    console.log("message.writer : " + messageObj.writer);
                    console.log("message.message : " + messageObj.message);
                    console.log("message.messageType : " + messageObj.messageType);

                    paramObj.callBack(messageObj);
                }.bind(this));
            }.bind(this),
            function(message) {
                console.log("====connect fail : " + message);
                paramObj.failCallBack();
            }
        );
         //*/
    },
    disconnect: function () {
        //dont need message
        this.stompClient.send("/game/quit", {}, {});
        this.stompClient.disconnect();
        console.log("disconnected!!");
    },
    sendChatMessage: function (messageObj) {
        console.log("send message : " + JSON.stringify(messageObj));
        this.stompClient.send("/game/message", {}, JSON.stringify(messageObj));
    },
    sendGameMessage: function(messageObj) {
        console.log("send message : " + JSON.stringify(messageObj));
        this.stompClient.send("/game/play", {}, JSON.stringify(messageObj));
    }
}




function connect() {
    let socket = new SockJS("./sock");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;   //console log off

    stompClient.connect({}, function (frame) {
        console.log("connected : " + frame);
        stompClient.subscribe("/sub/gameroom/", function (result) {
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


function appendMessage(msg) {

}

//connect();
