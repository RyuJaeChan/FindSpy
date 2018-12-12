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


                let msg = {};
                console.log("paramObj.roomid : " + paramObj.roomId);
                msg.gameroomId = paramObj.roomId;
                msg.writer = "user2_name";

                console.log("send join : " + JSON.stringify(msg));
                this.stompClient.send("/game/join", {}, JSON.stringify(msg));

                this.stompClient.send("/game/test", {}, JSON.stringify(msg));

                this.stompClient.subscribe("/sub/gameroom/" + paramObj.roomId, function (message) {
                    let messageObj = JSON.parse(message.body);

                    console.log("message.gameroomId : " + messageObj.gameroomId);
                    console.log("message.writer" + messageObj.writer);
                    console.log("message.message : " + messageObj.message);
                    console.log("message.type : " + messageObj.type);

                    paramObj.callBack(messageObj);
                    /*
                    switch(messageObj.type) {
                        case "MESSAGE":
                            chatManager.appendMessage(messageObj);
                            break;
                        case "ALERT":
                            chatManager.appendAlertMessage(messageObj);
                            break;
                    }
                     */
                });
            }.bind(this),
            function(message) {
                console.log("====connect fail : " + message);
                paramObj.failCallBack();
            }
        );
         //*/
    },
    disconnect: function (roomId, userId) {
        let msg = {};
        msg.gameroomId = roomId;
        msg.writer = userId;
        //msg.message = message;

        this.stompClient.send("/game/quit", {}, JSON.stringify(msg));
        this.stompClient.disconnect();
        console.log("disconnected!!");
    },
    send: function (messageObj) {
        console.log("send message : " + JSON.stringify(messageObj));
        this.stompClient.send("/game/test", {}, JSON.stringify(messageObj));
        this.stompClient.send("/game/message", {}, JSON.stringify(messageObj));
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
