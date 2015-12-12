define([
    'backbone',
    'tmpl/game',
    'views/superView',
    'views/gameover',
    'models/score',
    'game/scene',
    'game/fruit'
], function(
    Backbone,
    tmpl,
    superView,
    gameOverView,
    scoreModel,
    Scene,
    Fruit
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
        show: function(){
            this.render();
            this.trigger("show");
            this.$el.show(); 
            this.showCanvas();
        },
        showCanvas: function(){
            var basket = this.$el.find(".basket")[0];
            basket.ondragstart = function() {
              return false;
            };

            var canvas = this.$el.find(".js-canvas")[0];
            var scene = new Scene(canvas);
            scene.clear();

            scene.run();
        }
    });

    return new View();
});