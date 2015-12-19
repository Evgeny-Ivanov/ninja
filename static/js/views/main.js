define([
    'backbone',
    'tmpl/main',
    'views/superView',
    'models/user'
], function(
    Backbone,
    tmpl,
    superView,
    modelUser
){

    var View = superView.extend({
        id: "mainView",
        template: tmpl,
        render: function () {
            this.$el.html(this.template(this.model));
            return this;
        },
        model: modelUser  

    });

    return new View();
});