var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');
var request = require('request');
var app = express();

///////////////Parameters/////////////////
//CSE Params
var cseUri = "http://127.0.0.1:8080";

//AE params
var aeId = "Cae-monitor1";
var aeIp = "127.0.0.1";
var aePort = 3000;
//////////////////////////////////////////

app.use(bodyParser.json());
app.listen(aePort, function () {
	console.log("AE Monitor listening on: "+aeIp+":"+aePort);
});

var ledON = false;
app.post('/', function (req, res) {
	console.log("\n DAM NAY LA CAI ME GI NHI");
	console.log(req.body+ "\n" + "--------------");
	
//	console.log(body);

	var content = req.body["m2m:sgn"].nev.rep["m2m:cin"].con;
	console.log("Receieved luminosity: "+content);
	if(content>10 && ledON ){
		console.log("High luminosity => Switch led OFF");
		createContenInstance("OFF");
		ledON=false;
	}else if(content<=10 && !ledON){
		console.log("Low luminosity => Switch led ON");
		createContenInstance("ON")
		ledON=true;
	}else{
		console.log("Nothing to do");
	}
	res.sendStatus(200);	
});

createAE();
function createAE(){
	console.log("\n TAO AE - OK ROI");
	var method = "POST";
	var uri= cseUri+"/server";
	var resourceType=2;
	var requestId = "123456";
	var representation = {
		"m2m:ae":{
			"rn":"mymonitor1",			
			"api":"app.company.com",
			"rr":"true",
			"poa":["http://"+aeIp+":"+aePort]
		}
	};

	console.log(method+" "+uri);
	console.log(representation);

	var options = {
		uri: uri,
		method: method,
		headers: {
			"X-M2M-Origin": aeId,
			"X-M2M-RI": requestId,
			"Content-Type": "application/json;ty="+resourceType
		},
		json: representation
	};

	request(options, function (error, response, body) {
		console.log("◀◀◀◀◀");
		if(error){
			console.log(error);
		}else{
			console.log(response.statusCode);
			console.log(body);
			createSubscription();
		}
	});
}


function createSubscription(){
	console.log("\n SUB NAY - CHO NAY TOAN LOI");
	var method = "POST";
	var uri= cseUri+"/server/mydevice1/luminosity";
	var resourceType=23;
	var requestId = "123456";
	var representation = {
		"m2m:sub": {
			"rn": "subMonitor",
			"nu": ["Cae-monitor1"],
			"nct": 2,
			"enc": {
				"net": 3
			}
		}
	};

	console.log(method+" "+uri);
	console.log(representation);

	var options = {
		uri: uri,
		method: method,
		headers: {
			"X-M2M-Origin": aeId,
			"X-M2M-RI": requestId,
			"Content-Type": "application/json;ty="+resourceType
		},
		json: representation
	};

	request(options, function (error, response, body) {
		console.log("◀◀◀◀◀");
		if(error){
			console.log(error);
		}else{
			console.log(response.statusCode);
			console.log(body);
		}
	});
}

function createContenInstance(value){
	console.log("\n HAM NAY DUOC GOI NGAY TU TREN DAU - GUI BAN TIN VAO /LED");
	var method = "POST";
	var uri= cseUri+"/server/mydevice1/led";
	var resourceType=4;
	var requestId = "123456";
	var representation = {
		"m2m:cin":{
				"con": value
			}
		};

	console.log(method+" "+uri);
	console.log(representation);

	var options = {
		uri: uri,
		method: method,
		headers: {
			"X-M2M-Origin": aeId,
			"X-M2M-RI": requestId,
			"Content-Type": "application/json;ty="+resourceType
		},
		json: representation
	};

	request(options, function (error, response, body) {
		console.log("◀◀◀◀◀");
		if(error){
			console.log(error);
		}else{
			console.log(response.statusCode);
			console.log(body);
		}
	});
}


