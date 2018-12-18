document.addEventListener("DOMContentLoaded", initialize);

let chatManager = {
    chatArea: document.querySelector(".wrap .chat_area"),

    sendButton: document.querySelector(".input_area .send_button"),
    inputText: document.querySelector(".input_area .chat_input"),
    userId: document.querySelector(".user_id").dataset.user_id,
    
    recvTemplate: document.querySelector("#chat_template").innerText,
    myMessageTemplate: document.querySelector("#mymessage").innerText,
    playerTemplate: document.querySelector("#select_message").innerText,

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
            if(div.querySelector(".name")) {
                ret.querySelector(".arrow").src = "/chatserver/img/left_gray_desc.png";
            }
            else {
                ret.querySelector(".arrow").src = "/chatserver/img/right_gray_desc.png";
            }
            div.querySelector(".message").classList.remove("message_color");
            div.querySelector(".message").classList.add("description_style");
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
    showUserList: function(players) {
        let div = document.createElement("div");

        console.log("players : " + players);
        
        div.classList.add("player_list");
        players.forEach(element => {
            console.log("=-====element : [" + element.trim()+"]");
            console.log("=-====elementttue : " + typeof(element));

            if(this.userId != element.trim()) {//자기는 제외한 플레이어 목록 표시
                div.innerHTML += formatTemplate(this.playerTemplate, {userName:element.trim()});
            }
        });

        div.addEventListener("click", function(evt) {
            console.log("evt.target.className : " + evt.target.className);
            if(evt.target.className != "player" ||
            evt.target.className != "user_name" ||
            evt.target.className != "thumbnail_img") {
                let userName = evt.target.closest(".player").dataset.user_id;
                GameManager.votePlayer(userName);
                return;
            }
            
        });

        this.chatArea.appendChild(div);
        this._scrollDown();
    }
};

let GameManager = {
    roomid: document.querySelector(".roomid").dataset.roomid,
    userId: document.querySelector(".user_id").dataset.user_id,
    path: document.querySelector(".path").dataset.path,

    descFinishButton: document.querySelector(".input_area .desc_finish"),

    players: [],    //username string

    isMyTurn: false,
    addPlayer: function(userName) {
        this.players.push(userName);
    },
    delPlayer: function(userName) {
        this.players = this.players.filter(item => item !== userName);
    },
    setPlayers: function(messagePlayers) {
        console.log("set messagePlayers : " + messagePlayers);
        this.players = messagePlayers.trim().slice( 1, -1).split(",");;
        console.log("set player : " + this.players);
    },
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
            this._showDescFinishButton();
        }
        else {
            chatManager.appendAlertMessage(user + "님의 차례입니다.");
        }
    },
    finishDescription: function() {
        this.isMyTurn = false;

        let msg = {};
        msg.messageType = "DESCRIPTION_FINISH";
        msg.writer = this.userId;
        socketClient.sendGameMessage(msg);

        this._hideDescFinishButton();
    },
    _showDescFinishButton:function() {
        console.log("flasle;!!");
        this.descFinishButton.hidden = false;
    },
    _hideDescFinishButton:function() {
        this.descFinishButton.hidden = true;
    },
    sendChatMessage: function(inputValue) {
        let msg = {};
        msg.writer = this.userId;
        msg.message = inputValue;
        msg.messageType = this.isMyTurn == true ? "DESCRIPTION" : "MESSAGE";

        socketClient.sendChatMessage(msg);
    },
    votePlayer: function(playerName) {
        let msg = {};
        msg.writer = this.userId;
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
            console.log("callBack func called! : messagetype : " + messageObj.messageType);
            switch(messageObj.messageType) {
                case "JOIN":
                    GameManager.setPlayers(messageObj.message);
                    chatManager.appendAlertMessage(messageObj.writer + "님이 참가하였습니다.");
                    break;
                case "QUIT":
                    GameManager.setPlayers(messageObj.message);
                    chatManager.appendAlertMessage(messageObj.writer + "님이 퇴장하였습니다.");
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
                    //todo
                    break;
                case "SELECT_START":
                    console.log("GameManager.players : " + GameManager.players);
                    chatManager.appendAlertMessage("구라쟁이를 지목해주세요.");
                    chatManager.showUserList(GameManager.players);
                    break;
                case "SELECT_OK":
                    chatManager.appendAlertMessage(messageObj.writer + "님이 지목을 완료했습니다.");
                    break;
                case "DESCRIPTION_RESTART":
                    socketClient.sendGameMessage({messageType : "WORD_OK"});
                    chatManager.appendAlertMessage("투표결과 동률이 나왔습니다.");
                    chatManager.appendAlertMessage("다시 설명을 해주세요.");
                    break;
                case "GAME_END":
                    chatManager.appendAlertMessage("지목된 대상은 " + messageObj.writer + "입니다.");
                    if(messageObj.message == "SUCCESS") {
                        chatManager.appendAlertMessage("범인을 찾았습니다!!");    
                    }
                    else {
                        chatManager.appendAlertMessage("선량한 시민을 지목했습니다...");
                    }

                    chatManager.appendAlertMessage("==== 게임 종료 ====");
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

    let playButton = document.querySelector(".wrap .header_area .r_area .play_button")
    playButton.addEventListener("click", function() {
        GameManager.startGame();
    });

    let finishTurnButton = document.querySelector(".menu .discription_finish");

    GameManager.descFinishButton.addEventListener("click", function() {
        GameManager.finishDescription();
    })

    window.addEventListener("beforeunload", function(evt) {
        console.log("beforeunload evt!");
        socketClient.disconnect();
        return "byby";
    });

};

