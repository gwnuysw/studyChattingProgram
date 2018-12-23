package chattingJava;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class homework5_2 extends Frame implements ActionListener
{
	TextField hostname, hostclass;
	Button getinfor;
	TextArea display;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		homework5_2 host = new homework5_2("InetAddress Class");
		host.setVisible(true);
	}
	public homework5_2(String str) {
		super(str);
		addWindowListener(new WinListener());
		setLayout(new BorderLayout());
		
		Panel inputpaner = new Panel();
		inputpaner.setLayout(new BorderLayout());
		inputpaner.add("North", new Label("Host name"));
		hostname = new TextField("", 30);
		inputpaner.add("Center", hostname);
		
		getinfor = new Button("Get Host Information");
		inputpaner.add("South", getinfor);
		getinfor.addActionListener(this);
		add("North", inputpaner);
		
		Panel outputpanel = new Panel();
		outputpanel.setLayout(new BorderLayout());
		display = new TextArea("", 24, 40);
		display.setEditable(false);
		outputpanel.add("North", new Label("Internet address"));
		outputpanel.add("Center", display);
		add("Center", outputpanel);
		
		Panel outputpanel2 = new Panel();
		outputpanel2.setLayout(new BorderLayout());
		outputpanel2.add("North", new Label("Class"));
		hostclass = new TextField("", 30);
		outputpanel2.add("Center", hostclass);
		hostclass.setEditable(false);
		add("South", outputpanel2);
		
		setSize(270, 300);
	}
	public void actionPerformed(ActionEvent e)
	{
		String name = hostname.getText();
		String seconPanel = "";
		display.setText("");
		try
		{
			InetAddress[] inet = InetAddress.getAllByName(name);
			hostclass.setText(ipClass(inet[0].getAddress()));
			for(InetAddress i : inet) {
				System.out.println("Remote Ip Address : "+i.getHostAddress());
				String ip = i.getHostAddress() + "\n";
				display.append(ip);
			}
		}
		catch (UnknownHostException e2)
		{
			String ip = name + ": There is no host.\n";
			display.append(ip);
		}
	}
	static String ipClass(byte[] ip)
	{
		int highByte = 0xff & ip[0];
		return (highByte < 128) ? "A" : (highByte<192) ? "B" : (highByte < 224) ? "C" : (highByte < 240) ? "D" : "E";
	}

	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);

		}
	}

}