define([
], function(
){
    var methodMap = {
        'create': 'POST',
        'update': 'PUT',
        'patch':  'PATCH',
        'delete': 'DELETE',
        'read':   'GET'
    };
    //в options - те параметры, чьи значения по умолчанию мы хотим переопредилить

    // options = {
    //  url
    //  data
    //  error
    //  success
    //}
    var sync = function(method,model,options){//можно ли выпилить model
        var type = methodMap[method];
        var params = {
            type: type, 
            dataType: 'json'
        };
        params = _.extend(params, options);
        return $.ajax(params);

    };//на основе этой штуку пилим fetch,save,destroy


    return sync;

});




        //var type = methodMap[method];

        //var params = {type: type, dataType: 'json'};

        //if(!options.url){
        //    params.url = _.result(model,'url');
        //    //Если url — функция, она будет вызвана в контексте model; 
        //    //если нет — вернёт его значение.
        //}

        //if(options.data == null && model && 
        //    (method === "create" || method === "update" || method === "patch")){
        //    options.data = JSON.stringify(model.toJSON());
        //    params.contentType = 'application/json';
        //}

        //var xhr = options.xhr = $.ajax(_.extend(params, options));
        //model.trigger('request', model, xhr, options);//???
        //return xhr;//???