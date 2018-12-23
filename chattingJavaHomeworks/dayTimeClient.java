package chattingJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class dayTimeClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket theSocket;
		String host;
		InputStream is;
		BufferedReader reader;
		BufferedWriter writer;
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
			
			String theTime = reader.readLine();
			
			System.out.println("host time is "+theTime);
			theSocket.shutdownInput();
			
			writer = new BufferedWriter(new OutputStreamWriter(theSocket.getOutputStream()));
			writer.write("Thank you!\n");
			
			writer.flush();
			
			while(true);
		}catch(UnknownHostException e) {
			System.err.println(args[0]+"Couldn't find host");
		}catch(IOException e) {
			System.err.println(e);
		}
	}
}