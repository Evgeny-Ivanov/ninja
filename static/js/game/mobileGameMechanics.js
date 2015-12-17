define([
    'backbone',
    'deviceApiNormaliser'
], function(
    Backbone
){
    function GameMechanics(canvas){
    }


    GameMechanics.prototype.deviceorientationC = function(){
    	this.canvas.addEventListener("deviceorientation",function(events){
             var orientation = deviceOrientation(e);
             console.log(orientation.alpha,orientation.beta,orientation.gamma);
        });
    }



});

