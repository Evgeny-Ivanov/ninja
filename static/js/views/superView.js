define([
	'backbone'
],function(Backbone){

	var View = Backbone.View.extend({
        initialize: function () {
            this.render();
            $(document.body).append(this.$el);
            this.hide();
        },
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        show: function () {
            this.trigger("show");
            this.$el.show();
        },
        hide: function () {
            this.$el.hide();
        }
	});

	return View;

});