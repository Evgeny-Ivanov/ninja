function Circle(x,y,radius,color){
	this.color = color;
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.count = 0;

	var maxVelocity = 10;
	var signX = 1;
	if(Math.random()>0.5) signX = -1;
	var signY = 1;
	if(Math.random()>0.5) signY = -1;

	this.velocity = {
		x : Math.round(Math.random()*5*signX),
		y : Math.round(Math.random()*5*signY)
	}

	this.id = "координаты: "+ String(x) + " , " + String(y) +" velocity: " + this.velocity.x+ " ," + this.velocity.y; 

}

Circle.prototype.draw = function(color,radius){
	this.canvas = canvas;
	this.context = canvas.getContext('2d');

	this.context.beginPath();
	this.context.fillStyle = color||this.color;
	this.context.arc( 
		this.x, this.y,
		radius||this.radius,
		0, Math.PI*2,
		false
	);
	this.context.closePath();
	this.context.fill();
}

Circle.prototype.clear = function(){
	this.draw("#FFFFFF",this.radius+1);
}

Circle.prototype.animate = function(){ 
	var self = this;
	var animateThis = function(){
		requestAnimationFrame(animateThis);
		self.clear();//шарики накладываются друг на друга т.к. они не проверяют будующего положения остальных
		self.x += self.velocity.x;//надо обдумать последовательность
		self.y += self.velocity.y;
		self.isСontact();
		self.draw();
	}
	animateThis();
} 


Circle.prototype.isPointInPath = function(x,y){

//if(!y) return;//костыль т.к. видимо в Math.sqrt бывает Nane и он приходит сюда
var imageData = this.context.getImageData(x,y,1,1);	
   

	
	//вроде работает - но до того как мы не закрасили канвас - его цвет не белый (255.255.255) а (0.0.0)
	//alert(String(imageData.data[0])+"   "+String(imageData.data[1])+"   "+String(imageData.data[2]));
    if(imageData.data[0] != 255 && imageData.data[1] != 255 && imageData.data[2] != 255) return true;
    else return false;
    //imageData.data[0] значение красного цвета (число от 0 до 255);
    //imageData.data[1] //значение зеленого цвета (число от 0 до 255);
    //imageData.data[2] //значение синего цвета (число от 0 до 255);
    //imageData.data[3] //значение прозрачности (число от 0 до 255);

}


Circle.prototype.isСontact = function(){

	var x = this.radius;
	var y = 0;
	var step = 1;
	var indent = 1;
	while(x>-this.radius){
		x = x - step;
		y = Math.round(Math.sqrt(this.radius*this.radius - x*x));

		if(x>0) var indentX = 1*indent;//отступ,при приближении на это растояние произойдет удар
		else var identX = -1*indent;
		if(y>0) var indentY = 1*indent;//отступ,при приближении на это растояние произойдет удар
		else var identY = -1*indent;

		if(x==0) indentX = 0;
		if(y==0) indentY = 0;

//////////
//	this.context.beginPath();
//	this.context.fillStyle = color||this.color;
//	this.context.rect( x + this.x + indentX, y + this.y + indentY,1,1 );
//	context.rect(x + this.x + indentX, -y + this.y + indentY,1,1 );
//	this.context.closePath();
//	this.context.fill();
//////////

		if(this.isPointInPath( x + this.x + indentX, y + this.y + indentY )){ 
			this.ifCollision( x + this.x + indentX, y + this.y + indentY);
			this.setRandomColor();
			this.count++;
			//console.log(this.count);
			return;

		}
		//else this.color = "#FF6672";
		if(this.isPointInPath( x + this.x + indentX, -y + this.y + indentY)){
			this.ifCollision( x + this.x + indentX, -y + this.y + indentY);
			this.setRandomColor();
			this.count++;
			//console.log(this.count);
			return;
		}
		//else this.color = "#FF6672";
	}

}


