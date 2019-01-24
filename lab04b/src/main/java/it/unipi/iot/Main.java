package it.unipi.iot;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.Request;
import org.json.JSONObject;

public class Main {
	private String discoverContainer(String cse){
		CoapClient client = new CoapClient(cse);
		Request req = Request.newGet();
		req.getOptions().addOption(new Option(256, "admin:admin"));
		req.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_JSON);
		req.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
		CoapResponse responseBody = client.advanced(req);
		String response = new String(responseBody.getPayload());
		JSONObject content = new JSONObject(response); 
		String path = (String) content.getJSONArray("m2m:uril").get(0);
		return path;
	}
	private void createSubscription(String cse, String notificationUrl){
		CoapClient client = new CoapClient(cse);
		Request req = Request.newPost();
		req.getOptions().addOption(new Option(267, 23));
		req.getOptions().addOption(new Option(256, "admin:admin"));
		req.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_JSON);
		req.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
		JSONObject content = new JSONObject();
		content.put("rn", "Monitor");
		content.put("nu", notificationUrl);
		content.put("nct", 2);
		JSONObject root = new JSONObject();
		root.put("m2m:sub", content);
		String body = root.toString();
		req.setPayload(body);
		CoapResponse responseBody = client.advanced(req);
		String response = new String(responseBody.getPayload());
		System.out.println(response);
				
	}
	
	
	public static void main(String args[]) {
		Main main = new Main();
		MonitorThread thread = new MonitorThread();
		thread.start();
		String uri = main.discoverContainer("coap://127.0.0.1:5683/~/mn-cse?fu=1&rty=3");
		System.out.println(uri);
		main.createSubscription("coap://127.0.0.1:5683/~" + uri, "coap://127.0.0.1:5685/monitor");
	}
}
