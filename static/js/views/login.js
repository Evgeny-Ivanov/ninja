define([
    'backbone',
    'tmpl/login',
    'router',
    'models/user',
    'views/superAuthorizationView',
], function(
    Backbone,
    tmpl,
    router,
    modelUser,
    superAuthorizationView
){

    var View = superAuthorizationView.extend({
        id: "loginView",
        template: tmpl,
        model: modelUser,
        events: {
            'submit .ajax-signup': 'authorization',
            'input .ajax-signup': 'check',
        },
        validate: function(){
            this.model.validateLogin();
        },
        sendServer: function(){
            this.model.login();
        }

    });

    return new View();
});