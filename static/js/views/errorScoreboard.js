define([
    'backbone',
    'tmpl/errorScoreboard',
    'views/superView',
    'views/scoreboard'
], function(
    Backbone,
    tmpl,
    superView,
    scoreboard
){

    var View = superView.extend({
        id: "ErrorScoreboardView",
        template: tmpl,
        events:{
            "click .js-try-again-send": "send"
        },
        send: function(){
            Backbone.history.navigate("#main", {trigger: true, replace: true});//божественный костыль
            Backbone.history.navigate("#scoreboard", {trigger: true, replace: true});
        }
    });

    return new View();
});