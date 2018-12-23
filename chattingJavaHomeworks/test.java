package chattingJava;

import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String before = "123";
		
		byte[] data = new byte[40];
		data = before.getBytes();
		
		String array = new String(data, 0, data.length);
		
		System.out.println(array);
		for(char ch: array.toCharArray()) {
			System.out.println("number : "+ch);
		}
		System.out.println(Integer.parseInt(array));
	}
}
