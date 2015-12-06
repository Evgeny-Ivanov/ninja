define([
	'helpers/sync'
],function(
	sync
){
	var storage = {
		prefix: 'queue-',
		isEmpty: function(){
			var lengthSet = 0;
			this.forEach(function(key){
				lengthSet++;
			});
			return lengthSet;
		},
		put: function(method,options,key){//sync = function(method,model,options){
			var item = {
				method: method,
				options: options
			}

			item = JSON.stringify(item);
			if(!key){
				var name = this.prefix + options.data.name;
				localStorage.setItem(name,item);
				this.count++;
			}
			else{
				localStorage.setItem(key,item);
			}

		},
		clearAll: function(){
			var self = this;
			this.forEach(function(key){
				localStorage.removeItem(key);
			});
		},
		clear: function(name){
			localStorage.removeItem(name);
		},
		send: function(){
			var self = this;
			this.forEach(function(key){
				var data = localStorage.getItem(key);
				data = JSON.parse(data);
				var method = data.method;
				var options = data.options;

				var model = null;//костыль
				options.success = function(data,textStatus,xhr){
					console.log("localStorage success send");
					self.clear(key);
				}
				options.error = function(xhr,textStatus,errorMessage){
					console.log("localStorage error send");
				}
				sync(method,model,options);
			});
		},
		forEach: function(func){
			for(i=0;i<localStorage.length;i++){
				var key = localStorage.key(i);
				var prefix = key.split("-")[0]+'-';
				if(prefix == this.prefix ){
					func(key);
				}
			}
		}
	};


	return storage;
});