package btipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.resource.ContentInstance;


public class MyServer {
	
	StreamConnection mConnection;
	OutputStream outStream;
	InputStream is;
	
	Integer data=0;

	public Integer getData() {
		return data;
	}

	public void setData(String data) {
	//	System.out.println("\n=====data read from phone=== "+ data + "\nChuyen sang int" + Integer.parseInt(data) );
		this.data = Integer.parseInt(data);
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

				
					while ((bytesRead=is.read(data))!=-1) {
						buf.append((char)bytesRead);		
						String s= new String(data,0,bytesRead);
						setData(s);
						xuly(s);
						System.out.println("\n\n=======Delay============ 2 ============");

					}
					System.out.println("\n\n=======Off Connection cmnr============ 3 ============");

			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void write(String con) throws IOException {
		System.out.println("Gui len cai nay: " + con);
		outStream.write(con.getBytes());
		outStream.flush();		
	}
	
	void xuly(String s){
		
		System.out.println("\n\n========================="
				+ "MOI KHI NHAN DUOC BAN TIN TU PHONE"
				+ "=========================\n\n");
		
		int btValue = Integer.parseInt(s);
		String content = ObixUtil.getbtRep(btValue);
		String targetId = "/" + Monitor.CSE_ID + "/" + Monitor.CSE_NAME + "/" + Monitor.btId + "/" + Monitor.DATA;	
		ContentInstance cin = new ContentInstance();
		cin.setContent(content);
		cin.setContentInfo(MimeMediaType.OBIX);	
		RequestSender.createContentInstance(targetId, cin);
		
		String targetId_2 = "/"+ Monitor.CSE_ID + "/" + Monitor.CSE_NAME + "/" + "mydevice1" + "/" + "led";
		RequestSender.createContentInstance(targetId_2, cin);

	}
	
}
