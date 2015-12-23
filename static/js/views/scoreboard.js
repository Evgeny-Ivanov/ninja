define([
    'backbone',
    'tmpl/scoreboard',
    'models/score',
    'collections/scores',
    'views/superView',
    'helpers/storage',
    'views/loading',
    'views/errorScoreboard'
], function(
    Backbone,
    tmpl,
    scoreModel,
    ScoreCollection,
    superView,
    storage,
    loadingView,
    errorView
){

    var View = superView.extend({
        id: "CollectionView",
        collection: new ScoreCollection(),
        template: tmpl,
        events: {
            'click .back-in-main-menu': 'hide'
        },
        render: function () {           
            this.$el.html(this.template(this.collection.toJSON()));
        },
        initialize: function(){
            this.render();
            $(document.body).append(this.$el);
            this.hide();
            this.listenTo(this.collection,"sync",this.showSuccess);
            this.listenTo(this.collection,"error",this.showError);
        },
        show: function () {
            if(storage.isEmpty()){
                storage.send();
            }
            this.showLoading();

            this.collection.fetch(5);
        },
        showSuccess: function(){
            this.trigger("show");
            this.render();
            this.$el.show();
        },
        showError: function(){
            errorView.show();
        },
        showLoading: function(){
            loadingView.show();
        },
        hide: function(){
            this.$el.hide();
        }
    });

    return new View();
});