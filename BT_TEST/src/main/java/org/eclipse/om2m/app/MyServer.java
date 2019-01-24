package org.eclipse.om2m.app;

import java.io.*;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import org.json.JSONObject;

public class MyServer {
	private static String originator="admin:admin";
	private static String cseProtocol="http";
	private static String cseIp = "127.0.0.1";
	private static int csePort = 8080;				// IN port
	private static String cseId = "in-cse";
	private static String cseName = "in-name";

	private static String aeName = "mymanager";
	private static String aeProtocol="http";
	private static String aeIp = "127.0.0.1";
	private static int aePort = 1500;				// My manager port
	private static String subName="managersub_1";
	private static String targetSensorContainer="in-cse/in-name/mydevice1/luminosity";
	private static String targetActuatorContainer="in-cse/in-name/mydevice1/led";


	private static String csePoa = cseProtocol+"://"+cseIp+":"+csePort;
	private static String appPoa = aeProtocol+"://"+aeIp+":"+aePort;





	String actuatorState;
	//start server
	//Create a UUID for SPP
	StreamConnection mConnection;
	OutputStream outStream;
	InputStream is;

	public String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public MyServer(StreamConnection connection) throws IOException{
		mConnection = connection;
		outStream = mConnection.openOutputStream();
		is = mConnection.openInputStream();
	}



	void readData(){
		read mRead = new read();
		mRead.start();
	}

	class read extends Thread {

		public void run(){
			StringBuffer buf = new StringBuffer();
			byte[] data=new byte[1024];
			int bytesRead;

			try {

				while(true){
					while ((bytesRead=is.read(data))!=-1) {
						buf.append((char)bytesRead);		
						String s= new String(data,0,bytesRead);
						//	System.out.println("Data = " + s);
						//System.out.println("Buffer= " + buf.toString());		
						print_in(s);
						setData(s);
						xuly(s);
						// viet ham xuly(s) truyen vao s roi tra ve day mot gia tri nao do de
						// xong roi write(xuly(s))
						//write(s);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void write(String str) throws IOException{
		System.out.println("Gui len cai nay: " + str);
		outStream.write(str.getBytes());
		outStream.flush();
	}


	private void print_in(String string) throws IOException{
		System.out.println(string);
		//	write("Da nhan duoc: " + string);
		//	return string;
	}

	void xuly(String s){

		System.out.println("NHAN TU PHONE: " + s);
		/*
		if (s=="ON"){
			actuatorState="ON";
		}
		else{
			actuatorState="OFF";

		}
		 */
		actuatorState=s;
		JSONObject obj = new JSONObject();
		obj = new JSONObject();
		obj.put("cnf", "application/text");
		obj.put("con", actuatorState);
		JSONObject resource = new JSONObject();
		resource.put("m2m:cin", obj);

		RestHttpClient.post(originator, csePoa+"/~/"+targetActuatorContainer, resource.toString(), 4);

	}





}

