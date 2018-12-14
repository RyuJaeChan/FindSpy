document.addEventListener("DOMContentLoaded", initialize);

let chatManager = {
    chatArea: document.querySelector(".wrap .chat_area"),

    sendButton: document.querySelector(".input_area .send_button"),
    inputText: document.querySelector(".input_area .chat_input"),
    userId: document.querySelector(".user_id").dataset.user_id,
    
    recvTemplate: document.querySelector("#chat_template").innerText,
    myMessageTemplate: document.querySelector("#mymessage").innerText,

    lastUser: "",

    appendMessage: function(messageObj) {
        console.log("this.userId : " + this.userId);
        console.log("messageObj.writer : " + messageObj.writer);
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

        let ret = div.firstElementChild;
        if(messageObj.messageType == "DESCRIPTION") {
            ret.classList.add("decription_style");
        }

        return ret;
    },
    _scrollDown: function() {
        this.chatArea.scrollTop = this.chatArea.scrollHeight;
    },
    appendAlertMessage: function (messageStr) {
        let element = document.createElement("div");
        element.className = "alert_message";
        element.innerText = messageStr;
        this.chatArea.appendChild(element);

        this.lastUser = "";
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

                chatManager.appendAlertMessage("당신의 단어 : " + resultJson.word);

                socketClient.sendGameMessage({messageType : "WORD_OK"});
            }
        });
    },
    startDescription: function(messageObj) {
        let user = messageObj.message;
        if(user == this.userId) {
            console.log("user == this.userId is true");
            this.isMyTurn = true;
            chatManager.appendAlertMessage("당신의 차례입니다.");
        }
        else {
            chatManager.appendAlertMessage(user + "님의 차례입니다.");
        }
    },
    finishDescription: function() {
        this.isMyTurn = false;
        socketClient.sendGameMessage({messageType : "DESCRIPTION_FINISH"});
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
        msg.messageType = this.isMyTurn == true ? "DESCRIPTION" : "MESSAGE";

        socketClient.sendChatMessage(msg);
    },
    setMode: function(mode) {
        this.mode = mode;
    },
    votePlayer: function(playerName) {
        let msg = {};
        msg.messageType = "SELECT_OK";
        msg.message = playerName;

        socketClient.sendGameMessage(msg);
    }

};




function initialize() {
    let roomid =  document.querySelector(".roomid").dataset.roomid;
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
                case "JOIN":
                    chatManager.appendAlertMessage(messageObj.message);
                    break;
                case "QUIT":
                    chatManager.appendAlertMessage(messageObj.message);
                    break;
                case "MESSAGE":
                    console.log("MESSAGE");
                    chatManager.appendMessage(messageObj);
                    break;
                case "ALERT":
                    console.log("ALERT");
                    chatManager.appendAlertMessage(messageObj.message);
                    break;
                case "GAME_START":
                    console.log("GAME_START");
                    chatManager.appendAlertMessage("게임이 시작됩니다.");
                    //request ajax
                    GameManager.getWord();
                    break;
                case "DESCRIPTION_START":
                    GameManager.startDescription(messageObj);
                    break;
                case "DESCRIPTION":
                    chatManager.appendMessage(messageObj);
                    break;
                case "TIME_OUT":

                    break;
                case "SELECT_START":
                    chatManager.appendAlertMessage("구라쟁이를 지목해주세요.");

                    break;
                case "RESULT":
                    console.log("GAME_START");
                    chatManager.appendAlertMessage(messageObj.message);
                    break;
                default:
                    console.log("== WRONG MESSAGE TYPE ==");
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
    playButton.addEventListener("click", function() {
        GameManager.startGame();
    });

    let finishTurnButton = document.querySelector(".menu .discription_finish");
    finishTurnButton.addEventListener("click", function() {
        GameManager.finishDescription();
    })

    window.addEventListener("beforeunload", function(evt) {
        console.log("beforeunload evt!");
        socketClient.disconnect();
        return "byby";
    });
      

};

