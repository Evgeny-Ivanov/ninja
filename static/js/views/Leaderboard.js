define([
    'backbone',
    'collection/scores'
], function(
    Backbone,
    ScoresCollection
){

    var View = superView.extend({
        el: document.getElementsByClassName("player"),
        collection: ScoresCollection,
        show: function(){
        }
    });

    return new View();
});