define([
    'backbone',
    'game/fruit',
    'game/sword'
], function(
    Backbone,
    Fruit,
    Sword
){
    function GameMechanics(canvas,model){
        this.model = model;
        this.canvas = canvas;
        this.context = canvas.getContext("2d");

        //временно. Фрукты будут приходить из messageSystem
        var f = [];
        for(i=0;i<3;i++){
            var width = canvas.width/(i+2);
            var height = canvas.height/(i+2);
            var radius = canvas.height/10;
            f[i] = new Fruit(width,height,radius);
        }

        this.fruits = f;
        this.slicedFruits = [];
        this.p1 = null;
        this.p2 = null;
        this.sword = new Sword();
    } 

    GameMechanics.prototype.run = function(){
        this.checkСontactSwordAndFruit();
        this.f();
    }

    GameMechanics.prototype.checkСontactSwordAndFruit = function(){
        var self = this;
        var callback = function(event) {
            if(event.touches) evt = event.touches[0];
            else evt = event;
        
            mouseX = evt.pageX - self.canvas.offsetLeft;
            mouseY = evt.pageY - self.canvas.offsetTop;

            if(!self.p1){
                self.p1 = {
                    x: mouseX,
                    y: mouseY
                }
            }
            else{
                self.p2 = {
                    x: mouseX,
                    y: mouseY
                }

                var line = {
                    p1: self.p1,
                    p2: self.p2,
                    time: 0,
                    width: Sword.widthLine
                };

                self.sword.addLine(line);

                 _.each(self.fruits,function(fruit){
                    if(fruit.isBelongs(line)){
                        console.log("OK");
                        self.cutFruit(fruit);
                    }
                });

                self.p1 = self.p2;
            }


        }
        this.canvas.addEventListener("mousemove",callback);
        this.canvas.addEventListener("touchmove",callback);
    }

    GameMechanics.prototype.addFruit = function(fruit){

    }

    GameMechanics.prototype.cutFruit = function(fruit){
        console.log(this.fruits);
        this.fruits =  _.without(this.fruits, fruit);
        this.slicedFruits.push({
            fruit: fruit,
            time:0
        });
        //fruit.setRandomColor();
        console.log(this.fruits);
    }

    GameMechanics.prototype.finishGame = function(){
        var score = Math.round(Math.random()*100);//наш гемплей
        this.model.set('score',score);
        if(this.fruits.length == 0 && this.slicedFruits.length == 0) return true;
        return false;
    }

    GameMechanics.prototype.f = function(){
        var self  = this;
        addEventListener('deviceorientation',function(event){
            _.each(self.fruits,function(fruit){
                fruit.angle  = degreesToRadians(event.alpha);
            });
        })
    }

    function degreesToRadians(degrees){
        return degrees*(Math.PI/180);
    }

    function radiansToDegrees(radians){
        return radians/(Math.PI/180);
    }

    return GameMechanics;

});