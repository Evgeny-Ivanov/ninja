define([
    'backbone',
    'tmpl/login',
    'router',
    'models/user',
    'views/superAuthorizationView',
    'views/player'
], function(
    Backbone,
    tmpl,
    router,
    modelUser,
    superAuthorizationView,
    PlayerView
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