define([
], function(
){

    var loadResources = function(){
        var animated3 = new Image();
        var animated2 = new Image();
        animated2.src = "/burst.gif";
        animated3.src = "/blood.gif";

        var background = new Image();
        background.src = "/background.gif";

        window.imgСache = {
            img1: new Image(),
            img2: new Image(),
            img3: new Image(),
            img4: new Image(),
            img5: new Image(),
        }
        var i = 1;
        _.each(window.imgСache,function(img){
            img.src = "/img/" + i + ".gif";
            i++;
        });

        window.imgСache.animated2 = animated2;
        window.imgСache.animated3 = animated3;
        window.imgСache.background = background;
    }

    return loadResources;

});
