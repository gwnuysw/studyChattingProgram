package chattingJava;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class ChatMessageC extends Frame implements ActionListener, KeyListener{

	TextArea display;
	TextField wtext, ltext;
	Label mlbl, wlbl, loglbl;
	BufferedWriter output;
	BufferedReader input;
	Button logButton;
	Socket client;
	StringBuffer clientdata;
	String serverdata;
	String ID;
	
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	private static final int REQ_LOGOUT = 1011;
	private static final int REQ_WISPERSEND = 1022;
	private static final int LOGON_ACCEPT = 1031;
	private static final int LOGON_DENY = 1041;
	private static final int WISPER_MESSAGE = 2001;
	private static final int WISPER_ERROR = 2002;
	
	public ChatMessageC() {
		super("client");
		
		mlbl = new Label("show chatting status.");
		add(mlbl, BorderLayout.NORTH);
		
		display = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		Panel ptotal = new Panel(new BorderLayout());
		
		Panel pword = new Panel(new BorderLayout());
		
		wlbl = new Label("messages");
		wtext = new TextField(30);
		
		wtext.addKeyListener(this);
		pword.add(wlbl,  BorderLayout.WEST);
		pword.add(wtext, BorderLayout.CENTER);
		ptotal.add(pword, BorderLayout.CENTER);
		
		Panel plabel = new Panel(new BorderLayout());
		loglbl = new Label("logon");
		ltext = new TextField(30);
		logButton = new Button("Confirm");
		
		logButton.addActionListener(this);
		plabel.add(loglbl, BorderLayout.WEST);
		plabel.add(ltext, BorderLayout.CENTER);
		plabel.add(logButton, BorderLayout.EAST);
		ptotal.add(plabel, BorderLayout.SOUTH);
		
		add(ptotal, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		setSize(500, 250);
		setVisible(true);
	}
	public void runClient() {
		try {
			client = new Socket(InetAddress.getLocalHost(), 5000);
			mlbl.setText("connected server name : "+client.getInetAddress().getHostName());
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			clientdata = new StringBuffer(2048);
			mlbl.setText("connection finished, enter ID");
			while(true) {
				serverdata = input.readLine();
				
				if(serverdata.contains(SEPARATOR.toString())) {
					StringTokenizer st = new StringTokenizer(serverdata, SEPARATOR);
					int command = Integer.parseInt(st.nextToken());
					
					switch(command) {
						case LOGON_ACCEPT:{
							mlbl.setText(ID+" login success");
							logButton.setLabel("logOut");
							ltext.setVisible(false);
							break;
						}
						case LOGON_DENY:{
							display.append("Duplicated User, access denied\n");
							break;
						}
						case WISPER_ERROR:{
							display.append("No such uesr"+"\r\n");
							break;
						}
						case WISPER_MESSAGE:{
							display.append(serverdata+"\r\n");
							break;
						}
					}
				}
				else {
					display.append(serverdata+"\r\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatMessageC c = new ChatMessageC();
		c.runClient();
	}
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(logButton.getLabel().equals("Confirm")) {	//if logButton is Confim then execute 
			System.out.println(ID);
			
				ID = ltext.getText();
				if(ID.equals(""))
				{
					mlbl.setText("no ID");
					ID = null;
				}
				else {
					try {
						clientdata.setLength(0);
						clientdata.append(REQ_LOGON);
						clientdata.append(SEPARATOR);
						clientdata.append(ID);
						System.out.println("hellloooooo");
						output.write(clientdata.toString()+"\r\n");	//send req server
						System.out.println("hellloooooo");
						output.flush();
						
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			
			System.out.println("hellloooooo");
		}
		else if(logButton.getLabel().equals("logOut")){
			clientdata.setLength(0);
			clientdata.append(REQ_LOGOUT);
			clientdata.append(SEPARATOR);
			clientdata.append(ID);
			try {
				output.write(clientdata.toString()+"\r\n");
				output.flush();
				display.setText("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logButton.setLabel("Confirm");
			ltext.setVisible(true);
			ltext.setText(null);
			ID = null;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
			String message = wtext.getText();
			StringTokenizer st = new StringTokenizer(message, " ");
			
			if(ID == null) {
				mlbl.setText("login again");
				wtext.setText("");
			}
			else {
				try {
					if(st.nextToken().equals("/w")) {
						String WID = st.nextToken();
						String Wmessage = st.nextToken();
						display.append(WID+'\n');
						while(st.hasMoreTokens()) {
							Wmessage = Wmessage+" "+st.nextToken();
						}
						clientdata.setLength(0);
						clientdata.append(REQ_WISPERSEND);
						clientdata.append(SEPARATOR);
						clientdata.append(ID);
						clientdata.append(SEPARATOR);
						clientdata.append(WID);
						clientdata.append(SEPARATOR);
						clientdata.append(Wmessage);
						output.write(clientdata.toString()+"\r\n");
						output.flush();
						wtext.setText("");
					}
					else {
						clientdata.setLength(0);
						clientdata.append(REQ_SENDWORDS);
						clientdata.append(SEPARATOR);
						clientdata.append(ID);
						clientdata.append(SEPARATOR);
						clientdata.append(message);
						output.write(clientdata.toString()+"\r\n");
						output.flush();
						wtext.setText("");
					}
					
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
