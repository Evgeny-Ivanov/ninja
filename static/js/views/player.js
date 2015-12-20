define([
    'backbone',
    'tmpl/player',
    'models/user'
], function(
    Backbone,
    tmpl,
    userModel
){

    var View = Backbone.View.extend({
        className: "players",
        template: tmpl,
        model: userModel,
        events: {
            "click .js-logout": "logout"
        },
        initialize: function () {
            _.bindAll(this,'show');
            this.render();
            this.model.on("change:isAutorization",this.show);
            this.hide();
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        logout: function() {
            this.model.logout();
        },
        show: function () {
            $("#mainView").prepend(this.$el);
            this.render();
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }
    });

    return new View();
});