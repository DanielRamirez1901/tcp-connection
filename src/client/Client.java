package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import comm.TCPConnection.RTTServer;


public class Client {
	
	public static RTTServer rttserver;
	public static long generalTime=0;
	
	
	public static void main(String[] args) {
		try {
			boolean stop = false;
			System.out.println(showIntro());
			
			while(!stop) {
				
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				String line = scanner.nextLine()+"\n";
				
				Socket socket = new Socket("127.0.0.1", 6000);
				
				OutputStream os = socket.getOutputStream();
				InputStream is = socket.getInputStream();
				BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(os));
				BufferedReader breader = new BufferedReader(new InputStreamReader(is));
			
				if(line.contains("RTT")) {
					
					//String msg1024 = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque pen";
					String msgToSend = new String(new byte[1024]);
					
					bwriter.write(line);
					bwriter.flush();
					
					
					Socket socketRTT = new Socket("127.0.0.1", 6500);
					OutputStream osRTT = socketRTT.getOutputStream();
					InputStream isRTT = socketRTT.getInputStream();
					BufferedWriter bwriterRTT = new BufferedWriter(new OutputStreamWriter(osRTT));
					BufferedReader breaderRTT = new BufferedReader(new InputStreamReader(isRTT));
					
					bwriterRTT.write(msgToSend+"\n");
					bwriterRTT.flush();
					long TInicio, TFin, tiempo, finalTime;
					TInicio = System.currentTimeMillis();
					
				
					@SuppressWarnings("unused")
					String received = breaderRTT.readLine();
					String receivedTime = breader.readLine();

					TFin = System.currentTimeMillis();
					tiempo = TFin - TInicio;
					long timeConverted = Long.parseLong(receivedTime);
					finalTime = tiempo + timeConverted;
					System.out.println("El tiempo de ida y vuelta fue: "+finalTime+" milisegundo");
					socketRTT.close();
					
				}else if(line.contains("speed")) {
					
					String msgToSend = new String(new byte[8192]);
					
					bwriter.write(line);
					bwriter.flush();

					//Genero nueva conexion para envio del msg 2
					Socket socketSpeed = new Socket("127.0.0.1", 6500);
					OutputStream osSpeed = socketSpeed.getOutputStream();
					InputStream isSpeed = socketSpeed.getInputStream();
					BufferedWriter bwriterSpeed = new BufferedWriter(new OutputStreamWriter(osSpeed));
					BufferedReader breaderSpeed = new BufferedReader(new InputStreamReader(isSpeed));
					
					bwriterSpeed.write(msgToSend+"\n");
					bwriterSpeed.flush();
					long TInicio, TFin, tiempo, finalTime;
					TInicio = System.currentTimeMillis();
					
				
					@SuppressWarnings("unused")
					String received = breaderSpeed.readLine();//Recibo el msg de vuelta 
					String receivedTime = breader.readLine();//Recibo el tiempo

					TFin = System.currentTimeMillis();
					tiempo = TFin - TInicio;
					long timeConverted = Long.parseLong(receivedTime);
					finalTime = tiempo + timeConverted;
					double seconds = finalTime*0.0001;
					double speedInternet = 8192/seconds;
					System.out.println("La velocidad de transmision es : "+speedInternet+" B/s");
					socketSpeed.close();
					
				}	else {
					bwriter.write(line);
					bwriter.flush();
					String received = breader.readLine();
					System.out.println(received);
				}
				
				if(line.contains("off")) {
					stop = true;
				}
				socket.close();
			}
			
			
		}catch(IOException ex) {
			
		}
		System.out.println("Conexion con el servidor finalizada");
	}
	
	public static String showIntro() {
		String introMsg;
		introMsg = "----------------------------";
		introMsg += "\n********** Welcome ***********";
		introMsg += "\nEscriba la palabra";
		introMsg += "\n**[1]remoteIpconfig";
		introMsg += "\n**[2]interfaces";
		introMsg += "\n**[3]whatsTimeIsIt";
		introMsg += "\n**[4]RTT";
		introMsg += "\n**[5]speed";
		introMsg += "\n**[6]off";
		return introMsg;
	}

	public static void setGeneralTime(long generalTime) {
		Client.generalTime = generalTime;
	}
	

	
	

}
