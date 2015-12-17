define([
], function(
){

    function Fruit(px,py,radius){   
        //
        this.time = 0;
        this.radius = radius;
        this.initialAcceleration = 140;
        this.g = 98
        //
        this.color = "#F08080";
        this.position = {
            x: px,
            y: py
        };
        this.anchor = this.position;

    } 

    Fruit.prototype.show = function(context){

        this.position = this.law();
        context.beginPath();
        context.fillStyle = this.color;
        context.arc( 
            this.position.x, this.position.y,
            this.radius,
            0, Math.PI*2,
            false
        );
        context.closePath();
        context.fill();
    }

    Fruit.prototype.law = function(){
        //console.log(this.position);
        this.time++;
        this.initialAcceleration--;
        return {
            x: this.position.x, //this.position.x+this.time/4,
            y: this.position.y //(this.g-this.initialAcceleration)*this.time*this.time/1500+this.position.y
        }
    }

    Fruit.prototype.isBelongs = function(line){
        var xMin;
        var xMax;
        var yMin;
        var yMax;

        if(line.p1.x > line.p2.x){
            xMin = line.p2.x;
            xMax = line.p1.x;
        }
        else{
            xMin = line.p1.x;
            xMax = line.p2.x;
        }

        if(line.p1.y > line.p2.y){
            yMin = line.p2.y;
            yMax = line.p1.y;
        }
        else{
            yMin = line.p1.y;
            yMax = line.p2.y;
        }
        var step = 1;
        if( (xMin-this.radius+step<=this.anchor.x && xMax+this.radius-step>=this.anchor.x) && 
            (yMin-this.radius+step<=this.anchor.y && yMax+this.radius-step>=this.anchor.y) ){
            //console.log("x: ",xMin,xMax);
            //console.log("y: ",yMin,yMax);
            //console.log(this.position)
            return true;
        }

        return false;
    }

    Fruit.prototype.setRandomColor = function(){
        var red =  String(Math.round(Math.random()*255));
        var green =  String(Math.round(Math.random()*255));
        var blue =  String(Math.round(Math.random()*255));
        this.color = "rgb("+red+","+green+","+blue+")";
    }  

    Fruit.prototype.debug = function(context){
        context.beginPath();
        context.lineWidth = 5;
        context.arc( 
            this.anchor.x, this.anchor.y,
            this.radius,
            0, Math.PI*2,
            false
        );
        context.stroke();
        context.closePath();
    }

    Fruit.prototype.cut = function(context,line){
        var frame = 128; 
        var x = frame * this.tickX; 
        var y = frame * this.tickY;
        context.drawImage( 
          this.spriteExplosion,// изображение спрайт-листа
          x,y,128,128,  // исходные координаты (x,y,w,h)
          this.position.x,this.position.y,128,128 // конечные координаты (x,y,w,h)
        ); 
        this.tickX++;
        if(this.tickX == 6){
            this.tickX = 0;
            this.tickY++;
        }
    }

    Fruit.prototype.showCut = function(context,line){

    }

    Fruit.prototype.findPointIntersection = function(line){

    }

    Fruit.prototype.findPointIntersectionAxis = function(line){
        var bufLine = line.clone();
    }


    function calculLengthABC(){
        var a = calculLength(p1,p2);
        var b = calculLength(p2,this.position);
        var c = calculLength(p1,this.position);
        var angle = Math.acos( (c*c+b*b-a*a)/(2*c*b) );
    }

    function calculLength(p1,p2){
        return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y) );
    }


    function Smeshariki(px,py,radius){
        Fruit.apply(this, arguments);
        this.img = new Image();
        this.img.src = "/sovunya.png";
        this.anchor = null;

        this.spriteExplosion = new Image();
        this.spriteExplosion.src = "/animated.png";

        this.tickX = 0;
        this.tickY = 0;
    }

    Smeshariki.prototype = Object.create(Fruit.prototype);
    Smeshariki.prototype.constructor = Smeshariki;

    Smeshariki.prototype.show = function(context){
        this.position = this.law();
        this.anchor = {
            x: this.position.x + this.radius,
            y: this.position.y + this.radius
        };
        context.drawImage(this.img, this.position.x, this.position.y, this.radius*2,  this.radius*2); 
        this.debug(context);
    }

    return Smeshariki;

});