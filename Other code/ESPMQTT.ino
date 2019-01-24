
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "Timer.h"


const char* ssid = "SetStaff";
const char* password = "giangvien2015";

const char* mqtt_server = "192.168.1.109";


WiFiClient espClient;
PubSubClient client(espClient);

int sensorPin = A0; // select the input pin for LDR
int sensorValue = 0; // variable to store the value coming from the sensor

Timer t;

void setup_wifi() {

  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  /*
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
*/
  String req = (char*)payload;

  Serial.println(req);
   // Switch the LED state according to the received value
  if (req.indexOf("ON") != -1) {
    digitalWrite(5, 1);
  } else if (req.indexOf("OFF") != -1) {
    digitalWrite(5, 0);
  } 
    else if (req.indexOf("true") !=-1) {
      Serial.println("Dang ky thanh cong");
      }
    else {
    Serial.println("Ban tin dang khac");
//    client.stop();
    return;
  }

}

void reconnect() {
  // Loop until we're reconnected
 
}

void setup() {
  Serial.begin(115200);
  setup_wifi();

   // Configure pin 5 for LED control
  pinMode(5, OUTPUT);
  digitalWrite(5, 0);
  
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  
   while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "Hello500ae";
  //  clientId += String(random(0xffff), HEX);
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      client.subscribe("/oneM2M/resp/mn-cse/ESP8266/json");
      client.publish("/oneM2M/req/ESP8266/mn-cse/json", "{\"m2m:rqp\":{\"m2m:fr\":\"admin:admin\",\"m2m:to\":\"/mn-cse/mn-name\",\"m2m:op\":1,\"m2m:rqi\": 123456,\"m2m:pc\":{\"m2m:ae\":{\"rn\":\"ESP8266\",\"api\":\"app-sensor\",\"rr\":\"false\"}},\"m2m:ty\":2}}");
  client.publish("/oneM2M/req/ESP8266/mn-cse/json","{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cnt\" : { \"rn\": \"Luminosity\" } }, \"m2m:ty\": 3 } }");
client.publish("/oneM2M/req/ESP8266/mn-cse/json", "{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266/Luminosity\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cin\" : { \"cnf\": \"message\", \"con\": 0 } }, \"m2m:ty\": 4 } }");
  client.publish("/oneM2M/req/ESP8266/mn-cse/json","{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cnt\" : { \"rn\": \"Led\" } }, \"m2m:ty\": 3 } }");
client.publish("/oneM2M/req/ESP8266/mn-cse/json", "{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266/Led\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cin\" : { \"cnf\": \"message\", \"con\": OFF } }, \"m2m:ty\": 4 } }");
client.publish("/oneM2M/req/ESP8266/mn-cse/json","{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"\/mn-cse\/mn-name\/ESP8266\/Led\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:sub\" : { \"rn\": \"sub_test1\", \"enc\": { \"net\": [3] }, \"nu\": [\"\/ESP8266\"], \"nct\": 1 } }, \"m2m:ty\": 23 } }");
  t.every(1000 * 10, push);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }

}

void loop() {
t.update();
  if (!client.connected()) {
    reconnect();
  }
  client.loop();  
     // client.publish("/oneM2M/req/AE/mn-cse/json", "{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266/DATA\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cin\" : { \"cnf\": \"message\", \"con\": \"10\" } }, \"m2m:ty\": 4 } }");
}
void push() {
  sensorValue = analogRead(sensorPin);
  Serial.println(sensorValue);
  String data=String()+ "{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266/Luminosity\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cin\" : { \"cnf\": \"message\", \"con\":" + sensorValue + "} }, \"m2m:ty\": 4 } }";
  client.publish("/oneM2M/req/ESP8266/mn-cse/json",data.c_str());
   //  client.publish("/oneM2M/req/AE/mn-cse/json", "{ \"m2m:rqp\" : { \"m2m:fr\" : \"admin:admin\", \"m2m:to\" : \"/mn-cse/mn-name/ESP8266/DATA\", \"m2m:op\" : 1, \"m2m:rqi\": 123456, \"m2m:pc\": { \"m2m:cin\" : { \"cnf\": \"message\", \"con\": \"20\" } }, \"m2m:ty\": 4 } }");

}
