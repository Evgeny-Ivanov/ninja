define([
], function(
){

    var loadResources = function(){
        var animated3 = new Image();
        var animated2 = new Image();
        animated2.src = "/animated2.png";
        animated3.src = "/animated3.png";

        var background = new Image();
        background.src = "/background.jpg";

        window.imgСache = {
            img1: new Image(),
            img2: new Image(),
            img3: new Image(),
            img4: new Image(),
            img5: new Image(),
        }
        var i = 1;
        _.each(window.imgСache,function(img){
            img.src = "/img/" + i + ".png";
            i++;
        });

        window.imgСache.animated2 = animated2;
        window.imgСache.animated3 = animated3;
        window.imgСache.background = background;
    }

    return loadResources;

});
