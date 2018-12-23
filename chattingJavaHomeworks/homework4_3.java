package chattingJava;
import java.io.*;
import java.util.Scanner;

public class homework4_3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int numbeRead;
		char[] buffer = new char[16];
		
		FileInputStream fileIn= null;
		InputStreamReader fr = null;
		
		FileOutputStream fileOut = null;
		OutputStreamWriter ow = null;
		
		String filename1;
		String filename2;
		
		filename1 = sc.nextLine();
		filename2 = sc.nextLine();
		try {
			fileIn = new FileInputStream(filename1);
			fr = new InputStreamReader(fileIn);
			fileOut = new FileOutputStream(filename2, true); //덮어쓰지 않고 이어쓰기 위해서 두번째 인자에 true를 입력합니다.
			ow = new OutputStreamWriter(fileOut, fr.getEncoding());

			while((numbeRead = fr.read(buffer))>-1) {
				System.out.println(buffer);
				ow.write(buffer, 0, numbeRead);
			}
		} catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fr.close();
		ow.close();
	}

}
