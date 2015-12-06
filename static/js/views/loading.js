define([
    'backbone',
    'tmpl/loading',
    'views/superView'
], function(
    Backbone,
    tmpl,
    superView
){

    var View = superView.extend({
        id: "LoadingView",
        template: tmpl
    });

    return new View();
});