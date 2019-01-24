package it.unipi.iot;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.json.JSONObject;

public class CoAPactuator extends CoapServer
{
	private static final int COAP_PORT = MyActuator.aePort;

	void addEndpoints()
	{
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			if (((addr instanceof Inet4Address)) || (addr.isLoopbackAddress()))
			{
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
	}

	public CoAPactuator() throws SocketException
	{
		add(new Resource[] { new Monitor() });
	}

	class Monitor extends CoapResource
	{
		public Monitor()
		{
			super("myactuator");

			getAttributes().setTitle("Actuator");
		}

		public void handlePOST(CoapExchange exchange)
		{

			System.out.println("Event Recieved!");
			exchange.respond(ResponseCode.CREATED);
			byte[] content = exchange.getRequestPayload();
			String requestBody = new String(content);

			System.out.println(requestBody);
			JSONObject json = new JSONObject(requestBody);
			if (json.getJSONObject("m2m:sgn").has("m2m:vrq")) {
				System.out.println("Confirm subscription");
			} else {
				JSONObject rep = json.getJSONObject("m2m:sgn").getJSONObject("m2m:nev")
						.getJSONObject("m2m:rep").getJSONObject("m2m:cin");
				int ty = rep.getInt("ty");
				System.out.println("Resource type: "+ty);

				if (ty == 4) {
					String con = rep.getString("con");
					System.out.println("Actuator state = "+con);
					setActuatorValue(Boolean.parseBoolean(con));
				}
			}
		}


		public void setActuatorValue(boolean parseBoolean) {
			MyActuator.actuatorValue = parseBoolean;	
		}
	}

}