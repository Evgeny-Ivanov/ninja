define([
    'backbone',
    'game/fruit',
    'game/sword'
], function(
    Backbone,
    Fruit,
    Sword
){
    function GameMechanics(canvas){
        this.canvas = canvas;
        this.context = canvas.getContext("2d");

        //временно. Фрукты будут приходить из messageSystem
        var f1  = new Fruit(100,canvas.height,80);
        var f2  = new Fruit(100,300,80);
        var f3  = new Fruit(320,456,80);
        var f4  = new Fruit(700,10,80);

        this.fruits = [f1,f2,f3,f4];
        this.slicedFruits = [];
        this.p1 = null;
        this.p2 = null;
        this.sword = new Sword();
    } 

    GameMechanics.prototype.run = function(){
        this.checkСontactSwordAndFruit();
    }

    GameMechanics.prototype.checkСontactSwordAndFruit = function(){
        var self = this;
        var callback = function(evt) {
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


    return GameMechanics;

});