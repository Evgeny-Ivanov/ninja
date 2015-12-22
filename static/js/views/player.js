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
        className: "status-bar",
        template: tmpl,
        model: userModel,
        events: {
            "click .js-logout": "logout"
        },
        initialize: function () {
            _.bindAll(this,'render');
            $(document.body).prepend(this.$el);
            this.model.on("change:isAutorization",this.render);
            this.hide();
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        logout: function() {
            console.log("logout");
            this.model.logout();
        },
        show: function () {
            this.render();
            this.$el.show();
        },
        hide: function(){
            this.$el.hide();
        }
    });

    return new View();
});