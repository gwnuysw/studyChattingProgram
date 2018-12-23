package chattingJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class SendTime extends Thread{
	Socket connection;
	BufferedWriter bufwriter = null;
	BufferedReader bufreader = null;
	String say;
	SendTime(Socket connection) throws IOException{
		this.connection = connection;
	}
	public void run() {
		OutputStream os;
		try {
			os = connection.getOutputStream();
			bufwriter = new BufferedWriter(new OutputStreamWriter(os));
		
			Date now = new Date();

			bufwriter.write(now.toString() + "r\n");
			bufwriter.flush();
			
			bufreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			say = bufreader.readLine();
			System.out.println(say);
			
			while(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(IOException e)
			{
				System.err.println(e);
			}
		}
	}
}
