package chattingJava;
import java.io.*;


public class Combi2_1and2_3 {
	static FileOutputStream fout;
	static DataOutputStream dos;
	static FileInputStream fin;
	static DataInputStream dis;
	
	public Combi2_1and2_3() {
		boolean bdata;
		double ddata;
		int number;
		// TODO Auto-generated constructor stub
		try {
			fout = new FileOutputStream("numberdata.txt");
			dos = new DataOutputStream(fout);
			
			
			dos.writeBoolean(true);
			dos.writeDouble(989.27);
			
			for(int i = 0; i <= 500; i++) {
				dos.writeInt(i);
			}
			
			
		}catch(IOException e) {
			System.err.println(e);
		}
		finally {
		
			try {
				if(dos!=null) {
					dos.close();
				}
			}catch(IOException e) {}
		}
		
		
		try {
			fin = new FileInputStream("numberdata.txt");
			dis = new DataInputStream(fin);

			bdata = dis.readBoolean();
			System.out.println(bdata);
			
			ddata = dis.readDouble();
			System.out.println(ddata);
			
			while(true) {
				number = dis.readInt();
				System.out.println(number+" ");
			}
		}catch(EOFException e) {
			System.out.println("EOF");
		}catch(IOException e) {
			System.err.println(e);
		}
		finally {
		
			try {
				if(dis!=null){
					dis.close();
				}
			}catch(IOException e) {}
		}
	}

}
