package chattingJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class homework5_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hostname;
		BufferedReader br;
		printLocalAddress();
		br = new BufferedReader(new InputStreamReader(System.in));
		try {
			do {
				System.out.println("Type on host name or IP address");
				if((hostname = br.readLine()) != null) {
					printRemoteAddress(hostname);
				}
			}while(hostname != null);
			System.out.println("Program exit.");
		}catch(IOException ex) {
			System.out.println("Input error!");
		}
	}
	static void printLocalAddress() {
		try {
			InetAddress myself = InetAddress.getLocalHost();
			System.out.println("LocalHost name : "+myself.getHostName());
			System.out.println("Local IP name : "+myself.getHostAddress());
			System.out.println("Local Host Class : "+ipClass(myself.getAddress()));
			System.out.println("LocalHost InetAddress : "+myself.toString());
			System.out.println("Loopback address : "+myself.getLoopbackAddress());
			System.out.println("CanonicalHostName : "+myself.getCanonicalHostName());
		}catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	static void printRemoteAddress(String hostname) {
		try {
			System.out.println("finding host "+hostname+"...");
			InetAddress[] machine = InetAddress.getAllByName(hostname);
			for(InetAddress i : machine) {
				System.out.println("Remote Host name : "+i.getHostName());
				System.out.println("Remote Host IP : "+i.getHostAddress());
				System.out.println("Remote Host class : "+ipClass(i.getAddress()));
				System.out.println("Remote Host InetAddress : "+i.toString());
				System.out.println("Remote Host Canonical Name : "+i.getCanonicalHostName());
			}
		}catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	static char ipClass(byte[] ip) {
		int highByte = 0xff & ip[0];
		return (highByte < 128) ? 'A' : (highByte<192) ? 'B' : (highByte < 224) ? 'C' : (highByte < 240) ? 'D' : 'E';
	}
}
