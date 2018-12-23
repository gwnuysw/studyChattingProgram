package chattingJava;
import java.io.*;
public class Combi2_2and2_4 {
	static DataOutputStream dos;
	static FileInputStream fin;
	static DataInputStream dis;
	public Combi2_2and2_4() {
		// TODO Auto-generated constructor stub
		char ch;
		String sdata1, sdata2;
		try {
			String data;
			FileOutputStream fout = new FileOutputStream("chardata.txt");
			dos = new DataOutputStream(fout);
			dos.writeChar(65);
			dos.writeUTF("반갑습니다.");
			dos.writeUTF("자바 채팅 프로그래밍 교재");
		}catch(IOException e) {
			System.err.println(e);
		}
		finally {
			try {
				if(dos!=null) dos.close();
			}catch(IOException e) {
				System.err.println(e);
			}
		}
		try {
			fin = new FileInputStream("chardata.txt");
			dis = new DataInputStream(fin);
			ch = dis.readChar();
			sdata1 = dis.readUTF();
			sdata2 = dis.readUTF();
			System.out.println(sdata1);
			System.out.println(sdata2);
		}catch(EOFException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.err.println(e);
		}
	}

}
