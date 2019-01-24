package bluetooth_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.*;
import javax.microedition.io.*;

/**
 * Class that implements an SPP Server which accepts single line of
 * message from an SPP client and sends a single line of response to the client.
 */
public class SimpleSPPServer extends Thread {

	//start server
	public void run() {

		//}
		//private void startServer() throws IOException{

		//Create a UUID for SPP
		UUID uuid = new UUID("1101", true);
		//Create the servicve url
		String connectionString = "btspp://localhost:" + uuid +";name=Sample SPP Server";
		System.out.println(connectionString + uuid.toString());

		//open server url
		StreamConnectionNotifier streamConnNotifier;
		try {
			streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );
			//Wait for client connection
			System.out.println("\nServer Started. Waiting for clients to connect...");
			StreamConnection connection=streamConnNotifier.acceptAndOpen();

			RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
			System.out.println("Remote device address: "+dev.getBluetoothAddress());
			System.out.println("Remote device name: "+dev.getFriendlyName(true));


			Thread Read = new mRead(connection);
			Read.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//send response to spp client
	//	OutputStream outStream=connection.openOutputStream();
	// Xem sao 2 dong nay khong chay duoc 
	/*    
	      BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(outStream));
	      bWriter.write("Response String from SPP Server\r\n");
	 */
	//  PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));

	/*
			PrintWriter pWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream)));

			pWriter.write("Response String from SPP Server\r\n");
			pWriter.flush();
			pWriter.close();

			//streamConnNotifier.close();

			// connection.close();

	 */

	/*		
		//read string from spp client
		InputStream inStream=connection.openInputStream();
		BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));

		String lineRead=bReader.readLine();
		System.out.println(lineRead);
	 */


	/*     InputStream is=connection.openInputStream();
      StringBuffer buf = new StringBuffer();
      int data;
      while ((data=is.read())!=-1) {
    	  System.out.println("Co gi trong nay khong ?");
    	  buf.append((char)data);
      }

      System.out.println(buf.toString());
	 */		 




	//	}

	public class mWrite extends Thread {
		private StreamConnection mConnection;
		private String string;

		public mWrite(StreamConnection connection, String string)
		{
			mConnection = connection;
			this.string = string;
		}

		public void run(){
			OutputStream outStream;
			try {
				outStream = mConnection.openOutputStream();
				
				outStream.write(string.getBytes());
	            
				outStream.flush();
				//	PrintWriter pWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream)));
			//	pWriter.write(string);

				//pWriter.write("Response String from SPP Server\r\n");
			//	pWriter.flush();
			//	pWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Xem sao 2 dong nay khong chay duoc 
			/*    
	      BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(outStream));
	      bWriter.write("Response String from SPP Server\r\n");
			 */
			//  PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));



			//streamConnNotifier.close();

			// connection.close();
		}
	}


	public class mRead extends Thread {

		private StreamConnection mConnection;


		public mRead(StreamConnection connection)
		{
			mConnection = connection;
		}
		public void run() {
			InputStream is;
			try {
				is = mConnection.openInputStream();
				//	iis = mConnection.

				StringBuffer buf = new StringBuffer();
				byte[] data=new byte[1024];
				int bytesRead;

				while ((bytesRead=is.read(data))!=-1) {
					buf.append((char)bytesRead);		
					String s= new String(data,0,bytesRead);
					System.out.println("Data = " + s);
					System.out.println("Buffer= " + buf.toString());
					
					
					Thread mwrite = new mWrite(mConnection,"Tao nhan duoc" + s);
					mwrite.start();
					
				}

				System.out.println(buf.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}


	public static void main(String[] args) throws IOException {

		//display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("Address: "+localDevice.getBluetoothAddress());
		System.out.println("Name: "+localDevice.getFriendlyName());
		//while(true){
		Thread sampleSPPServer=new SimpleSPPServer();
		sampleSPPServer.start();
		//	}


	}
}