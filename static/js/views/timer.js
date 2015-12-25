define([
	'backbone',
    'tmpl/timer',
    'models/timer'
],function(
    Backbone,
    tmpl,
    timerModel
    ){

	var View = Backbone.View.extend({
        model: new timerModel(),
        template: tmpl,
        initialize: function () {
            this.render();
            $("#gameView").prepend(this.$el);
            this.hide();
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        show: function (data) {
            var time = data.time_of_game;
            this.model.set("time",time);
            this.render();
            this.$el.show();
            var self = this;
            this.interval = setInterval(function(){
                var time = self.model.get("time") - 1;
                self.model.set("time",time);
                self.render();
                if(time <= 0) {
                    clearInterval(self.interval);
                }
            },1000);
        },
        hide: function () {
            clearInterval(this.interval);
            this.$el.hide();
        }
	});

	return View;

});