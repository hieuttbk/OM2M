package it.unipi.iot;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.json.JSONObject;

public class Ex2 {
	public static void main(String[] args) {
		/*CoapClient client = new CoapClient("coap://[aaaa::c30c:0:0:2]:5683/sensors/battery");
		
		Request req = new Request(Code.GET);
	//	req.getOptions().setUriQuery("query=2");
		
		req.getOptions().setAccept(50);
		
		System.out.println(req.toString());

		CoapResponse resp = client.advanced(req);
		
		
		System.out.println(resp.getOptions().getContentFormat());
		
		System.out.println(resp.getCode());

		System.out.println(resp.getResponseText());
		*/
		String uri_server="coap://[aaaa::c30c:0:0:2]:5683/sensors/battery"; // Nhap uri cua server khoi tao
		CoapClient client = new CoapClient(uri_server);
		
		Request req = new Request(Code.GET);		
		req.getOptions().setAccept(50);
		
		CoapResponse resp = client.advanced(req);
		System.out.println(resp.getResponseText());
		
		JSONObject json = new JSONObject(resp.getResponseText());
		
		int con= json.getInt("battery") ;
		
		System.out.println("Xem co gi khong" + json.toString() + con);
	}
	
}
