define([
    'backbone',
    'tmpl/game',
    'views/superView',
    'models/score',
    'game/scene',
    'game/fruit',
    'game/messageSystem',
    'game/gameMechanics',
    'views/loading',
    'views/gameover',
    'views/players'
], function(
    Backbone,
    tmpl,
    superView,
    scoreModel,
    Scene,
    Fruit,
    MessageSystem,
    GameMechanics,
    LoadingView,
    GameOverView,
    playersView
){

    var View = superView.extend({
        id: "gameView",
        model: new scoreModel(),
        template: tmpl,
        events: {
            "click .js-button-game": "sendMessage",
            "click .js-button-chat": "sendMessageChat"
        },
        players: new playersView(),
        show: function(){
            this.render();
            this.trigger("show");
            LoadingView.show();
            this.showCanvas();
        },
        showCanvas: function(){
            var canvas = this.$el.find(".js-canvas")[0];
            var gameMechanics = new GameMechanics(canvas,this.model);
            var scene = new Scene(canvas,gameMechanics);
            scene.run();
            var url = "ws://localhost:8080/gameplay";
            var messageSystem = new MessageSystem(url,gameMechanics,scene,this.players);
            _.extend(messageSystem, Backbone.Events);
            gameMechanics.setMessageSystem(messageSystem);

            this.listenTo(messageSystem,"startGame",this.showGame);
            this.listenTo(messageSystem,"gameOver",this.showGameOver);

            messageSystem.connect();
        },
        showGame: function(data){
            this.model.set("name",data.your_name);
            this.trigger("show");
            this.$el.show();
            this.players.show([]); 

        },
        showGameOver: function(data){
            var name = this.model.get("name");
            console.log(data);
            var score;
            for(i=0;i<data.players.length;i++){
                if(data.players[i].name == name){
                    score = data.players[i].score;
                }
            }
            this.model.set("score",score);
            this.players.hide();
            GameOverView.show(this.model);
        }
    });

    return new View();
});