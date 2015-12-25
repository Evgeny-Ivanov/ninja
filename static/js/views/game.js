define([
    'backbone',
    'tmpl/game',
    'views/superView',
    'models/user',
    'game/scene',
    'game/fruit',
    'game/messageSystem',
    'game/gameMechanics',
    'views/loading',
    'views/gameover',
    'views/players',
    'collections/scores',
    'views/timer',
    'helpers/resourcesSystem'
], function(
    Backbone,
    tmpl,
    superView,
    userModel,
    Scene,
    Fruit,
    MessageSystem,
    GameMechanics,
    LoadingView,
    GameOverView,
    playersView,
    ScoresCollection,
    TimerView,
    loadResources
){

    var View = superView.extend({
        id: "gameView",
        model: userModel,
        template: tmpl,
        messageSystem: null,
        events: {
            "click .js-button-game": "sendMessage",
            "click .js-button-chat": "sendMessageChat"
        },
        initialize: function(){
            superView.prototype.initialize.apply(this,arguments);
            loadResources();
        },
        players: new playersView(),
        show: function(){
            this.render();
            this.trigger("show",this);
            LoadingView.show();
            this.showCanvas();
        },
        showCanvas: function(){
            var canvas = this.$el.find(".js-canvas")[0];
            var gameMechanics = new GameMechanics(canvas,this.model);
            var scene = new Scene(canvas,gameMechanics);
            scene.run();
            //g10.javaprojects.tp-dev.ru
            var url = "ws://g10.javaprojects.tp-dev.ru/gameplay";
            var messageSystem = new MessageSystem(url,gameMechanics,scene,this.players);
            _.extend(messageSystem, Backbone.Events);
            gameMechanics.setMessageSystem(messageSystem);

            var timerView = new TimerView();
            timerView.listenTo(messageSystem,"startGame",timerView.show);
            this.listenTo(messageSystem,"startGame",this.showGame);
            this.listenTo(messageSystem,"gameOver",this.showGameOver);

            this.messageSystem = messageSystem;
            messageSystem.connect();
        },
        showGame: function(data){
            this.model.set("name",data.your_name);
            this.trigger("show",this);
            this.$el.show();
            this.players.show(data.players); 

        },
        showGameOver: function(data){
            console.log(data);
            var self = this;
            var playerCollection = new ScoresCollection(data.players);
            var score = null;
            playerCollection.forEach(function(player){
                if(player.get("name") == self.model.get("name")){
                    player.set("isI",true);
                }
                
            });
            this.players.hide();
            GameOverView.show(playerCollection);
        },
        hide: function(){
            if(this.messageSystem){
                this.messageSystem.close();
            }
            document.body.style.overflow = "auto";
            superView.prototype.hide.apply(this,arguments);
        }
    });

    return new View();
});