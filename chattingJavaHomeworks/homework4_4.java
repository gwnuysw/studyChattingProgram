package chattingJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.TimeZone;

public class homework4_4 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		boolean equality = true;
		String buffer1;
		String buffer2;
		
		File file1 = null;
		FileInputStream fileIn1= null;
		InputStreamReader fr1 = null;
		BufferedReader br1 = null;
		
		File file2 = null;
		FileInputStream fileIn2 = null;
		InputStreamReader fr2 = null;
		BufferedReader br2 = null;
		
		String filename1;
		String filename2;
		
		filename1 = sc.nextLine();
		filename2 = sc.nextLine();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd (E) hh mm");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		long time;
		try {
			file1 = new File(filename1);
			fileIn1 = new FileInputStream(file1);
			fr1 = new InputStreamReader(fileIn1);
			br1 = new BufferedReader(fr1);
			
			file2 = new File(filename2);
			fileIn2 = new FileInputStream(file2);
			fr2 = new InputStreamReader(fileIn2);
			br2 = new BufferedReader(fr2);
			
			while((buffer1 = br1.readLine()) != null && (buffer2 = br2.readLine()) != null ) {
				if(!buffer1.equals(buffer2)) {
					equality = false;
					System.out.println("file1 length : "+file1.length());
					System.out.println("file2 length : "+file2.length());
					break;
				}
			}
			if(equality) {
				time = file1.lastModified();
				System.out.println("last Modified Date : " + sdf.format(new Date(time)));
				time = file2.lastModified();
				System.out.println("last Modified Date : " + sdf.format(new Date(time)));
			}
		} catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileIn1.close();
		fileIn2.close();
	}

}
