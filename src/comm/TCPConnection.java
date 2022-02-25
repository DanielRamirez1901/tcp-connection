package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import client.Client;

public class TCPConnection extends Thread{
	
	private ServerSocket server;
	private Socket socket;
	private int puerto;
	public Client client;
	
	public DayAndHour timeServer;
	public InterfaceInServer interServer;
	public IpServer ipInServer;
	public RTTServer rttServer;
	public SpeedServer speedSv;
	
	
	


	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	

	@Override
	public void run() {
		try {
			boolean stop = false;
			
			while(!stop) {
				
			server = new ServerSocket(puerto);
			System.out.println("Servidor iniciado correctamente");
			System.out.println("Esperando un cliente...");
			socket = server.accept();
			System.out.println("Cliente conectado");
			
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			BufferedReader breader = new BufferedReader(new InputStreamReader(is));
			BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(os));
			
			
			String msg = breader.readLine();
			if(msg.contains("whatsTimeIsIt")) {
				
				String msgToSend = timeServer.timeInServer();
				bwriter.write(msgToSend +"\n");
				bwriter.flush();
				
			}else if(msg.contains("interfaces")) {
				
				String msgToSend = interServer.interfacesServer();
				bwriter.write(msgToSend +"\n");
				bwriter.flush();
				
			}else if(msg.contains("off")) {
				
				stop = true;//Terminar la ejecucion del programa
				
			}else if(msg.contains("remoteIpconfig")) {
				
				String msgToSend = ipInServer.ipInServer();
				bwriter.write(msgToSend +"\n");
				bwriter.flush();
				
			}else if(msg.contains("RTT")) {
				
				//Genero una nueva conexion para asi por un socket enviar el msg y el tiempo de ejecucion
				
				ServerSocket serverRTT = new ServerSocket(6500);
				Socket socketRTT = serverRTT.accept();
				InputStream isRTT = socketRTT.getInputStream();
				OutputStream osRTT = socketRTT.getOutputStream();
				BufferedReader breaderRTT = new BufferedReader(new InputStreamReader(isRTT));
				BufferedWriter bwriterRTT = new BufferedWriter(new OutputStreamWriter(osRTT));
				
				
				String received = breaderRTT.readLine();

				@SuppressWarnings("unused")
				long TInicio, TFin, tiempo;
				TInicio = System.currentTimeMillis(); 
				
				bwriterRTT.write(received+"\n");
				bwriterRTT.flush();//Se envia el String recibido
				
				TFin = System.currentTimeMillis();
				String toSend = rttServer.rttServer(TFin, TInicio);
				
				bwriter.write(toSend+"\n");
				bwriter.flush();//Se envia el tiempo que tomo recibir
				
				serverRTT.close();
				
				
			}else if(msg.contains("speed")) {
				
				//Genero una nueva conexion para asi por un socket enviar el msg y el tiempo de ejecucion
				ServerSocket serverSpeed = new ServerSocket(6500);
				Socket socketSpeed = serverSpeed.accept();
				InputStream isSpeed = socketSpeed.getInputStream();
				OutputStream osSpeed = socketSpeed.getOutputStream();
				BufferedReader breaderSpeed = new BufferedReader(new InputStreamReader(isSpeed));
				BufferedWriter bwriterSpeed = new BufferedWriter(new OutputStreamWriter(osSpeed));
				

				String received = breaderSpeed.readLine();

				@SuppressWarnings("unused")
				long TInicio, TFin, tiempo;
				TInicio = System.currentTimeMillis(); 
				
				bwriterSpeed.write(received+"\n");
				bwriterSpeed.flush();//Se envia el String recibido
				
				TFin = System.currentTimeMillis();
				String toSend = speedSv.speedServer(TFin, TInicio);
				
				bwriter.write(toSend+"\n");
				bwriter.flush();//Se envia el tiempo que tomo recibir
				
				serverSpeed.close();
				
			}else {
				
				bwriter.write("Palabra no registrada en el servidor"+"\n");
				bwriter.flush();
				
			}
			server.close();//Terminar la conexion con un solo cliente 
			}
			System.out.println("Conexion con el cliente finalizada");
			
		}catch(IOException ex) {
			System.out.println("Servidor incorrectamente inicializado");
		}
	}
	
	public void setDayAndHour(DayAndHour time) {
		timeServer = time;
	}
	
	public void setInterfaceInServer(InterfaceInServer intServer) {
		this.interServer = intServer;
	}
	
	public void setIpInServer(IpServer ipInServer) {
		this.ipInServer = ipInServer;
	}
	
	public void setRttServer(RTTServer rttServer) {
		this.rttServer = rttServer;
	}
	
	public void setSpeedSv(SpeedServer speedSv) {
		this.speedSv = speedSv;
	}
	
	
	
	public interface DayAndHour{
		public String timeInServer();
	}
	
	public interface InterfaceInServer{
		public String interfacesServer();
	}
	
	public interface IpServer{
		public String ipInServer();
	}
	
	public interface RTTServer{
		public String rttServer(long timeFinal, long timeInit);
	}
	
	public interface SpeedServer{
		public String speedServer(long timeFinal, long timeInit);
	}

	
	

}
