define([
    'backbone'
], function(
    Backbone
){
    function Sword(){
        this.maxTime = 5;
        this.lines = [];
        Sword.widthLine = 5;
    }

    Sword.prototype.addLine = function(line){
        this.lines.push(line);
    }

    Sword.prototype.garbageCollector = function(){
        var self = this;
        _.each(this.lines,function(line){
            line.time++;
            line.width = self.calculWidth(line.time);
            if(line.time == self.maxTime){
                self.lines = _.without(self.lines,line);
            }
        });
    }

    Sword.prototype.draw = function(context){
        _.each(this.lines,function(line){
            context.lineWidth = line.width;
            context.lineCap = 'butt'; // butt round square
            context.lineJoin = 'miter'; // miter round bevel
            context.strokeStyle = "#4c6575";
            context.beginPath();
            context.moveTo(line.p1.x,line.p1.y);
            context.lineTo(line.p2.x,line.p2.y);
            context.closePath(); // Замкнуть контур
            context.fill(); // Залить
            context.stroke(); // Обвести
        });
        this.garbageCollector();
    }

    Sword.prototype.calculWidth = function(time){
        return this.maxTime-time;
    }

    return Sword;

});



//Line = {
//    p1:{
//        x,
//        y
//    }
///    p2:{
//        x,
//        y
//    }
//    time:,
//    width
//
//}