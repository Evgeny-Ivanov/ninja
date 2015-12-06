define([
    'backbone',
    'tmpl/registration',
    'router',
    'models/user',
    'views/superAuthorizationView'
], function(
    Backbone,
    tmpl,
    router,
    modelUser,
    superAuthorizationView
){

//все нужно в модель 
//исправить BEM названия сущностей
//переписать коллекцию
//
    var View = superAuthorizationView.extend({
        id: "registrationView",
        template: tmpl,
        model: new modelUser(),
        events: {
            'submit .ajax-signin': 'authorization',
            'input .ajax-signin': 'check',
        },
        validate: function(){
            this.model.validateRegistration();
        },
        sendServer: function(){
            this.model.registration();
        }
    });

    return new View();
});