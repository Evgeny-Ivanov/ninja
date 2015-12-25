define([
    'backbone',
    'game/fruit',
    'game/sword',
    'game/messageSystem',
    'game/webAudio'
], function(
    Backbone,
    Fruit,
    Sword,
    MessageSystem,
    webAudio
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
            event.preventDefault();
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
        webAudio.play("bang");
        this.messageSystem.sendMessage(fruit.id);
        fruit.frame = 128;
        fruit.spriteExplosion = window.imgСache.animated2;
        this.fruits =  _.without(this.fruits, fruit);
        this.slicedFruits.push({
            fruit: fruit,
            time:0
        });
        if(fruit.id % 15 == 0){
            this.isRed = 8;
        }
    }

    GameMechanics.prototype.deleteFruit = function(id){
        var fruit = null;
        _.each(this.fruits,function(f){
            if(f.id == id) fruit = f;
        });
        if(!fruit) return;

        fruit.frame = 68;
        fruit.spriteExplosion = window.imgСache.animated3;

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

    GameMechanics.prototype.changeSizeFruits = function(){
        _.each(this.fruits,function(fruit){
            fruit.changeSize();
        });
    }

    GameMechanics.prototype.fillRed = function(){
        this.isRed-=1;
        this.context.beginPath();
        this.context.rect(0,0,this.canvas.width,this.canvas.height);
        this.context.closePath();
        this.context.fillStyle = "rgba(255,0,0,0." + this.isRed;
        this.context.fill();
    }

    function degreesToRadians(degrees){
        return degrees*(Math.PI/180);
    }

    function radiansToDegrees(radians){
        return radians/(Math.PI/180);
    }

    return GameMechanics;

});