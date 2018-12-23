package chattingJava;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WriteRandomFile extends Frame implements ActionListener{
	private TextField accountField, nameField, balanceField;
	private Button enter, Load;
	private RandomAccessFile output;
	private Record data;
	public WriteRandomFile() {
		super("write File");
		data = new Record();
		try {
			output = new RandomAccessFile("customer.txt", "rw");
		}catch(IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
		setSize(300, 150);
		setLayout(new GridLayout(4,2));
		add(new Label("Account"));
		accountField = new TextField();
		add(accountField);
		add(new Label("Name"));
		nameField = new TextField(20);
		add(nameField);
		add(new Label("Balance"));
		balanceField = new TextField(20);
		add(balanceField);
		enter=new Button("Write");
		enter.addActionListener(this);
		add(enter);
		Load=new Button("Load");
		Load.addActionListener(this);
		add(Load);
		addWindowListener(new WinListener());
		setVisible(true);
	}
	public void addRecord() {
		int accountNo = 0;
		Double d;
		if(!accountField.getText().equals("")) {
			try {
				accountNo = Integer.parseInt(accountField.getText());
				if(accountNo > 0 && accountNo <= 100) {
					data.setAccount(accountNo);
					data.setName(nameField.getText());
					d = new Double(balanceField.getText());
					data.setBalance(d.doubleValue());
					output.seek((long)(accountNo-1)*Record.size());
					data.write(output);
				}
				accountField.setText("");
				nameField.setText("");
				balanceField.setText("");
			}catch(NumberFormatException nfe) {
				System.err.println("type Number");
			}catch(IOException io) {
				System.err.println("file writing error\n"+io.toString());
				System.exit(1);
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new WriteRandomFile();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == Load) {
			try {
				FindAccount();
			}catch(IOException io) {
				System.err.println("file read error\n"+io.toString());
			}
		}
		else {
			addRecord();
		}
	}
	public void FindAccount() throws IOException {
		int temp = 0;
		long length = output.length();
		long presentPos;
		output.seek(0);
		for(presentPos = output.getFilePointer(); presentPos < length; presentPos = output.getFilePointer()) {
			data.read(output);
			temp = data.getAccount();
			
			if(temp == Integer.parseInt(accountField.getText())){
				accountField.setText(Integer.toString(data.getAccount()));
				nameField.setText(data.getName());
				balanceField.setText(String.valueOf(data.getBalance()));
				break;
			}
		}
		if(temp != Integer.parseInt(accountField.getText())) {
			System.out.println("This account is not available");
		}
	}
	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	
}

class Record{
	private int account;
	private String name;
	private double balance;
	
	public void read(RandomAccessFile file) throws IOException{
		account = file.readInt();
		char namearray[] = new char[15];
		for(int i = 0; i < namearray.length; i++) {
			namearray[i] = file.readChar();
		}
		name = new String(namearray);
		balance = file.readDouble();
	}
	public void write(RandomAccessFile file) throws IOException{
		StringBuffer buf;
		System.out.println("wrtie");
		file.writeInt(account);
		if(name != null) {
			buf = new StringBuffer(name);
		}
		else {
			buf = new StringBuffer(15);
		}
		buf.setLength(15);
		file.writeChars(buf.toString());
		file.writeDouble(balance);
	}
	public void setAccount(int a) { account = a;}
	public int getAccount() {return account;}
	public void setName(String f) {name = f;}
	public String getName() {return name;}
	public void setBalance(double b) {balance = b;}
	public double getBalance() {return balance;}
	public static int size() {return 42;}
}