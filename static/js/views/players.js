define([
    'backbone',
    'tmpl/players'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        className: "players",
        collection: [{name:"sdfsdf",score:"10"}],
        template: tmpl,
        initialize: function () {
            this.hide();
        },
        render: function () {
            this.$el.html(this.template(this.collection));
            return this;
        },
        show: function () {
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