Circle.prototype.ifCollision = function(x,y){
    //нужно как то определить с каким шаром столкнулся наш шар
    //masCircle - как то нужно сюда пропехнуть
    canvas.height
	canvas.width//x
	var indent = 10;
    if(x>canvas.width-indent || x<indent){
    	this.velocity.x*=-1
    	return;
    }
    if(y>canvas.height-indent || y<indent){
    	this.velocity.y*=-1
    	return;
    }
    //использую глобальный объект - очень плохо
    var circle = this.findSecondCircle(x,y,masCircle);//стенку надо как то подругому обрабатывать
    if(!circle) return;
    console.log(this,circle);
    //до этого все работает более или менее не плохо

    //this.velocity.x*=-1;
	//this.velocity.y*=-1;
    //alert(circle);//фиксируются столкновения с самим собой
    //var xx = String(this.velocity.x);
    //var yy = String(this.velocity.y);
    this.velocity.x = Math.round( this.calculateVelocity(this.velocity.x,circle.velocity.x) ) ; 
	this.velocity.y = Math.round( this.calculateVelocity(this.velocity.y,circle.velocity.y) ) ;
	//alert("было: " + xx+" " + yy + " стало: " + String(this.velocity.x)+"  "+String(this.velocity.y));

}

Circle.prototype.calculateVelocity = function(v1,v2){
	//v1 - скорость нашего шара

	//v1,v2 - проэкции скорости первого и второго шаров
	//можно сделать еще более универсально если учитывать массу
	var D = (v1+v2)*(v1+v2) - 4*v1*v2;
	var V1 = -1*( (v1+v2) + Math.sqrt(D) )/2;
	var V2 = -1*( (v1+v2) - Math.sqrt(D) )/2;
	//не учитываем знаки 
	if( (v1<0 && v2<0) || (v2>0 && v1>0) ){
		//если первоначальная скорость нашего шара была меньше второго то скорость возрастет
		if(Math.abs(v1)<Math.abs(v2)) return V1;
		else return V2;
	}
	else{
		if(Math.abs(v1)<Math.abs(v2)) return V2;
		else return V1;
	}

}

Circle.prototype.setRandomColor = function(){
	var red =  String(Math.round(Math.random()*255));
	var green =  String(Math.round(Math.random()*255));
	var blue =  String(Math.round(Math.random()*255));
	this.color = "rgb("+red+","+green+","+blue+")";
}

Circle.prototype.findSecondCircle = function(x,y,masCircle){
	var newX = x;
	var newY = y;

	for(i=0;i<masCircle.length;i++){
		if(masCircle[i].checkAccessory(newX,newY)==true){
			if(masCircle[i]!==this)//костыль
				{	//alert(masCircle[i]);
					return masCircle[i];//почему то часто возвращается undefind
			}
			else return null;
		}
	}

	return null;

}

Circle.prototype.checkAccessory = function(x,y){
    var check = Math.sqrt( (this.x-x)*(this.x-x) + (this.y-y)*(this.y-y) );
    if(check <=  this.radius) return true;
    return false;
}

//Circle.prototype.setSmoothlyColor = function(){
//	if(this.red<255) this.red++;
//	this.green++;
//	this.blue++;
//}

function setBackgroundColor(color,context){
	height = document.documentElement.clientHeight;
	width = document.documentElement.clientWidth;
	context.beginPath();
	context.fillStyle = color;
	context.rect(0,0,width,height);
	context.closePath();
	context.fill();
	//Завершающий шаг это вызовом метода stroke или fill.
    //Собственно первый обводит фигуру линиями, а второй заливает фигуру сплошным цветом.
}



 

var canvas = $(".js-canvas")[0];
canvas.height = document.documentElement.clientHeight;//546
canvas.width = document.documentElement.clientWidth;//1082
context = canvas.getContext('2d');
//закрашиваем canvas 
setBackgroundColor("#FFFFFF",context);
var sizeCircle = 20;


var masCircle = []


for(i=0;i<masCircle.length;i++){//почему не заработал for in ?
	masCircle[i].masCircle = masCircle;//магия
	masCircle[i].draw();
	masCircle[i].animate();
}


//мышь в качестве объекта 
var counter = 0;
//onmousemove
canvas.onclick = function(evt) {
	counter+=1;
	var mouseX = evt.pageX - canvas.offsetLeft;
	var mouseY = evt.pageY - canvas.offsetTop;
	var circle = new Circle(mouseX,mouseY,50,"#FF6672");
	circle.draw();
	circle.animate();
	masCircle[masCircle.length] = circle;
	text = "Координаты "+mouseX+":"+mouseY;
	console.log(text);
}
