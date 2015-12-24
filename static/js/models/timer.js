define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
    	initialize: function(){
    	},
    	defaults: {
    		time: ''
    	}
    });
    return Model;
});