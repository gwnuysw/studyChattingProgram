package chattingJava;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDP_Calculator extends Frame implements ActionListener{
	private TextField monitor;
	private Button bt; 
	private boolean state=true;//입력진행모드 true 면 한자리 숫자. false 면 한자리 이상
	private int operator;//연산자 구분 번호로 +는1, -는 2, *는 3, /는 4
	private boolean OperatorStatus=true;//연산자를 버튼을 연속으로 눌렀는지의 여부, 연속으로 누른다면 true
	private String operand1;
	private String operator1;
	private String operand2;
	private String operator2;
	
	private DatagramSocket Sock;
	private DatagramPacket sendPack;
	private DatagramPacket recievePack;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UDP_Calculator cs = new UDP_Calculator();
		cs.setVisible(true);
		cs.Receive();
	}
		// 생성자
	public UDP_Calculator(){

		super("AWT 계산기");

		Panel main = new Panel();
		main.setLayout(new BorderLayout());

		monitor = new TextField();
		main.add("North",monitor);

		Panel p=new Panel();
		p.setLayout(new GridLayout(4,4));

// 	버튼 부착하고 이벤트 리스너 연결하기
		String buttonLabel="789+456-123*0C=/"; 
		for(int i=0;i<buttonLabel.length();i++){
			bt=new Button(buttonLabel.substring(i,i+1));
			bt.addActionListener(this);
			p.add(bt);
		}
		main.add("Center",p);
		add(main);

//종료버튼 달기
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		setSize(180,150);
		//socket open
		try {
			Sock = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operand1 = "";
		operator1 = "";
		operand2 = "";
		operator2 = "";
		
	}
	public void actionPerformed(ActionEvent ae){

		String val=ae.getActionCommand();//이벤트가 발생한 버튼을 문자열로 리턴
		
		if(val.equals("C")){
			Clear();
	//숫자버튼을 눌렸는지 검사. 이때 숫자를 연속해서 누르면 십단위가 되고 아님 1단위가 된다.연속해서 눌렀는지를 체크하는 변수 state
		}else if('0' <= val.charAt(0) && val.charAt(0) <= '9'){
			if (state) monitor.setText(val);
			else monitor.setText(monitor.getText()+val);
			state = false;
		}else
			monitorTovalue(val);
	}
	
	public void Receive() {
		byte[] buffer = new byte[10];
		int Index;
		while(true) {
			recievePack = new DatagramPacket(buffer, buffer.length);
			try {
				Sock.receive(recievePack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("buffer.lenght : "+buffer.length);
			for(byte ch : buffer) {
				System.out.println("byte init : "+ ch);
			}

			operand1 = new String(recievePack.getData(), 0, recievePack.getLength());
			for(char ch : operand1.toCharArray()) {
				System.out.println("init : "+ ch);
			}
			monitor.setText(operand1);
			System.out.println("received : "+operand1);
			state = true;
		}
	}
	private void Send(String sendData) {
		byte[] buffer = new byte[10];
		buffer = sendData.getBytes();
		try {
			sendPack = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 4000);
			Sock.send(sendPack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void monitorTovalue(String operator) {
		if(operator1.equals("")) {
			operand1 = monitor.getText();
			System.out.println(operand1);
			Send(operand1);
			operator1 = operator;
			System.out.println(operator1);
			Send(operator1);
		}
		else {
			operand2 = monitor.getText();
			System.out.println("send operand : "+operand2);
			Send(operand2);
			operator2 = operator;
			System.out.println(operator2);
			Send(operator2);
			
			monitor.setText(operand1);
			
			operator1 = operator2;
		}
		monitor.setText("");
		state = true;
	}
	private void Clear() {
		monitor.setText("");
		operand1 = "";
		operator1 = "";
		operand2 = "";
		operator2 = "";
		Send(operand1);
		Send(operator1);
	}
}
