package it.unipi.iot;

import java.net.SocketException;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONObject;


public class MyActuator {

	public static boolean actuatorValue;

	private static String originator="admin:admin";
	private static String cseProtocol="coap";
	private static String cseIp = "127.0.0.1";
	private static int csePort = 5684;
	private static String cseId = "mn-cse";
	private static String cseName = "mn-name";

	private static String aeName = "myactuator";
	private static String cntName = "data";

	private static String aeProtocol="coap";
	private static String aeIp = "127.0.0.1";
	static int aePort = 5686;	
	private static String subName="actuatorsub";

	private static String csePoa = cseProtocol+"://"+cseIp+":"+csePort;
	private static String appPoa = aeProtocol+"://"+aeIp+":"+aePort;

	public static void main(String[] args) throws SocketException {

		CoAPactuator server;
		server = new CoAPactuator();
		server.addEndpoints();
		server.start();

		JSONArray array = new JSONArray();
		array.put(appPoa);
		JSONObject obj = new JSONObject();
		obj.put("rn", aeName);
		obj.put("api", 12345);
		obj.put("rr", true);
		obj.put("poa",array);
		JSONObject resource = new JSONObject();
		resource.put("m2m:ae", obj);
		RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName, resource.toString(), 2);

		obj = new JSONObject();
		obj.put("rn", cntName);
		resource = new JSONObject();
		resource.put("m2m:cnt", obj);
		RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName, resource.toString(), 3);

		array = new JSONArray();
				//coap://127.0.0.1:5685/monitor
		array.put(appPoa+"/"+aeName);
		obj = new JSONObject();
		obj.put("nu", array);
		obj.put("rn", subName);
		obj.put("nct", 2);
		resource = new JSONObject();		
		resource.put("m2m:sub", obj);
		RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName+"/"+cntName, resource.toString(), 23);
	}
}