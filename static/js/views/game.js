define([
    'backbone',
    'tmpl/game',
    'views/superView',
    'views/gameover',
    'models/score'
], function(
    Backbone,
    tmpl,
    superView,
    gameOverView,
    scoreModel
){

    var View = superView.extend({
        id: "gameView",
        model: new scoreModel(),
        template: tmpl,
        events: {
            "click .js-gameover": "gameOver",
            "click .js-button-game": "sendMessage",
            "click .js-button-chat": "sendMessageChat"
        },
        gameOver: function(){
            var score = Math.round(Math.random()*100);//наш гемплей
            this.model.set('score',score);
            gameOverView.show(this.model);
        },
        ws: null,
        show: function(){
            this.render();
            this.trigger("show");
            this.$el.show(); 

            var started = false;
            var finished = false;
            var myName = "${name}";
            var enemyName = "";

            this.ws = new WebSocket("ws://localhost:8080/gameplay");

            this.ws.onopen = function (event) {
                console.log("соеднинение установлено");
            }
            
            this.ws.onmessage = this.wsOnmessage;

            this.ws.onclose = function (event) {
                console.log(event);
                console.log("соеднинение закрыто");
            }

            this.ws.onerror = function(error) {
              console.log("ошибка: ",error);
            }

        },
        sendMessage: function() {
            console.log("sendMessage");
            var message = { status : "increment" };
            message = JSON.stringify(message);
            this.ws.send(message);
        },
        sendMessageChat: function(event) {
            console.log("sendMessageChat");
            var text = String($("#chat").val());
            var message = { 
                status : "message",
                text : text
            };
            message = JSON.stringify(message);
            this.ws.send(message);
        },
        wsOnmessage:  function (event) {
            var data = JSON.parse(event.data);
            if(data.status == "start"){
                alert( "Старт игры" );
                document.getElementById("wait").style.display = "none";
                document.getElementById("gameplay").style.display = "block";
                document.getElementById("mychat").style.display = "block";

                document.getElementById("time_of_game").innerHTML = data.time_of_game;
                //document.getElementById("enemyName").innerHTML = data.enemyName;

                console.log("Start: Пришло количество игроков: "+data.players.length);
                console.log("Их список: ");
                for(i=0;i<data.players.length;i++){
                    console.log(data.players[i].name);
                    $('#score').append("<p id="+data.players[i].name+">"+data.players[i].name+ " : 0</p>");
                }
            }

            if(data.status == "finish"){
               alert( "финиш игры");
               document.getElementById("gameOver").style.display = "block";
               document.getElementById("gameplay").style.display = "none";
               document.getElementById("mychat").style.display = "none";

               document.getElementById("win").innerHTML = data.win;
            }

            if(data.status == "scores"){
                console.log(data.status+" пришло количество игроков: "+data.players.length);
                console.log("их список: ");
                for(i=0;i<data.players.length;i++){
                    console.log(data.players[i].name);
                    $("#"+data.players[i].name).html(data.players[i].name+ " : "+data.players[i].score);
                }
            }

            if(data.status=="message"){
                console.log(data);
                $("#textarea").append(" "+data.name+" : "+data.text+"\n");
            }

            if(data.status=="leave"){
                $("#"+data.name).hide();
            }

            //if(data.status == "increment" && data.name == document.getElementById("enemyName").innerHTML){
            //    document.getElementById("enemyScore").innerHTML = data.score;
            //}
        }
    });

    return new View();
});