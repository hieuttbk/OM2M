package it.unipi.iot;
 
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.json.JSONObject;

public class MySensor {
 
	public static int sensorValue;
	private static String originator="admin:admin"; // acc and password
	private static String cseProtocol="coap";
	private static String cseIp = "192.168.1.109";
	private static int csePort = 5683;				// ? port nay cua MN
	private static String cseId = "mn-cse";
	private static String cseName = "mn-name";
 
	private static String aeName = "mysensor";
	private static String cntName = "data";
	private static String csePoa = cseProtocol+"://"+cseIp+":"+csePort; // 1 phan cua URL
 
	public static void main(String[] args) throws UnknownHostException {
		
		//InetAddress IP=InetAddress.getLoInetAddress IP=InetAddress.getLocalHost();
		InetAddress IP=InetAddress.getLocalHost();
		System.out.println(IP.getHostAddress() + IP.getAddress());

		System.out.println(Inet4Address.getLocalHost().getHostAddress());

 
		JSONObject obj = new JSONObject();
		obj.put("rn", aeName);
		obj.put("api", 12345);
		obj.put("rr", false);
		JSONObject resource = new JSONObject();
		resource.put("m2m:ae", obj);
		RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName, resource.toString(), 2);
 
	        obj = new JSONObject();
	        obj.put("rn", cntName);
		resource = new JSONObject();
	        resource.put("m2m:cnt", obj);
		RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName, resource.toString(), 3);
 
		while (true){
 
			obj = new JSONObject();
			obj.put("cnf", "application/text");
			obj.put("con", getSensor());
			resource = new JSONObject();
			resource.put("m2m:cin", obj);
			RestCoAPClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName+"/"+cntName, resource.toString(), 4);
 
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 
static int getSensor(){
		
		String uri_server="coap://[aaaa::c30c:0:0:9e]:5683/sensors/battery"; // Nhap uri cua server khoi tao
		CoapClient client = new CoapClient(uri_server);
		
		Request req = new Request(Code.GET);		
		req.getOptions().setAccept(50);
		
		CoapResponse resp = client.advanced(req);
		System.out.println(resp.getResponseText());
		
		JSONObject json = new JSONObject(resp.getResponseText());
		
		int con= json.getInt("battery") ;
		
		System.out.println("Xem co gi khong" + json.toString() + con);
		return con;
	}
}