define([
    'backbone',
    'tmpl/main',
    'views/superView'
], function(
    Backbone,
    tmpl,
    superView
){

    var View = superView.extend({
        id: "mainView",
        template: tmpl
    });

    return new View();
});