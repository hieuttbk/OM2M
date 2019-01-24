package it.unipi.iot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyManager {

	static String originator="admin:admin";
	private static String cseProtocol="coap";
	private static String cseProtocol2="http";

	private static String cseIp = "127.0.0.1";
	private static int csePort = 5683;				// IN port
	private static int csePort2 = 8080;				// IN port

	private static String cseId = "in-cse";
	private static String cseName = "in-name";

	private static String aeName = "mymanager";
	private static String aeProtocol="coap";
	private static String aeIp = "127.0.0.1";
	static int aePort = 5682;				// My manager port
	private static String subName="managersub";
	private static String targetSensorContainer="mn-cse/mn-name/mysensor/data";
	static String targetActuatorContainer="mn-cse/mn-name/myactuator/data";

	static String csePoa = cseProtocol+"://"+cseIp+":"+csePort;
	public static final String csePoa2 = cseProtocol2+"://"+cseIp+":"+csePort2;
	private static String appPoa = aeProtocol+"://"+aeIp+":"+aePort;

	public static void main(String[] args) throws SocketException {

		CoAPmanager server;
		server = new CoAPmanager();

		//	server.add(resources);
/*
		server.add(new CoapResource("/")) {
			public void handleGET(CoapExchange exchange) {
				exchange.respond(ResponseCode.CONTENT);
			}
*/
		
		server.add(new MyResource("/"));
			server.addEndpoints();
			server.start();

			JSONArray array = new JSONArray();
			array.put(appPoa);
			JSONObject obj = new JSONObject();
			obj.put("rn", aeName);
			obj.put("api", 12346);
			obj.put("rr", true);
			obj.put("poa",array);
			JSONObject resource = new JSONObject();
			resource.put("m2m:ae", obj);
			RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName, resource.toString(), 2);

			array = new JSONArray();
			array.put(appPoa+"/"+aeName);
			obj = new JSONObject();
			obj.put("nu", array);
			obj.put("rn", subName);
			obj.put("nct", 2);
			resource = new JSONObject();		
			resource.put("m2m:sub", obj);
			RestCoAPClient.post(originator, csePoa+"/~/"+targetSensorContainer, resource.toString(), 23);
		}
	
	
	static class MyResource extends CoapResource {

		public MyResource(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}
		
		 @Override
	        public void handleGET(CoapExchange exchange) {
	            
	            // respond to the request
	            exchange.respond("Hello World!");
	        }
	}

}
