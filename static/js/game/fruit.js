define([
], function(
){

    function Fruit(px,py,radius){   
        this.radius = radius;
        this.position = {
            x: px,
            y: py
        };

    } 

    Fruit.prototype.show = function(context){

        var newPosition = this.law();
        context.beginPath();
        context.fillStyle = "#F08080";
        context.arc( 
            newPosition.x, newPosition.y,
            this.radius,
            0, Math.PI*2,
            false
        );
        context.closePath();
        context.fill();
    }

    Fruit.prototype.law = function(){
        return {
            x: this.position.x++,
            y: this.position.y++
        }
    }

    Fruit.prototype.isBelongs = function(line){
        var p1 = line.p1;
        var p2 = line.p2;
        var a = calculLength(p1,p2);
        var b = calculLength(p2,this.position);
        var c = calculLength(p1,this.position);

        var p = 0.5*(a+b+c);
        var h = 2*Math.sqrt(p*(p-a)*(p-b)*(p-c))/a;

        if(h<this.radius) return true;
        return false;
    }

    function calculLength(p1,p2){
        return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y) );
    }

    return Fruit;

});