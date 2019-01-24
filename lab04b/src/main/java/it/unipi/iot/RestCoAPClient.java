package it.unipi.iot;
 

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.Request;
import org.json.JSONObject;
 
public class RestCoAPClient {
 
	public static CoapResponse post(String originator, String uri, String body, int ty) {
		//orginator = "admin:admin"
		
		System.out.println("\nHTTP POST\n " + "\t URI: " + uri + "\n" + "\t ty: " + ty + "\n" +body);
		
		CoapClient client = new CoapClient(uri);
		
		Request req = Request.newPost();
		req.getOptions().addOption(new Option(267, ty));
		req.getOptions().addOption(new Option(256, originator));
		req.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_JSON);
		req.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
		
		req.setPayload(body);
		
		CoapResponse response = client.advanced(req);
					String responseBody = new String(response.getPayload());
					System.out.println("Response Status Code: " + response.getCode());
					System.out.println(responseBody);
		return response;	
	}
}