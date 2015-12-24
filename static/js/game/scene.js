define([
    'backbone',
    'game/gameMechanics',
    'views/gameover',
    'game/messageSystem'
], function(
    Backbone,
    GameMechanics,
    gameOverView,
    MessageSystem
){


    function Scene(canvas,gameMechanics){  
        this.canvas = canvas;
        var self = this;

        window.addEventListener("resize", function() {
            self.resize();
        }, false);

        var height = window.innerHeight;//546
        var width = window.innerWidth;//1082
        this.canvas.height = height;
        this.canvas.width = width;

        this.context = canvas.getContext('2d'); 
        this.img = new Image();
        this.img.src = "/background.jpg";
        this.context.drawImage(this.img,0,0,canvas.width,canvas.height);

        document.body.style.overflow = "hidden";
        this.gameMechanics = gameMechanics;
    } 
    

    Scene.prototype.run = function(){
        var self = this;
        self.gameMechanics.run();
        var animateThis = function(){
            self.r = requestAnimationFrame(animateThis);
            self.context.drawImage(self.img,0,0,self.canvas.width,self.canvas.height);
            _.each(self.gameMechanics.fruits,function(fruit){
                if(fruit.position.x > self.canvas.width || fruit.position.x < 0 ||  fruit.position.y > self.canvas.height){
                    self.gameMechanics.fruits =  _.without(self.gameMechanics.fruits,fruit);
                }
                fruit.show(self.canvas);
            });
            _.each(self.gameMechanics.slicedFruits,function(f){
                f.fruit.cut(self.context);
                f.fruit.time++;
                if(f.fruit.time == 49){
                    self.gameMechanics.slicedFruits =  _.without(self.gameMechanics.slicedFruits,f);
                }
            });
            self.gameMechanics.sword.draw(self.context);
            if(self.gameMechanics.finishGame()){
                self.showGameOver();
            }
        }
        animateThis();
    }

    Scene.prototype.stop = function(){
        cancelAnimationFrame(this.r);
    }

    Scene.prototype.resize = function(){
        this.canvas.height = window.innerHeight;
        this.canvas.width =  window.innerWidth;
        this.gameMechanics.changeSizeFruits();
    }

    Scene.prototype.showGameOver = function(){
        this.stop();
        gameOverView.show(this.model);
    }

    Scene.prototype.setMobileEvents = function(){
        var self = this;
        addEventListener('orientationchange',function(){

            self.canvas.height = window.innerHeight;
            self.canvas.width =  window.innerWidth;
            alert(self.canvas.height + " : " + self.canvas.width);

            if(window.orientation%180 === 0){
                //portrait
                alert("Советуем повернуть экран");
            } else {
                //landscape
            }

        });
    }

    return Scene;

});

