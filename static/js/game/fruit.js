define([
], function(
){


    function Fruit(px,py,radius,canvas,id){   
        //
        this.id = id;
        this.time = 0;
        this.radius = radius;
        this.angle = 0;
        //
        this.color = "#F08080";
        this.position = {
            x: px,
            y: py
        };
        if(id%2==0){
            this.position.x = canvas.width;
            this.step = -(canvas.width / 200);
        } else {
            this.position.x = 0;
            this.step = (canvas.width / 200);
        }

        this.anchor = this.position;
    } 

    Fruit.prototype.setLaw = function(fun){
        this.serverFun = fun;
        var wc = this.canvas.width;
        var hc = this.canvas.height;

        var ka = 1/1000;
        var kb = 1/50;
        var kc = 1/2;
        var ws = 100;
        var hs = 100;
        var kx = wc/ws;
        var ky = hc/hs;
        var dx = wc/2;
        var dy = hc/2;

        var a = ky*(ka*fun.a/(kx*kx));
        var b = ky*(fun.b*kb/kx - ka*fun.a*ws/(kx*kx));
        var c = ky*(ka*fun.a*ws*ws/(4*kx*kx) + fun.b*kb*ws/(kx*2) + fun.c*kc) + hs/2;
        this.fun = {
            a: a,
            b: b,
            c: c
        }
    }

    Fruit.prototype.show = function(context){

        this.law();
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
        this.position.x += this.step;
        this.position.y = (this.fun.a * this.position.x * this.position.x +this.fun.b*this.position.x + this.fun.c);
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
        var frame = this.frame; 
        var x = frame * this.tickX; 
        var y = frame * this.tickY;

        context.drawImage( 
          this.spriteExplosion,
          x,y,frame,frame,  // исходные координаты (x,y,w,h)
          this.position.x,this.position.y,this.radius*2,this.radius*2 // конечные координаты (x,y,w,h)
        ); 

        this.tickX++;

        if(frame == 128 && this.tickX == 6){
            this.tickX = 0;
            this.tickY++;
        }

        if(frame == 68 && this.tickX == 4){
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


    function Smeshariki(px,py,radius,canvas){
        this.canvas = canvas;
        Fruit.apply(this, arguments);
        var id = this.id%5+1;
        this.img = window.imgСache["img"+id];
        this.anchor = {
            x: this.position.x + this.radius,
            y: this.position.y + this.radius
        };

        this.tickX = 0;
        this.tickY = 0;
    }

    Smeshariki.prototype = Object.create(Fruit.prototype);
    Smeshariki.prototype.constructor = Smeshariki;

    Smeshariki.prototype.show = function(canvas){
        this.law();
        this.anchor = {
            x: this.position.x + this.radius,
            y: this.position.y + this.radius
        };
        var context = canvas.getContext('2d');

        context.save();
        context.translate(this.position.x +  this.radius,this.position.y + this.radius);
        context.rotate(this.angle);
        context.drawImage(this.img, -this.radius,-this.radius, this.radius*2,  this.radius*2);
        context.restore();

        //this.debug(context);
    }

    Smeshariki.prototype.changeSize = function(){
        this.radius = this.canvas.height/12;
    }

    return Smeshariki;

});