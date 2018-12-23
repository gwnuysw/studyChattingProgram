package chattingJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class dicClient {

	public static void main(String args[]) {
		Socket theSocket;
		String host;
		InputStream is;
		BufferedReader reader;
		BufferedWriter writer;
		String eng;
		Scanner sc = new Scanner(System.in);
		
		if(args.length > 0) {
			host = args[0];
		}
		else {
			host="localhost";
		}
		try {
			theSocket = new Socket(host, 1024);
			
			is = theSocket.getInputStream();
			
			reader = new BufferedReader(new InputStreamReader(is));
			
			writer = new BufferedWriter(new OutputStreamWriter(theSocket.getOutputStream()));
			
			System.out.println("connected");
			
			eng = sc.nextLine();
			
			writer.write(eng+"\n");
			
			writer.flush();
			
			String kor = reader.readLine();

			System.out.println(kor);
			
			while(true);
		}catch(UnknownHostException e) {
			System.err.println(args[0]+"Couldn't find host");
		}catch(IOException e) {
			System.err.println(e);
		}
	}
}
