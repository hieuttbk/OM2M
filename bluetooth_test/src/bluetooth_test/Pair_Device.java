package bluetooth_test;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

public class Pair_Device {

	public static void main(String args[]) throws IOException
	{
		
		LocalDevice device = LocalDevice.getLocalDevice();
		
		RemoteDevice[] remotedevice = device.getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);
		
		for(RemoteDevice d : remotedevice)
		{
			System.out.println("Device Name : "+d.getFriendlyName(false));
			System.out.println("Bluetooth Address : "+d.getBluetoothAddress()+"\n");
		}
		
	}
	
}
