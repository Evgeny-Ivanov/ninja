define([
    'backbone',
    'tmpl/game',
    'views/superView',
    'models/score',
    'game/scene',
    'game/fruit'
], function(
    Backbone,
    tmpl,
    superView,
    scoreModel,
    Scene,
    Fruit
){

    var View = superView.extend({
        id: "gameView",
        model: new scoreModel(),
        template: tmpl,
        events: {
            "click .js-button-game": "sendMessage",
            "click .js-button-chat": "sendMessageChat"
        },
        show: function(){
            this.render();
            this.trigger("show");
            this.$el.show(); 
            this.showCanvas();
        },
        showCanvas: function(){
            //var basket = this.$el.find(".basket")[0];
            //basket.ondragstart = function() {
            //  return false;
            //};

            var canvas = this.$el.find(".js-canvas")[0];
            var scene = new Scene(canvas,this.model);

            scene.run();
        }
    });

    return new View();
});