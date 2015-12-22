define([
	'backbone'
], function(
	Backbone
){

	var Model = Backbone.Model.extend({
		urlRegistration: "/api/v1/auth/signup",
		urlLogin: "/api/v1/auth/signin",
		urlLogout: "/api/v1/auth/logout?",
        urlFetch: "/checkauth",
		defaults: {
			name: "",
			email: "",
			password: "",
			isRegistration: false,
			isAutorization: false,
			loginMessage: "",
			passwordMessage: "",
			emailMessage: "",
			errorRegistrationMessage: "",
            errorAutorizationMessage: ""
		},
        initialize: function(){
            _.bindAll(this,"successRegistation",
                           "successAutorization",
                           "successLogout",
                           "successFetch");
        },
		self: this,
		setting: {
            type: "POST",
            dataType: 'json'
		},
		validateRegistration: function(){
			var answerEmail = this.checkEmail();
			var answerLogin = this.checkLogin();
			var answerPassword = this.checkPassword();
			if(!answerEmail || !answerLogin || !answerPassword) validationError = "error"; 
		},
		validateLogin: function(){
			var answerEmail = this.checkEmail();
			var answerPassword = this.checkPassword();
			if(!answerEmail || !answerPassword) validationError = "error";
		},
		registration: function(){
			this.setting.error = this.errorSend;
            this.setting.success = this.successRegistation;
			this.setting.url = this.urlRegistration;
			this.setting.data = this.toJSON();
	        $.ajax(this.setting);
		},
		login: function(){
			this.setting.error = this.errorSend;
            this.setting.success = this.successAutorization;
            this.setting.url = this.urlLogin;
			this.setting.data = this.toJSON();
	        $.ajax(this.setting);
		},
		errorSend: function(xhr, status, error) {
	        alert(xhr.responseText + '|\n' + status + '|\n' +error);
	        console.log(xhr.responseText + '|\n' + status + '|\n' +error);
	        console.log("ajax error");
	    },
        successAutorization: function(answer){ 	
        	console.log(answer);
        	if(answer.status == 304){
        		var self = this;
                setTimeout(function(){
                    $.ajax(self.setting);
                },500);
        	} else if(answer.status == 200){
                this.set("isAutorization",true);
                this.set("name",answer.info);
                console.log(this);
            } else{
            	this.set("errorAutorizationMessage",answer.info);
            }
        },
        successRegistation: function(answer){ 	
        	console.log(answer);
            if(answer.status == 304){
                var self = this;
                setTimeout(function(){
                    $.ajax(self.setting);
                },500);
            } else if(answer.status == 200){
                console.log(answer);
                this.set("isRegistration",true);
                this.set("errorRegistrationMessage","Вы успешно зарегистрировались");
            } else{
                console.log("error registration")
                this.set("errorRegistrationMessage",answer.info);
            }
        },
        checkEmail: function(){
        	if(this.get("email").length<4){
        		this.set("emailMessage","Email слишком короткий");
        		return false;
        	}
			this.set("emailMessage","");
			return true;
        },
        checkPassword: function(){
			if(this.get("password").length<2) {
				this.set("passwordMessage","Пароль слишком короткий");
				return false;
			}
            if(this.get("password")=="12345") {
            	this.set("passwordMessage","Пароль 12345 небезопасен");
            	return false;
			}

			this.set("passwordMessage","");
			return true;
        },
        checkLogin: function(){
            if(this.get("name").length<4) {
            	this.set("loginMessage","Логин слишком короткий");
            	return false;
			}
        	this.set("loginMessage","");
        	return true
        },
        successLogout: function(answer){
            console.log(answer);
            if(answer.status == 304){
                var self = this;

                    $.ajax({
                        type: "GET",
                        url: self.urlLogout,
                        success: self.successLogout,
                        error: self.errorLogout,
                        dataType: 'json'
                    });

            } else if(answer.status == 200){
                this.set("isAutorization",false);
            } else{

            }
        },
        errorLogout: function(answer){
            console.log(answer);
        },
        logout: function(){
            console.log("logout");
        	var self = this;
        	$.ajax({
        		type: "GET",
        		url: self.urlLogout,
                success: self.successLogout,
                error: self.errorLogout,
                dataType: 'json'
        	});
        },
        fetch: function(){
            var self = this;
            $.ajax({
                type: "GET",
                url: self.urlFetch,
                success: self.successFetch,
                dataType: 'json'
            });
        },
        successFetch: function(answer){
            console.log(answer);
            if(answer.status == 200){
                var name = answer.info;
                this.set("name",name);
                this.set({"isAutorization":true});
            }
            if(answer.status == 401){
                this.set("isAutorization",false);
            }
        }

	});

	//в случае если пользователь залогинен нужно обновить модель
	var user = new Model();
    user.fetch();
	return user;
});