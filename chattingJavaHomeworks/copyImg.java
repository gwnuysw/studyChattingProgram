package chattingJava;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class copyImg {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			copy(new RandomAccessFile("copyfrom.jpg", "r"), new RandomAccessFile("copyto.jpg","rw"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void copy(RandomAccessFile in, RandomAccessFile out) throws IOException{
		int bytesRead;
		byte[] buffer = new byte[1024];
		long TotalProgress = in.length();
		long sep10= (TotalProgress/10);
		long progress = sep10;
		int iterate = 1;
		long curentProgress;
		System.out.println("total:"+TotalProgress);
		synchronized(in) {
			synchronized(out) {
				while((bytesRead = in.read(buffer))>=0) {
					out.write(buffer, 0, bytesRead);
					curentProgress = out.getFilePointer();
		//			System.out.println("sep10: "+sep10);
		//			System.out.println("curent:"+curentProgress);
					if(curentProgress >= progress) {
						iterate++;
						
						System.out.print("*");
						
						progress = sep10 * iterate;
					}
				}
				System.out.println("\nfinished");
			}
		}
		in.close();
		out.close();
	}
}
