define([
    'backbone',
    'game/gameMechanics'
], function(
    Backbone,
    GameMechanics
){

    function Scene(canvas,height,width){  
        this.canvas = canvas;

        var height = document.documentElement.clientHeight;//546
        var width = document.documentElement.clientWidth;//1082

        canvas.height = height-20;//546
        canvas.width = width*3/4;//1082

        this.context = canvas.getContext('2d');
        this.gameMechanics = new GameMechanics(canvas);
    } 
    
    Scene.prototype.clear = function(){
        this.context.fillStyle = "white"; 
        this.context.fillRect(0,0,this.canvas.width,this.canvas.height); 
    }

    Scene.prototype.run = function(){
        var self = this;
        self.gameMechanics.run();
        var animateThis = function(){
            requestAnimationFrame(animateThis);
            self.clear();
            _.each(self.gameMechanics.fruits,function(fruit){
                fruit.show(self.context);
            });
            _.each(self.gameMechanics.slicedFruits,function(f){
                f.fruit.cut(self.context);
                f.fruit.time++;
                if(f.fruit.time == 49){
                    self.gameMechanics.slicedFruits =  _.without(self.gameMechanics.slicedFruits,f);
                }
            });
            self.gameMechanics.sword.draw(self.context);
        }
        animateThis();
    }

    Scene.prototype.stop = function(){

    }

    return Scene;

});



//var basket =  new Image();
//basket.src = "/basket.png";
//self.context.drawImage(basket, 40, 40);