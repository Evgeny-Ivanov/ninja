define([
	'backbone'
], function(
	Backbone
){

	var Model = Backbone.Model.extend({
		urlRegistration: "/api/v1/auth/signup",
		urlLogin: "/api/v1/auth/signin",
		defaults: {
			name: "",
			email: "",
			password: "",
			isSuccess: false,
			loginMessage: "",
			passwordMessage: "",
			emailMessage: ""
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
			var self = this;
			var setting = {
	            type: "POST",
	            url: self.urlRegistration,
	            dataType: 'json',
	            data: self.toJSON(),
	            error : self.errorSend,
	            success : self.successSend
		    };
	        $.ajax(setting);
		},
		login: function(){
			var self = this;
			var setting = {
	            type: "POST",
	            url: self.urlLogin,
	            dataType: 'json',
	            data: self.toJSON(),
	            error : self.errorSend,
	            success : self.successSend
		    };
	        $.ajax(setting);
		},
		errorSend: function(xhr, status, error) {
	        alert(xhr.responseText + '|\n' + status + '|\n' +error);
	        console.log(xhr.responseText + '|\n' + status + '|\n' +error);
	        console.log("ajax error");
	    },
        successSend: function(answer){ 	
            if(answer == "OK"){
                console.log("ajax success");
                this.isSuccess = true;
            }
            else{
            	console.log("С сервера что-то пришло:");
            	console.log(answer);
            	this.isSuccess = false;
            }
        },
        checkEmail: function(){
        	if(this.get("email").length<4){
        		console.log("error email");
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
            if(this.get("password")=="1234") {
            	this.set("passwordMessage","Пароль 1234 небезопасен");
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
        }

	});

	return new Model();
});