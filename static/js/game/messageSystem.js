define([
    'models/score'
], function(
    scoreModel
){

    function MessageSystem(url,gameMechanics,scene){
        this.url = url;
        this.gameMechanics = gameMechanics;
        this.scene = scene;
        this.ws = null;
    } 


    MessageSystem.prototype.connect = function(){
        var ws = new WebSocket(this.url);
        this.ws = ws;
        var self = this;

        ws.onopen = function(event){
            console.log("open");
        }

        ws.onmessage = function(event){
            var data = JSON.parse(event.data);

            if(data.status == "start"){
                self.start(data);
            }

            if(data.status == "finish"){
                self.finish(data);
            }
            
            if(data.status == "newfruit"){
                self.newfruit(data);
            }

            if(data.status == "scores"){
                self.scores(data);
            }

            if(data.status=="message"){
                self.message(data);
            }

            if(data.status=="leave"){ 
                self.leave(data);
            }

            if(data.status=="enemyshot"){
                self.gameMechanics.deleteFruit(data.id);
            }

        }

        ws.onclose = function (event) {
            console.log("соеднинение закрыто");
        }
    }

    MessageSystem.prototype.start = function(data){

        console.log("Start: Пришло количество игроков: "+data.players.length);
        console.log("Их список: ");
        for(i=0;i<data.players.length;i++){
            console.log(data.players[i].name);
        }

        this.trigger("startGame",data);
    }

    MessageSystem.prototype.finish = function(data){
        this.trigger("gameOver",data);
    }


    MessageSystem.prototype.newfruit = function(data){
        var response = {};
        response.koef = {
            a: data.koef[0],
            b: data.koef[1],
            c: data.koef[2]
        };
        response.id = data.id;

        //console.log(response.koef.a, response.koef.b,response.koef.c);
        this.gameMechanics.addFruit(response);
    }

    MessageSystem.prototype.scores = function(data){
        console.log(data.status+" пришло количество игроков: "+data.players.length);
        console.log("их список: ");
        for(i=0;i<data.players.length;i++){
            console.log(data.players[i].name);
        }
        this.scene.stop();
        this.trigger("gameOver",data);
    }

    MessageSystem.prototype.message = function(data){

    }

    MessageSystem.prototype.leave = function(data){
        console.log("leave");
    }

    MessageSystem.prototype.sendMessage = function(id){
        var message = { 
            status : "myshot",
            id: id
        };
        message = JSON.stringify(message);
        this.ws.send(message);
    }

    return MessageSystem;
});