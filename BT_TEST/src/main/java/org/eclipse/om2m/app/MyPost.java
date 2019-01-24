package org.eclipse.om2m.app;

import java.io.*;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class MyPost {
	//start server
	//Create a UUID for SPP
	StreamConnection mConnection;
	OutputStream outStream;
	public MyPost(StreamConnection connection) throws IOException{
	mConnection = connection;
	outStream = mConnection.openOutputStream();
	}
	
	//OutputStream outStream ;
	
	//OutputStream outStream = mConnection.openOutputStream();


	void write(String str) throws IOException{
		System.out.println("Vao ham wirte");

		
		outStream.write(str.getBytes());
	//	outStream.write(str.getBytes());
		//outStream.write("123".getBytes());

		outStream.flush();
	//	outStream.close();
	}

	

	

}

