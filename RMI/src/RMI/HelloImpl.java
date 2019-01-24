package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {

	protected HelloImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String hello(String name) throws RemoteException {
		System.out.println("Ten la " + name);
		
		return "xin chao " + name;
	}
public static void main(String[] args) throws RemoteException, MalformedURLException {
	Naming.rebind("rmi://localhost:5000/hello", new HelloImpl());
	System.out.println("Tao duoc server RMI");
}
}
