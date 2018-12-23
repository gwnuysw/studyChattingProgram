package chattingJava;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class CalculatorServer {

	private DatagramSocket Sock;
	private DatagramPacket sendPack;
	private DatagramPacket receivePack;
	private int operand1;
	private String operator1;
	private int operand2;
	private String operator2;
	private String buffer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalculatorServer cs = new CalculatorServer();
		cs.Receive();
	}
	public CalculatorServer(){
		try {
			Sock = new DatagramSocket(4000);
		} catch (SocketException e) {
			System.out.println("duplicated port");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operand1 = 0;
		operator1 = "";
		operand2 = 0;
		operator2 = "";
	}
	public void Receive() {
		int Index;
		while(true) {
			byte[] data = new byte[10];
			receivePack = new DatagramPacket(data, data.length);
			try {
				Sock.receive(receivePack);
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Index = 0; data[Index] != 0; Index++);
			buffer = new String(receivePack.getData(), 0, receivePack.getLength());

			buffer = buffer.substring(0, Index);
			if(buffer.equals("")) {
				operand1 = 0;
				operator1 = "";
				operand2 = 0;
				operator2 = "";
			}
			else if(operator1.equals("")){
				if('0' <= buffer.charAt(0) && buffer.charAt(0) <= '9') {
					
					System.out.println(buffer);
					operand1 = Integer.parseInt(buffer);
				}
				else {
					System.out.println("operator : "+buffer);
					operator1 = buffer;
				}
			}
			else {
				
				if('0' <= buffer.charAt(0) && buffer.charAt(0) <= '9') {
					
					operand2 = Integer.parseInt(buffer);
				}
				else {
					System.out.println(buffer);
					operator2 = buffer;
				}
				operand1 = Operate(operator1);
				operator1 = operator2;
				System.out.println("result : "+operand1);
				Send(operand1);
			}
		}
		
	}
	
	private void Send(int ResultData) {
		byte[] data = new byte[10];
		String result = String.valueOf(ResultData);
		System.out.println("send : "+result);
		data = result.getBytes();
		System.out.println("data length : "+data.length);
		for(byte cs:data) {
			System.out.println("byte send : "+cs);
		}
		sendPack = new DatagramPacket(data, data.length, receivePack.getAddress(), receivePack.getPort());
		try {
			Sock.send(sendPack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int Operate(String operator) {
		switch(operator) {
			case "+":{
				return operand1 + operand2;
			}
			case "-":{
				return operand1 - operand2;
			}
			case "*":{
				return operand1 * operand2;
			}
			case "/":{
				return operand1 / operand2;
			}
			case "=":{
				return operand2;
			}
		}
		return 0;
	}
}