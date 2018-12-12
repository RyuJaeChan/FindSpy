let stompClient = null;

let socketClient = {
    socket: null,
    stompClient: null,
    roomid: null,
    path: document.querySelector(".path").dataset.path,
    userId: document.querySelector(".user_id").dataset.user_id,

    initialize: function (chatManager) {
        console.log("socketClient init");
        this.roomid = chatManager.roomid;

        // /**
        this.socket = new SockJS(this.path + "/sock");
        
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = () => {};  //console log off

        this.stompClient.connect({},
            function (frame) {
                console.log("connected : " + frame);


               let msg = {};
                msg.gameroomId = this.roomid;
                msg.writer = this.userId;

                this.stompClient.send("/game/join", {}, JSON.stringify(msg));
                this.stompClient.subscribe("/sub/gameroom/" + chatManager.roomid, function (message) {
                    let messageObj = JSON.parse(message.body);

                    console.log("message : " + messageObj);
                    console.log("message.gameroomId : " + messageObj.gameroomId);
                    console.log("message.writer" + messageObj.writer);
                    console.log("message.message : " + messageObj.message);
                    console.log("message.type : " + messageObj.type);

                    switch(messageObj.type) {
                        case "MESSAGE":
                            chatManager.appendMessage(messageObj);
                            break;
                        case "ALERT":
                            chatManager.appendAlertMessage(messageObj);
                            break;
                    }

                });
            }.bind(this),
            function(message) {
                console.log("====connect fail : " + message);
            }
        );
         //*/
    },
    disconnect: function () {
        let msg = {};
        msg.gameroomId = this.roomid;
        msg.writer = this.userId;
        //msg.message = message;

        this.stompClient.send("/game/quit", {}, JSON.stringify(msg));
        this.stompClient.disconnect();
        console.log("disconnected!!");
    },
    send: function (message) {
        let msg = {};
        msg.gameroomId = this.roomid;
        msg.writer = this.userId;
        msg.message = message;
        msg.type = "MESSAGE";

        console.log("send message : " + JSON.stringify(msg));

        this.stompClient.send("/game/message", {}, JSON.stringify(msg));
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
