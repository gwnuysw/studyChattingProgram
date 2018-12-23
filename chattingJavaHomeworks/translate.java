package chattingJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class translate extends Thread{
	Socket connection;
	BufferedWriter bufwriter = null;
	BufferedReader bufreader = null;
	BufferedReader diction = null;
	String kor = null;
	String eng = null;
	String findeng = null;
	public translate(Socket connection) throws IOException{
		this.connection = connection;
	}
	public void run() {
		OutputStream os;
		try {
			os = connection.getOutputStream();
			bufwriter = new BufferedWriter(new OutputStreamWriter(os));
		
			bufreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			eng = bufreader.readLine();

			System.out.println("get word "+eng);
			
			diction = new BufferedReader(new InputStreamReader(new FileInputStream("Dictionary")));
			
			while((findeng = diction.readLine()) != null) {
				if(findeng.equals(eng)) {
					kor = diction.readLine();
					break;
				}
			}
			if(kor == null) {
				bufwriter.write("No data\n");
			}
			else {
				bufwriter.write(kor+"\n");
			}
			
			bufwriter.flush();
			
			
			System.out.println(kor);
			
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
