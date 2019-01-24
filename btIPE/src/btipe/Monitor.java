package btipe;

import java.io.IOException;
import java.math.BigInteger;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.commons.resource.Subscription;
import org.eclipse.om2m.core.service.CseService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class Monitor {

	static MyServer mServer;



	static int check=0;

	static CseService CSE;
	static String CSE_NAME=Constants.CSE_NAME;
	static String CSE_ID=Constants.CSE_ID;
	static String ipeId="BLUETOOTH_IPE";
	static String btId="BLUETOOTH_DEVICE";
	static String DATA="DATA";

	static int btValue=0;
	private BtLisener btListener;


	public Monitor(CseService cse) {
		CSE=cse;
	}

	public void start() throws IOException {
		createBtResource();

		UUID uuid=new UUID("1101",true);
		String url = "btspp://localhost:" + uuid +";name=Sample SPP Server" ;

		StreamConnectionNotifier streamConnNotifier;

		streamConnNotifier = (StreamConnectionNotifier)Connector.open( url );
		System.out.println("\nServer Started. Waiting for clients to connect...");
		StreamConnection connection=streamConnNotifier.acceptAndOpen();
		RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
		System.out.println("Remote device address: "+dev.getBluetoothAddress());
		System.out.println("Remote device name: "+dev.getFriendlyName(true));

		check=1;
		mServer=new MyServer(connection);			
		mServer.readData();
		System.out.println("\n\n=======Delay============ 1 ============");

		listentoBt();
	}

	private void createBtResource() {
		String targetId, content;
		targetId = "/" + CSE_ID + "/" + CSE_NAME;
		AE ae=new AE();
		ae.setRequestReachability(true); // rr=true
		ae.setAppID(ipeId);
		ae.getPointOfAccess().add(ipeId);
		ae.setName(btId);

		ResponsePrimitive respone=RequestSender.createAE(ae);

		if(respone.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
			targetId = "/" + CSE_ID + "/" + CSE_NAME + "/" + btId; // send to .../MY_SENSOR
			Container cnt = new Container();
			cnt.setMaxNrOfInstances(BigInteger.valueOf(10));
			cnt.setName(DATA);
			RequestSender.createrContainer(targetId,cnt);

			content = ObixUtil.getbtRep(btValue);
			targetId = "/" + CSE_ID + "/" + CSE_NAME + "/" + btId + "/" + DATA;
			ContentInstance cin = new ContentInstance();
			cin.setContent(content);
			cin.setContentInfo(MimeMediaType.OBIX);
			RequestSender.createContentInstance(targetId, cin);

		}

		targetId =  "/" + CSE_ID + "/" + CSE_NAME + "/" + "mydevice1" + "/" + "luminosity";

		String nu = "/" + CSE_ID + "/" + CSE_NAME + "/" + btId;
		Subscription sub = new Subscription();
		sub.setName("debug_sub");
		sub.setNotificationContentType(BigInteger.valueOf(2));
		sub.setNotificationURI(nu);
		//	sub.setReturnContentType(MimeMediaType.JSON);
		ResponsePrimitive response2 = RequestSender.createSub(targetId,sub);

	}

	private void listentoBt() {
		btListener = new BtLisener();
		btListener.start();
	}
	public void stop() {
		if(btListener !=null && btListener.isAlive()){
			btListener.stopThread();
		}
	}

	private static class BtLisener extends Thread {
		private boolean running = true;

		@Override
		public void run() {		


		}

		public void stopThread() {
			running=false;
		}
	}

	public static void writetophone(Object content) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		System.out.println("=================check point 0=================");

		JSONObject json = (JSONObject) parser.parse(content.toString());
		System.out.println("=================check point 1=================");
		JSONObject json1=(JSONObject) json.get("m2m:sgn");
		JSONObject json2=(JSONObject) json1.get("m2m:nev");
		JSONObject json3=(JSONObject) json2.get("m2m:rep");
		JSONObject json4=(JSONObject) json3.get("m2m:cin");
		System.out.println("=================check point 2=================" + json4.toString());
		
		long ty = (long) json4.get("ty");

		System.out.println("\n\n==JSON TEST =" + ty);
		if (ty == 4) { 
			System.out.println("=================check point 3=================");

			String con = (String) json4.get("con");
			mServer.write(con);		
		}
		
	}



}
