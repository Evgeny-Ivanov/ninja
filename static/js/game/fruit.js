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
        if( (xMin-this.radius+step<=this.position.x && xMax+this.radius-step>=this.position.x) && 
            (yMin-this.radius+step<=this.position.y && yMax+this.radius-step>=this.position.y) ){
        console.log("x: ",xMin,xMax);
        console.log("y: ",yMin,yMax);
        console.log(this.position)
            return true;
        }

        return false;
    }

    function calculLength(p1,p2){
        return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y) );
    }

    return Fruit;

});