package chattingJava;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class ex2_5 extends Frame implements ActionListener{
	private TextField account, name, balance, contact;
	private Button enter, done;
	private DataOutputStream output;
	public ex2_5() {
		// TODO Auto-generated constructor stub
		super("고객파일 생성");
		try {
			output = new DataOutputStream(new FileOutputStream("client.txt"));
		}catch(IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
		setSize(250, 130);
		setLayout(new GridLayout(5,2));
		add(new Label("구좌번호"));
		account = new TextField(20);
		add(account);
		add(new Label("이름"));
		name = new TextField(20);
		add(name);
		add(new Label("잔고"));
		balance = new TextField(20);
		add(balance);
		add(new Label("전화번호"));
		contact = new TextField(20);
		add(contact);
		enter = new Button("입력");
		enter.addActionListener(this);
		add(enter);
		done = new Button("종료");
		done.addActionListener(this);
		add(done);
		setVisible(true);
	}
	public void addRecord() {
		int accountNo = 0;
		String d;
		if(!account.getText().equals("")) {
			try {
				accountNo = Integer.parseInt(account.getText());
				if(accountNo > 0) {
					output.writeInt(accountNo);
					output.writeUTF(name.getText());
					d = balance.getText();
					output.writeDouble(Double.valueOf(d));
					output.writeUTF(contact.getText());
					contact.setText("");
					account.setText("");
					name.setText("");
					balance.setText("");
				}
			}catch(NumberFormatException nfe) {
				System.err.println("정수를 입력해야 합니다.");
			}catch(IOException io) {
				System.err.println(io.toString());
				System.exit(1);
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		addRecord();
		if(e.getSource() == done) {
			try {
				output.close();
			}catch(IOException io) {
				System.err.println(io.toString());
			}
			System.exit(0);
		}
	}
}
