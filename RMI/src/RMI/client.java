package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class client {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		Hello hello = (Hello) Naming.lookup("rmi://localhost:5000/hello");
	System.out.println(hello.hello("Chuong trinh RMI"));
	
	
	
	}

}
