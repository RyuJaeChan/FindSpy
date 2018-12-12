let menu = {
    menu: document.querySelector(".menu"),
    menuButton: document.querySelector(".wrap .header_area .button_area .menu_button"),
    menuCloseButton: document.querySelector(".menu .close_button"),
    initialize: function() {
        this.menuButton.addEventListener("click", function() {
            this._showMenu();            
        }.bind(this));
        this.menuCloseButton.addEventListener("click", function() {
            this._hideMenu();
        }.bind(this));


    },
    _showMenu: function() {
        this.menu.style.display = "block";
    },
    _hideMenu: function() {
        this.menu.style.display = "none";
    }

}


menu.initialize();