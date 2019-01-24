package it.unipi.iot;

import org.eclipse.californium.core.CoapServer;

public class Ex1 extends CoapServer {

	public static void main(String[] args) {
		Ex1 server = new Ex1();
		server.add(new CoAPResourceExample("hello"));
		server.add(new QueryResource("query"));// coap://localhost:5683/query?query=09
		server.start();
	}
}
