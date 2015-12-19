define([
    'backbone',
    'game/fruit',
    'game/sword',
    'game/messageSystem'
], function(
    Backbone,
    Fruit,
    Sword,
    MessageSystem
){
    function GameMechanics(canvas,model){

        this.model = model;
        this.canvas = canvas;
        this.context = canvas.getContext("2d");

        this.fruits = [];
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
                        self.cutFruit(fruit);
                    }
                });

                self.p1 = self.p2;
            }


        }
        this.canvas.addEventListener("mousemove",callback);
        this.canvas.addEventListener("touchmove",callback);

        this.canvas.addEventListener("mouseout",function(){
            self.p1 = null;
        });
        this.canvas.addEventListener("touchend",function(){
            self.p1 = null;
        });
    }

    GameMechanics.prototype.addFruit = function(data){
        var koef = data.koef;
        var fruit = new Fruit(0,0,this.canvas.height/12,this.canvas,data.id);
        fruit.setLaw(koef);
        this.fruits.push(fruit);
    }

    GameMechanics.prototype.cutFruit = function(fruit){
        this.messageSystem.sendMessage(fruit.id);
        this.fruits =  _.without(this.fruits, fruit);
        this.slicedFruits.push({
            fruit: fruit,
            time:0
        });
    }

    GameMechanics.prototype.deleteFruit = function(id){
        var fruit = null;
        _.each(this.fruits,function(f){
            if(f.id == id) fruit = f;
        });
        if(!fruit) return;
        this.fruits =  _.without(this.fruits, fruit);
        this.slicedFruits.push({
            fruit: fruit,
            time:0
        });
    }

    GameMechanics.prototype.finishGame = function(){
        var score = Math.round(Math.random()*100);//наш гемплей
        this.model.set('score',score);
    }

    GameMechanics.prototype.f = function(){
        var self  = this;
        addEventListener('deviceorientation',function(event){
            _.each(self.fruits,function(fruit){
                fruit.angle  = degreesToRadians(event.alpha);
            });
        })
    }

    GameMechanics.prototype.setMessageSystem = function(messageSystem){
        this.messageSystem = messageSystem;
    }

    function degreesToRadians(degrees){
        return degrees*(Math.PI/180);
    }

    function radiansToDegrees(radians){
        return radians/(Math.PI/180);
    }

    return GameMechanics;

});