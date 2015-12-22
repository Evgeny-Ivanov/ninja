define([
    'backbone',
    'router',
    'models/user',
    'views/superView'
], function(
    Backbone,
    router,
    modelUser,
    superView
){
	var View = superView.extend({
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        initialize: function () {
            _.bindAll(this,"render");
            this.model.on("change:isAutorization",this.render);
            this.model.on("change:errorAutorizationMessage",this.render);
            this.model.on("change:errorRegistrationMessage",this.render);
            this.render();
            $(document.body).append(this.$el);
            this.hide();
        },
        check: function(event) {
            var nameField = this.$el.find(event.target).attr("name");//узнаем что за поле 
            var value = this.$el.find(event.target).val();//узнаем что мы в него записали
            this.model.set(nameField,value);//записываем в модель
            this.validate();
                                    //По умолчанию метод validate вызывается только перед save, но также может быть 
                                    //вызван при выполнении set, если передать {validate:true} в хеше options.
            this.repaintInput(event);

            if(!this.model.isValid()){//проверяем модель на валидность
                console.log("isValid");
                return false;
            }
            console.log("return true");
            return true;
        },
        authorization: function(event){
            event.preventDefault();//отменяем стандартную отправку формы

            this.sendServer();
            if(this.model.get("isSuccess")){
                console.log("Успешно");
                Backbone.history.navigate('', { trigger: true });
            }
            else{
                return false;
            }
        },
        repaintInput: function(event){
            var type = this.$el.find(event.target).attr("type");
            var message = this.model.get(type+"Message");//получаем ошибку из модели
                                                         //если ошибки нет вернется пустая строка
            console.log("message: "+message)
            event.target.setCustomValidity(message);//если передать сюда пустую строку
                                                    //браузер воспримет что ошибки нет
            //устанавливаем тут ошибку - в main.css - псевдокласс который поменяет стили
        },
        show: function () {
            this.render();
            superView.prototype.show.apply(this,arguments);
        },
        hide: function(){
            superView.prototype.hide.apply(this,arguments);
            this.model.set("errorRegistrationMessage","");
            this.model.set("errorAutorizationMessage","");
        }
	});

	return View;
});