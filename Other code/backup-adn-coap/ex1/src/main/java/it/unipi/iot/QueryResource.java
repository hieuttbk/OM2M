package it.unipi.iot;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Response;


public class QueryResource extends CoapResource {
	public QueryResource(String name) {
		super(name);
		setObservable(true);
	}
	public void handleGET(CoapExchange exchange) {
		Integer num = 0;
		try {
			if(exchange.getRequestOptions().getURIQueryCount() != 1) {
				exchange.respond(ResponseCode.BAD_REQUEST);
				System.out.println("Khong thay URI");
				return;
			}
			String q = exchange.getRequestOptions().getUriQuery().get(0);
			q = q.split("=")[1];
			num = Integer.parseInt(q);
			num = num*num;
			exchange.respond(num.toString());	// thang nay luc dau dat sai vi tri. phai dat trong handdleGET moi co exchange
			
		}catch (NumberFormatException e) {
			exchange.respond(ResponseCode.BAD_REQUEST);
			System.out.println("Format");

			return;
		}catch (ArrayIndexOutOfBoundsException e1) {
			exchange.respond(ResponseCode.BAD_REQUEST);
			System.out.println("KHONG BIET LA GI");
			return;
		}
	//	Response response = new Response(ResponseCode.CONTENT);
		//response.setPayload(num.toString());
		//exchange.respond(ResponseCode.VALID);
		//exchange.respond(response);
	}
}
