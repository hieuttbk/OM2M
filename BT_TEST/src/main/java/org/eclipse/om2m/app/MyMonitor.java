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

public class MyMonitor {

	private static String originator = "admin:admin";
	private static String cseProtocol = "http";
	private static String cseIp = "127.0.0.1";
	private static int csePort = 8080;
	private static String cseId = "in-cse";
	private static String cseName = "in-name";

	private static String aeMonitorName = "mymonitor";
	private static String aeProtocol = "http";
	private static String aeIp = "127.0.0.1";
	private static int aePort = 1600;
	private static String subName = "monitorsub";
	private static String targetCse = "mn-cse/mn-name";

	private static String csePoa = cseProtocol + "://" + cseIp + ":" + csePort;
	private static String appPoa = aeProtocol + "://" + aeIp + ":" + aePort;

	private static JSONObject ae;
	private static JSONObject sub;

	public static void main(String[] args) {

		// Create sever port 1600 to receive from other elements
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(aePort), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();

		// Create aeMonitor in in-name
		JSONArray array = new JSONArray();
		array.put(appPoa);
		JSONObject obj = new JSONObject();
		obj.put("rn", aeMonitorName);
		obj.put("api", 12346);
		obj.put("rr", true);
		obj.put("poa", array);
		ae = new JSONObject();
		ae.put("m2m:ae", obj);
		RestHttpClient.post(originator, csePoa + "/~/" + cseId + "/" + cseName,
				ae.toString(), 2);

		// Sub itself
		array = new JSONArray();
		array.put("/" + cseId + "/" + cseName + "/" + aeMonitorName);
		obj = new JSONObject();
		obj.put("nu", array);
		obj.put("rn", subName);
		obj.put("nct", 2);
		sub = new JSONObject();
		sub.put("m2m:sub", obj);
		RestHttpClient.post(originator, csePoa + "/~/" + targetCse,
				sub.toString(), 23);

		System.out.println("[INFO] Discover all containers in " + csePoa);

		// Sent GET to discover all container (ty=3)
		HttpResponse httpResponse = RestHttpClient.get(originator, csePoa
				+ "/~/" + targetCse + "?fu=1&ty=3");

		JSONObject result = new JSONObject(httpResponse.getBody());

		// Receive all uri list of container --> Slip and sent POST to sub all
		// container
		JSONArray uril_arr = result.getJSONArray("m2m:uril");
		for (Object test : uril_arr) {
			RestHttpClient.post(originator, csePoa + "/~" + test,
					sub.toString(), 23);
		}
	}

	// -----------------------DOAN TU DAY LEN TREN LA OK
	// ROI-----------------------

	static class MyHandler implements HttpHandler {

		public void handle(HttpExchange httpExchange) {
			System.out.println("\nEvent Recieved!");
			System.out.println("HTTP EXCHANGE ");

			// initialization String requestBody

			try {
				InputStream in = httpExchange.getRequestBody();

				String requestBody = "";
				int i;
				char c;
				while ((i = in.read()) != -1) {
					c = (char) i;
					requestBody = (String) (requestBody + c);
				}

				System.out
						.println("DAY LA TOAN BO BAN TIN NHAN DUOC TRUOC KHI XU LY"
								+ requestBody);

				JSONObject json = new JSONObject(requestBody);
				// Has vrq in sgn ==> done sub
				
				if (json.getJSONObject("m2m:sgn").has("m2m:vrq")) {
					System.out.println("Confirm subscription");
				}
//------------------------------ TAM THOI DOAN TREN OK ---------------
				// case cho viec co mot thang moi duoc tim ra.
				else if (json.getJSONObject("m2m:sgn").getJSONObject("m2m:nev")
						.getJSONObject("m2m:rep").has("m2m:ae")) {

					// read in to ae
					JSONObject rep = json.getJSONObject("m2m:sgn")
							.getJSONObject("m2m:nev").getJSONObject("m2m:rep")
							.getJSONObject("m2m:ae");

					int ty = rep.getInt("ty");
					System.out.println("DONG NAY CO CHAY DC KHONG ? ");

					System.out.println("Resource type: " + ty);

					if (ty == 2) {
						// ty = 2 => print info new AE
						String aeName = rep.getString("rn");
						System.out.println("[INFO] New AE has been registred: "
								+ aeName + "\n");
						System.out.println("[INFO] Wait 3 seconds\n");
						Thread.sleep(3000);
						System.out.println("[INFO] Monitor AE " + aeName
								+ " containers");

						HttpResponse httpResponse = RestHttpClient.get(
								originator, csePoa + "/~/" + targetCse + "/"
										+ aeName + "?rcn=5");
						// rcn = 5 means ?? - result content = 5 ==> find
						// container in ae
						// attributes and child resource references

						JSONObject ae = new JSONObject(httpResponse.getBody());
						if (!ae.getJSONObject("m2m:ae").isNull("ch")) {
							JSONArray aeChild = ae.getJSONObject("m2m:ae")
									.getJSONArray("ch");

							System.out.println("[INFO] AE " + aeName + " has "
									+ aeChild.length() + " child:");

							for (int j = 0; j < aeChild.length(); j++) {
								System.out.println("TESTINGGGGGGGGG " + aeChild.getJSONObject(j));
								
								if (aeChild.getJSONObject(j).getInt("typ") == 3) {
									String cntName = aeChild.getJSONObject(j)
											.getString("nm");
									System.out.println("[INFO] Container: "
											+ cntName + "\n");
									// Sub into child of ae
									RestHttpClient
											.post(originator, csePoa + "/~/"
													+ targetCse + "/" + aeName
													+ "/" + cntName,
													sub.toString(), 23);
									System.out
											.println("Co chay vao day khong va cai tren la gi");
									System.out.println(originator + csePoa
											+ "/~/" + targetCse + "/" + aeName
											+ "/" + cntName + sub.toString());
								}
							}
						}
					} 
					
		/*			
					else if (ty == 4) {
						String ciName = rep.getString("rn");
						String content = rep.getString("con");

						System.out.println("[INFO] New Content Instance "
								+ ciName + " has been created");
						System.out.println("[INFO] Content: " + content);

					}*/
				}
				
				else{
					JSONObject rep = json.getJSONObject("m2m:sgn")
							.getJSONObject("m2m:nev").getJSONObject("m2m:rep")
							.getJSONObject("m2m:cin");

					int ty = rep.getInt("ty");
					if (ty == 4) {
						String ciName = rep.getString("rn");
						String content = rep.getString("con");

						System.out.println("[INFO] New Content Instance "
								+ ciName + " has been created");
						System.out.println("[INFO] Content: " + content);
					
					
				}}

				String responseBudy = "";
				byte[] out = responseBudy.getBytes("UTF-8");
				httpExchange.sendResponseHeaders(200, out.length);
				OutputStream os = httpExchange.getResponseBody();
				os.write(out);
				os.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
