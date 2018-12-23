package chattingJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Udp_day_time_client {
	private DatagramPacket sendPacket, recievePacket;
	private DatagramSocket socket;
	public Udp_day_time_client(){
		try {
			socket  = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Udp_day_time_client c = new Udp_day_time_client();
		c.waitForPackets();
	}
	public void waitForPackets() {
		byte data[] = new byte[100];
		
		String str = new String("Thank you!");
		try {
			data = str.getBytes("ASCII");
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 13);
			socket.send(sendPacket);
			recievePacket = new DatagramPacket(data, data.length);
			socket.receive(recievePacket);
			System.out.println(new String(recievePacket.getData()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
