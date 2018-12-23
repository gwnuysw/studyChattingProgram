package chattingJava;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

public class homework4_1 {

	public static void main(String[] args)
	{
		String buf;
		FileReader fin=null;
		FileWriter fout=null;
		
		if(args.length != 2)
		{
			System.out.println("소스파일 및 대상파일을 지정하십시오.");
			System.exit(0);
		}
		try
		{
			fin = new FileReader(args[0]);
			fout = new FileWriter(args[1]);
		}catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		LineNumberReader read = new LineNumberReader(fin);
		PrintWriter write = new PrintWriter(fout);
		int num;
		
		while(true)
		{
			try
			{
				buf=read.readLine();//한줄을 읽고
				num = read.getLineNumber();//번호를 붙여줍니다.
				if(buf == null)
					break;
			}catch (IOException e)
			{
				System.out.println(e);
				break;
			}
			buf = num + " : " + buf;
			write.println(buf); 
			// num++(이 부분 사용하지 않고)
		}	
		try
		{
			fin.close();
			fout.close();
		} catch (IOException e)
		{
			System.out.println(e);
		}
	}
}
