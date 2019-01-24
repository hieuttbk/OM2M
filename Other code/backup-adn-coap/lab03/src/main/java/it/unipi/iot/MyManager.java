package it.unipi.iot;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
 
public class MyManager {
 
	static String originator="admin:admin";
	private static String cseProtocol="coap";
	private static String cseIp = "127.0.0.1";
	//private static String cseIp = "192.168.1.5";
	private static int csePort = 5683;				// IN port
	private static String cseId = "in-cse";
	private static String cseName = "in-name";
 
	private static String aeName = "mymanager";
	private static String aeProtocol="coap";
	private static String aeIp = "127.0.0.1";
	static int aePort = 5682;				// My manager port
	private static String subName="managersub_2";
	private static String targetSensorContainer="mn-cse/mn-name/mysensor/data";
	static String targetActuatorContainer="mn-cse/mn-name/myactuator/data";
 
	static String csePoa = cseProtocol+"://"+cseIp+":"+csePort;
	private static String appPoa = aeProtocol+"://"+aeIp+":"+aePort;
 
	public static void main(String[] args) throws SocketException {
 
		CoAPmanager server;
		server = new CoAPmanager();
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
	
	}
