define([
    'backbone',
    'tmpl/players'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        className: "players",
        collection: [],
        template: tmpl,
        initialize: function () {
            this.hide();
        },
        render: function (players) {
            this.$el.html(this.template(players));
            return this;
        },
        show: function (players) {
            this.render(players);
            $("#gameView").prepend(this.$el);
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }
    });

    return View;
});