require.config({//Настроить опции RequireJS можно через метод require.config()
    urlArgs: "_=" + (new Date()).getTime(),//дополнительные параметры при запросе скрипта(удобно использовать решая вопрос кеширования)
   										   //при загрузке каждого файла реккваерм он будет добавлять к url
   										   //рандомное значение - для того что бы сбрасывать кэш
   										   //чтоб при обновлении странички обновлялись и скрипты
    baseUrl: "js",//базовый путь, где лежат все модули
    paths: {// пути для модулей, которые находятся не в baseUrl
        jquery: "lib/jquery",
        underscore: "lib/underscore",
        backbone: "lib/backbone",
        deviceApiNormaliser: "lib/deviceapi-normaliser"
    },
    shim: {//Параметр shim позволяет добавить сторонние модули
    	   //(которые определены не в AMD стиле, или проще говоря: без метода define)
    	   //новая jquery (>1.7.1) уже умная – совместима с AMD: 
    	   //если обнаруживает метод define, то автоматически прописывает себя. 
    	   //Т.е. мы можем использовать модуль “jquery” без объявления его через shim.
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'deviceapi-normaliser': {
            exports: 'deviceOrientation'
        }
    }
});

define([
    'backbone',
    'router'
], function(
    Backbone,
    router
){


    Backbone.history.start();
});
