define([
    'backbone',
    'tmpl/main',
    'views/superView',
    'models/user'
], function(
    Backbone,
    tmpl,
    superView,
    modelUser
){

    var View = superView.extend({
        id: "mainView",
        template: tmpl,
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        model: modelUser,  
        initialize: function () {
            superView.prototype.initialize.apply(this, arguments);
            _.bindAll(this,"render");
            this.model.on("change:isAutorization",this.render);
            
        }
    });

    return new View();
});