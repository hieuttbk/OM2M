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

public class CoAPmanager extends CoapServer
{
	private static final int COAP_PORT = MyManager.aePort;

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

	public CoAPmanager() throws SocketException
	{
		add(new Resource[] { new Monitor() });
	}

	class Monitor extends CoapResource
	{
		public Monitor()
		{
			super("mymanager");

			getAttributes().setTitle("Manager");
		}

		public void handlePOST(CoapExchange exchange)
		{

			System.out.println("Event Recieved!");
			exchange.respond(ResponseCode.CREATED);
			byte[] content = exchange.getRequestPayload();
			String requestBody = new String(content);

			System.out.println("HTTP EXCHANGE\nBAN_TIN_DOC_DUOC:\n" + requestBody);

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
					int sensorValue = Integer.parseInt(con);
					System.out.print("OBSERVATION: Sensor Value "+sensorValue);

					boolean actuatorState;
					if(sensorValue<250){
						System.out.println(" -> LOW");
						actuatorState=true;

					}else{
						System.out.println(" -> HIGH");
						actuatorState=false;
					}
					System.out.println("ACTION: switch actuator state to "+actuatorState+"\n");

					JSONObject obj = new JSONObject();
					obj = new JSONObject();
					obj.put("cnf", "application/text");
					obj.put("con", actuatorState);
					JSONObject resource = new JSONObject();
					resource.put("m2m:cin", obj);
					RestHttpClient.post(MyManager.originator, MyManager.csePoa2+"/~/"+ MyManager.targetActuatorContainer, resource.toString(), 4);
				}
			}

		}
	}
}