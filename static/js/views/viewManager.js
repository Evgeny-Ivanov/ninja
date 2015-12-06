define(['backbone'],function(Backbone){

	var ViewManager = Backbone.View.extend({
		views : [],
		add : function(view){
			this.views.push(view);
			this.listenTo(view,'show',this.hide);
		},
		addArray : function(array){
			var self = this;
			_.each(array,function(view){
				self.views.push(view);
				self.listenTo(view,'show',self.hide);
			});
		},
		hide : function(){
			var self = this;
			_.each(this.views,function(view){
				view.hide();
			});
		},
		initialize : function(){

		}
	});

	return new ViewManager();
});