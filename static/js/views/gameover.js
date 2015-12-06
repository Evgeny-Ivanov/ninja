define([
	"backbone",
	"views/superView",
	"tmpl/gameover",
	"models/score"
],function(
	Backbone,
	superView,
	tmpl,
	scoreModel
){

	var View = superView.extend({
		id: "gameoverView",
		template: tmpl,
		events: {
			"submit .js-ajax-send-score": "sendScore"
		},
		initialize: function () {
        },
		sendScore: function(event){
			event.preventDefault();
			this.listenModelStart();
			var name = $(".js-ajax-send-score-input").val();
			this.model.set("name",name);
			this.blockUnlockForm(event.target,true);
			this.model.save();
		},
		render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        show: function (model) {
        	this.model = model;
            this.trigger("show");
            this.render();
            $(document.body).append(this.$el); 
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        },
        showError: function(){
        	alert("error");
        	var form = this.$el.find(".js-ajax-send-score")[0];
        	this.blockUnlockForm(form,false);
        	this.listenModelStop();
        },
        showSuccess: function(){
			this.listenModelStop();
			Backbone.history.navigate("#scoreboard",{trigger: true});
        },
        listenModelStart: function(){
        	this.listenTo(this.model,"sync",this.showSuccess);
        	this.listenTo(this.model,"error",this.showError);
        },
        listenModelStop: function(){
        	this.stopListening(this.model,"sync");
        	this.stopListening(this.model,"error");
        },
        blockUnlockForm: function(target,value){
			var children = target.getElementsByTagName('*');
			$(children).prop("disabled", value); // true = блокируем форму
        }
	})
	return new View;

});