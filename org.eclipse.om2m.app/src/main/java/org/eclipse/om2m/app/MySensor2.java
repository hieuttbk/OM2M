package org.eclipse.om2m.app;
 
import org.json.JSONObject;
 
public class MySensor2 {
 
	public static int sensorValue;
 
	private static String originator="admin:admin"; // acc and password
	private static String cseProtocol="http";
	private static String cseIp = "127.0.0.1";
	private static int csePort = 8585;				// ? port nay cua MN
	private static String cseId = "mn-cse-c";
	private static String cseName = "mn-name-c";
 
	private static String aeName = "TEST4";
	private static String cntName = "data";
	private static String csePoa = cseProtocol+"://"+cseIp+":"+csePort; // 1 phan cua URL
 
	public static void main(String[] args) {
 
		JSONObject obj = new JSONObject();
		obj.put("rn", aeName);
		obj.put("api", 12345);
		obj.put("rr", false);
		JSONObject resource = new JSONObject();
		resource.put("m2m:ae", obj);
		RestHttpClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName, resource.toString(), 2);
 
	        obj = new JSONObject();
	        obj.put("rn", cntName);
		resource = new JSONObject();
	        resource.put("m2m:cnt", obj);
		RestHttpClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName, resource.toString(), 3);
 
		while (true){
 
			obj = new JSONObject();
			obj.put("cnf", "application/text");
			obj.put("con", getSensorValue());
			resource = new JSONObject();
			resource.put("m2m:cin", obj);
			RestHttpClient.post(originator, csePoa+"/~/"+cseId+"/"+cseName+"/"+aeName+"/"+cntName, resource.toString(), 4);
 
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 
	public static int getSensorValue(){
		//sensorValue = (int)(Math.random()*500);
		sensorValue = (int)(700);
		System.out.println("Sensor value = "+sensorValue);
		return sensorValue;
	}
}