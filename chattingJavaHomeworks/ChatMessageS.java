package chattingJava;

import java.awt.*;
import java.awt.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatMessageS extends Frame{
	TextArea display;
	Label info;
	ArrayList<ServerThread> list;
	Hashtable hash;
	public ServerThread SThread;
	
	public ChatMessageS() {
		super("server");
		info = new Label();
		add(info, BorderLayout.CENTER);
		display = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.SOUTH);
		addWindowListener(new WinListener());
		setSize(300, 250);
		setVisible(true);
	}
	
	public void runServer() {
		ServerSocket server;
		Socket sock;
		hash = new Hashtable();
		try {
			list = new ArrayList<ServerThread>();
			server = new ServerSocket(5000, 100);
			try {
				while(true) {
					sock = server.accept();
					SThread = new ServerThread(this, sock,display);
					SThread.start();
					info.setText(sock.getInetAddress().getHostName()+" server connected client");
				}
			}catch(IOException ioe) {
				server.close();
				ioe.printStackTrace();
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatMessageS s = new ChatMessageS();
		s.runServer();
	}
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
class ServerThread extends Thread{
	Socket sock;
	BufferedWriter output;
	BufferedReader input;
	TextArea display;
	Label info;
	TextField text;
	String clientdata;
	ChatMessageS cs;
	String AcceptedId;
	StringBuffer serverdata;
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	private static final int REQ_WISPERSEND = 1022;
	private static final int REQ_LOGOUT = 1011;
	private static final int LOGON_ACCEPT = 1031;
	private static final int LOGON_DENY = 1041;
	private static final int WISPER_MESSAGE = 2001;
	private static final int WISPER_ERROR = 2002;
	
	public ServerThread(ChatMessageS c, Socket s, TextArea ta) {
		sock = s;
		display = ta;
		cs = c;
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		serverdata = new StringBuffer(1024);
	}
	public void run() {

		try {
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				int cnt = cs.list.size();
				int i;
				display.append(clientdata);
				ServerThread SThread;
				String ID;
				switch(command) {
					case REQ_LOGON : {
						ID = st.nextToken();
						boolean catchflag = false;//bring client's ID
						
						for(Iterator<ServerThread> it = cs.list.iterator(); it.hasNext();) {						//check ID list
							SThread = (ServerThread)it.next();
							System.out.println("deny checking "+SThread.AcceptedId);
							if(SThread.AcceptedId.equals(ID)) {			//access deny
								display.append(ID+" denied.\r\n");
								serverdata.setLength(0);
								serverdata.append(LOGON_DENY);
								serverdata.append(SEPARATOR);
								serverdata.append(ID);
								System.out.println(ID);
								output.write(serverdata.toString()+"\r\n");
								output.flush();
								catchflag = true;
								break;
							}
						}
						
						if(catchflag == false) {
							//ID accept
							System.out.println("accept req");
							
							this.AcceptedId = ID;
							
							cs.list.add(this);									//add threads list
							for (Iterator<ServerThread> it = cs.list.iterator(); it.hasNext();) {
								SThread = (ServerThread)it.next();
								System.out.println(SThread.AcceptedId);
							}
							display.append("client "+ID+" logoned.\r\n");
							
							serverdata.setLength(0);
							serverdata.append(LOGON_ACCEPT);
							serverdata.append(SEPARATOR);
							serverdata.append(ID);

							cs.hash.put(ID, this);
							
							output.write(serverdata.toString()+"\r\n");
							output.flush();
							
							System.out.println("check point");
						}
						break;
					}
					case REQ_SENDWORDS :{
						ID = st.nextToken();
						String message = st.nextToken();
						display.append(ID+" : "+message+"\r\n");
						for(Iterator<ServerThread> it = cs.list.iterator(); it.hasNext();) {
							SThread = (ServerThread)it.next();
							SThread.output.write(ID+" : "+message+"\r\n");
							SThread.output.flush();
							System.out.println("message send to "+ SThread.AcceptedId);
						}
						break;
					}
					case REQ_LOGOUT : {
						ID = st.nextToken();
						System.out.println("get logout req");
						cs.hash.remove(ID);
						cs.list.remove(this);
						/*for (Iterator<ServerThread> it = cs.list.iterator(); it.hasNext();) {
							SThread = (ServerThread)it.next();
							System.out.println("list in : "+SThread.AcceptedId+" want delete ID"+ID);
							if(SThread.AcceptedId.equals(ID)) {
								
								it.remove();
							}
						}*/
						display.append(ID+" logout\r\n");
						break;
					}
					case REQ_WISPERSEND : {
						ID = st.nextToken();
						String WID = st.nextToken();
						String message = st.nextToken();
						display.append(ID+"->"+WID+" : "+message+"\r\n");
						
						if(cs.hash.get(WID) == null) {
							serverdata.setLength(0);
							serverdata.append(WISPER_ERROR);
							serverdata.append(SEPARATOR);
							output.write(serverdata.toString()+"\r\n");
							output.flush();
						}
						else {
							SThread = (ServerThread)cs.hash.get(ID);
							SThread.output.write(WISPER_MESSAGE+ID+"->"+WID+" : "+message+"\r\n");
							SThread.output.flush();
							SThread = (ServerThread)cs.hash.get(WID);
							SThread.output.write(WISPER_MESSAGE+ID+" : "+message+"\r\n");
							SThread.output.flush();
						}
						break;
					}
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		cs.hash.remove(this);
		cs.list.remove(this);
		try {
			sock.close();
		}catch(IOException ea) {
			ea.printStackTrace();
		}
	}
}
