document.addEventListener("DOMContentLoaded", initialize);

let chatManager = {
    chatArea: document.querySelector(".wrap .chat_area"),

    sendButton: document.querySelector(".input_area .send_button"),
    inputText: document.querySelector(".input_area .chat_input"),
    
    recvTemplate: document.querySelector("#chat_template").innerText,
    myMessageTemplate: document.querySelector("#mymessage").innerText,

    roomid: document.querySelector(".roomid").dataset.roomid,
    userId: document.querySelector(".user_id").dataset.user_id,
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

    }

};




function initialize() {
    socketClient.initialize(chatManager);

    let sendButton = document.querySelector(".input_area .send_button");
    let inputText = document.querySelector(".input_area .chat_input");

    sendButton.addEventListener("click", function() {
        if(inputText.value == "") {
            return;
        }

        console.log("send button evt inputText.value : " + inputText.value);
        socketClient.send(inputText.value);
        inputText.value = "";
    });

    document.addEventListener("keydown", function(evt) {
        if(evt.keyCode === 13) {
            socketClient.send(inputText.value);
            inputText.value = "";
        }
    });

    window.addEventListener("beforeunload", function (evt) {
        console.log("beforeunload evt!");
        socketClient.disconnect();
        return "byby";
    });
      

};

