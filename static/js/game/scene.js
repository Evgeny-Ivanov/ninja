define([
    'backbone',
    'game/gameMechanics',
    'views/gameover'
], function(
    Backbone,
    GameMechanics,
    gameOverView
){

    function Scene(canvas,model){  
        this.canvas = canvas;
        this.model = model;
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

        this.gameMechanics = new GameMechanics(canvas,model);
    } 
    
    Scene.prototype.clear = function(){
        this.context.fillStyle = "white"; 
        this.context.fillRect(0,0,this.canvas.width,this.canvas.height); 
    }

    Scene.prototype.run = function(){
        var self = this;
        self.gameMechanics.run();
        var animateThis = function(){
            self.r = requestAnimationFrame(animateThis);
            self.clear();
            self.context.drawImage(self.img,0,0,self.canvas.width,self.canvas.height);
            _.each(self.gameMechanics.fruits,function(fruit){
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
        //нужно перерисовать фрукты
    }

    Scene.prototype.showGameOver = function(){
        document.body.style.overflow = "auto";
        this.stop();
        gameOverView.show(this.model);
    }

    return Scene;

});



//var basket =  new Image();
//basket.src = "/basket.png";
//self.context.drawImage(basket, 40, 40);