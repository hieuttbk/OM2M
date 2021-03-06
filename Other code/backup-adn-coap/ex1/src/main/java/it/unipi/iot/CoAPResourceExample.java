package it.unipi.iot;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class CoAPResourceExample extends CoapResource {
	public CoAPResourceExample(String name) {
		super(name);
		setObservable(true);
	}
	public void handleGET(CoapExchange exchange) {
		exchange.respond("hello world");
	}
	public void handlePOST(CoapExchange exchange) {
		/* your stuff */
		exchange.respond(ResponseCode.CREATED);
	}
}