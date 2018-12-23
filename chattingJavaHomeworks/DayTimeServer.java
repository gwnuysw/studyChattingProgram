package chattingJava;
import java.io.*;
import java.net.*;
import java.util.*;

public class DayTimeServer {
	public final static int daytimeport=1024;
	
	public static void main(String[] args)
	{
		ServerSocket theServer;
		Socket theSocket = null;

		try
		{
			theServer = new ServerSocket(daytimeport);
			SendTime time = null;
			while(true)
			{
				try
				{
					theSocket = theServer.accept();
					time = new SendTime(theSocket);
					time.start();
				}
				catch(IOException e)
				{
					System.err.println(e);
				}
				
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
}