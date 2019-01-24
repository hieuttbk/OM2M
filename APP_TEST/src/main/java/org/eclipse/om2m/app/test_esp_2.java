//09012019 - Test mqtt
package org.eclipse.om2m.app;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
 
public class test_esp_2 {
 
	private static String originator="admin:admin";
	private static String cseProtocol="http";
	private static String cseIp = "127.0.0.1";
	private static int csePort = 8080;				// IN port
	private static String cseId = "";
	private static String cseName = "server";
 // /~/in-cse/in-name/mydevice1/luminosity
	private static String aeName = "mymanager_mqtt";
	private static String aeProtocol="http";
	private static String aeIp = "127.0.0.1";
	private static int aePort = 3001;				// My manager port
	private static String subName="managersub_mqtt";
	private static String targetSensorContainer="/server/myd/luminosity";
	private static String targetActuatorContainer="/server/mydevice1/led";
 
	private static String csePoa = cseProtocol+"://"+cseIp+":"+csePort;
	private static String appPoa = aeProtocol+"://"+aeIp+":"+aePort;
 
	public static void main(String[] args) {
 
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(aePort), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
 
		JSONArray array = new JSONArray();
		array.put(appPoa);
		JSONObject obj = new JSONObject();
		obj.put("rn", aeName);
		obj.put("api", 12346);
		obj.put("rr", true);
		obj.put("poa",array);
		JSONObject resource = new JSONObject();
		resource.put("m2m:ae", obj);
		RestHttpClient.post(originator, csePoa+"/~/"+cseId+""+cseName, resource.toString(), 2);
 
		array = new JSONArray();
		array.put("/"+cseId+""+cseName+"/"+aeName);
		obj = new JSONObject();
		obj.put("nu", originator);
		obj.put("rn", subName);
		obj.put("nct", 2);
		resource = new JSONObject();		
		resource.put("m2m:sub", obj);
		RestHttpClient.post(originator, csePoa+targetSensorContainer, resource.toString(), 23);
	}
 
	static class MyHandler implements HttpHandler {
 
		public void handle(HttpExchange httpExchange){  
			System.out.println("\n" + "------------" + "Event Recieved!====CO BAN TIN TRAO DOI" + "\n");
 
			try{
				InputStream in = httpExchange.getRequestBody();
 
				String requestBody = "";
				int i;char c;
				while ((i = in.read()) != -1) {
					c = (char) i;
					requestBody = (String) (requestBody+c);
				}
 
				System.out.println("HTTP EXCHANGE\nBAN_TIN_DOC_DUOC:\n" + requestBody);
 
				JSONObject json = new JSONObject(requestBody);
				if (json.getJSONObject("m2m:sgn").has("m2m:vrq")) {
					System.out.println("Confirm subscription");
				} else {
					JSONObject rep = json.getJSONObject("m2m:sgn").getJSONObject("nev")
							.getJSONObject("rep").getJSONObject("m2m:cin");
					int ty = rep.getInt("ty");
					System.out.println("Resource type: "+ty);
 
					if (ty == 4) {
						String con = rep.getString("con");
						int sensorValue = Integer.parseInt(con);
						System.out.print("OBSERVATION: Sensor Value "+sensorValue);
 
						String actuatorState="ON";
						if(sensorValue<10){
							System.out.println(" -> LOW");
							actuatorState="ON";
 
						}else{
							System.out.println(" -> HIGH");
							actuatorState="OFF";
						}
						System.out.println("ACTION: switch actuator state to "+actuatorState+"\n");
 
						JSONObject obj = new JSONObject();
						obj = new JSONObject();
					//	obj.put("cnf", "application/text");
						obj.put("con", actuatorState);
						JSONObject resource = new JSONObject();
						resource.put("m2m:cin", obj);
						RestHttpClient.post(originator, csePoa+targetActuatorContainer, resource.toString(), 4);
					}
				}	
 
				String responseBudy ="";
				byte[] out = responseBudy.getBytes("UTF-8");
				httpExchange.sendResponseHeaders(200, out.length);
				OutputStream os = httpExchange.getResponseBody();
				os.write(out);
				os.close();
 
			} catch(Exception e){
				e.printStackTrace();
			}		
		}
	}
}