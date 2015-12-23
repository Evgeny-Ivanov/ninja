define([
    'backbone',
    'tmpl/players',
    'collections/scores',
    'models/user'
], function(
    Backbone,
    tmpl,
    scoresCollection,
    userModel
){

    var View = Backbone.View.extend({
        className: "players",
        collection: new scoresCollection(),
        model: userModel,
        template: tmpl,
        initialize: function () {
            this.hide();
        },
        render: function () {
            this.$el.html(this.template(this.collection.toJSON()));
            return this;
        },
        show: function (players) {
            this.collection.set(players);
            var self = this;
            this.collection.forEach(function(player){
                if(player.get("name") == self.model.get("name")){
                    player.set("isI",true);
                }
                
            });
            this.render();
            $("#gameView").prepend(this.$el);
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }
    });

    return View;
});