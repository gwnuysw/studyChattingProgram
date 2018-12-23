package chattingJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import chattingJava.UDP_1o1_chat_client.WinListener;

public class UDP_1o1_chat extends Frame implements ActionListener{
	private TextField enter;
	  private TextArea display;
	  private DatagramPacket sendPacket, receivePacket;
	  private DatagramSocket socket;
	  public UDP_1o1_chat(){
	     super( "server" );
	     enter = new TextField( "type on message" );
	     enter.addActionListener( this ); //입력된 데이터를 전송하기위한 이벤
	     add( enter, BorderLayout.NORTH );
	     display = new TextArea();
	     add( display, BorderLayout.CENTER );
	     addWindowListener(new WinListener());
	     setSize( 400, 300 );
	     setVisible( true );
	     try {
	        socket = new DatagramSocket(5000); // 
	     }catch( SocketException se ) {
	        se.printStackTrace();
	        System.exit( 1 );
	     }
	  }
	  public void waitForPackets(){
	     while ( true ) {
	        try { // make receive packet
	        	 System.out.println("ASDASD");
	           byte data[] = new byte[ 100 ];
	           System.out.println("ASDAS");
	           receivePacket = new DatagramPacket( data, data.length );
	           System.out.println("ASDA");
	           socket.receive( receivePacket ); // wait packet
	           System.out.println("ASD");
	           display.append( "\nreceived packet:" +
	              "\nserver address: " + receivePacket.getAddress() +
	              "\nserver port number: " + receivePacket.getPort() +
	              "\nmessage length: " + receivePacket.getLength() +
	              "\nmessage : " + new String( receivePacket.getData() ) );
	        }catch( IOException exception ) {
	           display.append( exception.toString() + "\n" );
	           exception.printStackTrace();
	        }
	     }
	  }
	  public void actionPerformed( ActionEvent e ){
	     try {
	        display.append( "\nsend message: " + e.getActionCommand() + "\n" );
	        String s = e.getActionCommand(); // get data for send server
	        byte data[] = s.getBytes(); // change byte array
	        sendPacket=new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 4000);
	        socket.send( sendPacket );
	        display.append( "packet send complete\n" );
	     }catch ( IOException exception ) {
	        display.append( exception.toString() + "\n" );
	        exception.printStackTrace();
	     }
	  }
	  public static void main( String args[] )
	  {
	     UDP_1o1_chat c = new UDP_1o1_chat();
	     c.waitForPackets();
	  }
	  class WinListener extends WindowAdapter{
	     public void windowClosing(WindowEvent e){
	        System.exit(0);
	     }
	  }

}
