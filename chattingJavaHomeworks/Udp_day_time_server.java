package chattingJava;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

public class Udp_day_time_server {
public final static int daytimeport=13;
private DatagramPacket sendPacket;
private DatagramPacket recievePacket;
private static DatagramSocket socket;
	public Udp_day_time_server(){
		try {
			socket = new DatagramSocket(daytimeport);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		Udp_day_time_server s = new Udp_day_time_server();
		s.waitForPackets();
	}
	public void waitForPackets() {
		Date now;
		
		while(true) {
			try {
				byte data[] = new byte[100];
				recievePacket = new DatagramPacket(data, data.length);
				socket.receive(recievePacket);
				System.out.println(new String(recievePacket.getData()));
				now = new Date();
				data = now.toString().getBytes("ASCII");
				sendPacket = new DatagramPacket(data,data.length,recievePacket.getAddress(),recievePacket.getPort());
				socket.send(sendPacket);
			}
			catch(IOException io) {
				io.printStackTrace();
			}
		}
	}
}