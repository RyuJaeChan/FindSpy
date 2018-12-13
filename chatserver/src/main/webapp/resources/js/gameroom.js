document.addEventListener("DOMContentLoaded", initialize);

let chatManager = {
    chatArea: document.querySelector(".wrap .chat_area"),

    sendButton: document.querySelector(".input_area .send_button"),
    inputText: document.querySelector(".input_area .chat_input"),
    
    recvTemplate: document.querySelector("#chat_template").innerText,
    myMessageTemplate: document.querySelector("#mymessage").innerText,

    lastUser: "",

    appendMessage: function(messageObj) {
        if (this.userId == messageObj.writer) {
            this._appendMyMessage(messageObj);
        }
        else {
            this._appendRecieveMessage(messageObj);
        }
        this._scrollDown();
    },
    _appendRecieveMessage: function(messageObj) {
        this.chatArea.appendChild(this._createElement(this.recvTemplate, messageObj));
        
        this.lastUser = messageObj.writer;
    },
    _appendMyMessage: function(messageObj) {
        this.chatArea.appendChild(this._createElement(this.myMessageTemplate, messageObj));
        
        this.lastUser = messageObj.writer;
    },
    _createElement: function(template, messageObj) {
        let div = document.createElement("div");
        div.innerHTML += formatTemplate(template, messageObj);

        if(this.lastUser == messageObj.writer) {
            div.querySelector(".arrow").hidden = true;
            let name = div.querySelector(".name");
            if(name) {
                name.hidden = true;
            }
            let thumb = div.querySelector(".thumbnail_img");
            if(thumb) {
                thumb.hidden = true;
                div.querySelector(".thumbnail_area").style.height = "1px";
            }
        }

        return div.firstElementChild;
    },
    _scrollDown: function() {
        this.chatArea.scrollTop = this.chatArea.scrollHeight;
    },
    appendAlertMessage: function (messageObj) {
        let element = document.createElement("div");
        element.className = "alert_message";
        element.innerText = messageObj.message;
        this.chatArea.appendChild(element);

        this._scrollDown();
    },
    showDisconnectMessage: function() {
        //alert("서버와 연결이 끊어졌습니다.");
    },
};

let GameManager = {
    roomid: document.querySelector(".roomid").dataset.roomid,
    userId: document.querySelector(".user_id").dataset.user_id,
    path: document.querySelector(".path").dataset.path,

    isMyTurn: false,
    
    startGame: function() {
        socketClient.sendGameMessage({messageType : "GAME_REQUEST"});
    },
    getWord: function() {
        requestAjax({
            url: this.path + "/words",
            method: "GET",
            success: function (response) {
                let resultJson = JSON.parse(response);
                printObject(resultJson);
                console.log("response : " + resultJson.word);

                chatManager.appendAlertMessage({message : "당신의 단어 : " + resultJson.word});

                socketClient.sendGameMessage({messageType : "WORD_OK"});
            }
        });
    },
    /*
    sendMessage: function(inputValue) {
        if(this.mode == "GAME" && this.isMyTurn == false) {
            console.log("it is not my turn");
            return;
        }


        let msg = {};
        //msg.gameroomId = this.roomid;
        msg.writer = this.userId;
        msg.message = inputValue;
        msg.messageType = this.mode == "CHAT" ? "MESSAGE" : "CLUE";

        socketClient.send(msg);
    }, */
    sendChatMessage: function(inputValue) {
        let msg = {};
        msg.writer = this.userId;
        msg.message = inputValue;
        msg.messageType = "MESSAGE";

        socketClient.sendChatMessage(msg);
    },
    setMode: function(mode) {
        this.mode = mode;
    }
};




function initialize() {
    let roomid =  document.querySelector(".roomid").dataset.roomid;
    let userId = document.querySelector(".user_id").dataset.user_id;
    let path = document.querySelector(".path").dataset.path;

    /*
    *   - paramObj
    *   pathUrl         : connect Url
    *   roomId          : game room id
    *   callBack        : message 수신 처리
    *   failCallBack    : disconnect 실패 처리
    */
    socketClient.initialize({
        pathUrl: path,
        roomId : roomid,
        callBack: function(messageObj) {
            console.log("callBack func called!");
            switch(messageObj.messageType) {
                case "MESSAGE":
                    console.log("MESSAGE");
                    chatManager.appendMessage(messageObj);
                    break;
                case "ALERT":
                    console.log("ALERT");
                    chatManager.appendAlertMessage(messageObj);
                    break;
                case "GAME_START":
                    console.log("GAME_START");
                    chatManager.appendAlertMessage({message:"게임이 시작됩니다."});
                    //request ajax
                    GameManager.getWord();
                    break;
                case "DESCRIPTION_START":

                    break;
                case "TIME_OUT":

                    break;
                case "SELECT_START":
                
                    break;
                case "RESULT":

                    break;
            }
        },
        failCallBack: function() {
            alert("서버와 접속이 끊어졌습니다.");
        }
    });

    let sendButton = document.querySelector(".input_area .send_button");
    sendButton.addEventListener("click", function() {
        if(inputText.value == "") {
            return;
        }
        GameManager.sendChatMessage(inputText.value);
        inputText.value = "";
    });

    let inputText = document.querySelector(".input_area .chat_input");
    document.addEventListener("keydown", function(evt) {
        if(evt.keyCode === 13) {
            if(inputText.value == "") {
                return;
            }

            GameManager.sendChatMessage(inputText.value);
            inputText.value = "";
        }
    });

    let startButton = document.querySelector(".menu .start_button");
    startButton.addEventListener("click", function() {
        //GameManager.send
    });

    let playButton = document.querySelector(".wrap .header_area .r_area .play_button")
    playButton.addEventListener("click", function(){
        GameManager.startGame();
    });

    window.addEventListener("beforeunload", function (evt) {
        console.log("beforeunload evt!");
        socketClient.disconnect();
        return "byby";
    });
      

};

