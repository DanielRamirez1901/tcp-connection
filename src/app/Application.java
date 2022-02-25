package app;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Enumeration;

import comm.TCPConnection;

public class Application implements TCPConnection.DayAndHour,TCPConnection.InterfaceInServer,TCPConnection.IpServer,TCPConnection.RTTServer,TCPConnection.SpeedServer{
	
	private TCPConnection connection;
	
	public Application() {
		connection = new TCPConnection();
		connection.setPuerto(6000);
		connection.setDayAndHour(this);
		connection.setInterfaceInServer(this);
		connection.setIpInServer(this);
		connection.setRttServer(this);
		connection.setSpeedSv(this);
	}

	public void init() {
		connection.start();
		
	}

	@Override
	public String timeInServer() {
		Calendar c = Calendar.getInstance();
		String dateHour = c.getTime().toString();
		return dateHour;
	}

	@Override
	public String interfacesServer() {
			String it = "";
		try {
			
			Enumeration<NetworkInterface> interfaces;
			
			interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()) {
				NetworkInterface netN = interfaces.nextElement();
				
				if(netN.isUp()) {
					it += netN.getName();
					it += " - ";
					if(netN.getHardwareAddress()!=null) {
						
						String mac = new BigInteger(1,netN.getHardwareAddress()).toString(16);
						it += mac;
						it += " - ";
						
					}
				}
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;	
	}

	@Override
	public String ipInServer() {
		String msg = "";
		try {
			InetAddress ip = InetAddress.getLocalHost();
			msg = ip.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public String rttServer(long timeFinal, long timeInit) {
		long timeComplete = timeFinal-timeInit;
		String time = String.valueOf(timeComplete);
		return time;
	}

	@Override
	public String speedServer(long timeFinal, long timeInit) {
		long timeComplete = timeFinal-timeInit;
		String time = String.valueOf(timeComplete);
		return time;
	}

	

	
	
	
}
