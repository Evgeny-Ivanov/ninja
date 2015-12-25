define([
],function(	
){


	var WAudio  =  function(){
		// создаем аудио контекст
		this.context = new AudioContext();
		// переменные для буфера, источника и получателя
		this.buffer = null;
		this.source = null;
		this.destination = null;
		this.sounds = {};

	}

	// функция для подгрузки файла в буфер
	WAudio.prototype.loadSoundFile = function(url,name) {
		// делаем XMLHttpRequest (AJAX) на сервер
		var xhr = new XMLHttpRequest();
		xhr.open('GET', url, true);
		xhr.responseType = 'arraybuffer'; // важно

		var self = this;
		xhr.onload = function(e) {
		// декодируем бинарный ответ
		self.context.decodeAudioData(this.response,
		function(decodedArrayBuffer) {
		  // получаем декодированный буфер
		  console.log("success decoded");
		  self.sounds[name] = decodedArrayBuffer;
		}, function(e) {
		  console.log('Error decoding file', e);
		});

		};


		xhr.send();
	}

	// функция начала воспроизведения
	WAudio.prototype.play = function(name){
		// создаем источник
		console.log("play");
		this.source = this.context.createBufferSource();
		// подключаем буфер к источнику
		this.source.buffer = this.sounds[name];
		// дефолтный получатель звука
		this.destination = this.context.destination;
		// подключаем источник к получателю
		this.source.connect(this.destination);
		// воспроизводим
		this.source.start(0);
	}

	// функция остановки воспроизведения
	WAudio.prototype.stop = function(){
	  this.source.stop(0);
	}

	
	var wAudio = new WAudio();

	wAudio.loadSoundFile("bang.ogg","bang");

	return wAudio;

});