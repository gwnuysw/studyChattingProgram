package chattingJava;
import java.io.*;

public class pr4_3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numbeRead;
		char[] buffer = new char[16];
		FileInputStream fileIn= null;
		FileWriter fileOut = null;
		InputStreamReader isr1 = null;
		
		try {
			fileIn = new FileInputStream(args[1]);
			fileOut = new FileWriter(args[3]);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
