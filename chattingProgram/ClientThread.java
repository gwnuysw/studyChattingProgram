package chattingProgram;

import java.io.*;
import java.util.*;
import java.net.*;

public class ClientThread extends Thread
{
   
   private ChatClient  ct_client; // ChatClient object
   private Socket ct_sock; // client socket
   private DataInputStream ct_in; //input stream
   private DataOutputStream ct_out; //output stream
   private StringBuffer ct_buffer; //buffer
   private Thread thisThread;
   private DisplayRoom room;

   private static final String SEPARATOR = "|";
   private static final String DELIMETER = "`";

   //definition of message packet code and data

   //message code to server
   private static final int REQ_LOGON = 1001;
   private static final int REQ_ENTERROOM = 1011;
   private static final int REQ_CREATEROOM = 1012;
   private static final int REQ_SENDWORDS = 1021;
   private static final int REQ_LOGOUT = 1031;
   private static final int REQ_QUITROOM = 1041;
   private static final int REQ_WHISPER = 1051;
   private static final int REQ_DELETEROOM = 1061;

   //receiving message code from server
   private static final int YES_LOGON = 2001;
   private static final int NO_LOGON = 2002;
   
   private static final int YES_ENTERROOM = 2011;
   private static final int NO_ENTERROOM = 2012;
    private static final int YES_QUITROOM = 2041;
    
   private static final int MDY_USERIDS = 2013;
   private static final int EXIST_USERIDS = 2014;
    private static final int EXSIST_ROOMIDS = 2015;
    
   private static final int YES_SENDWORDS = 2021;
   private static final int NO_SENDWORDS = 2022;
   
   private static final int YES_LOGOUT = 2031;
   private static final int NO_LOGOUT = 2032;

   private static final int SEND_WHISPER = 2051;
 
   private static final int YES_CREATEROOM = 2061;
   private static final int NO_DELETEROOM = 2062;
   private static final int NO_CREATEROOM = 2063;
   
   //error message code
   private static final int MSG_ALREADYUSER = 3001;
   private static final int MSG_SERVERFULL = 3002;
   private static final int MSG_CANNOTOPEN = 3011;

 //broad cast case
   private static final int WAITROOM = 4001;
   private static final int OPENROOMS = 5001;
   private static final int INTHEROOM = 6001;
   
   private static MessageBox msgBox, logonbox;

   /* 占쏙옙占쏙옙호占쏙옙트占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙
          占쏙옙占쏙옙 : java ChatClient 호占쏙옙트占싱몌옙 占쏙옙트占쏙옙호 
   	  To DO .....				*/

   // 占쏙옙占쏙옙호占쏙옙트占쏙옙占쏙옙 占쏙옙占쏙옙歐占� 占쏙옙占싹울옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙
   // 占쏙옙占쏙옙占쏙옙 클占쏙옙占싱억옙트占쏙옙 占쏙옙占쏙옙 占시쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙磯占�. 
   public ClientThread(ChatClient client) {
      try{
         ct_sock = new Socket(InetAddress.getLocalHost(), 2777);
         ct_in = new DataInputStream(ct_sock.getInputStream());
         ct_out = new DataOutputStream(ct_sock.getOutputStream());
         ct_buffer = new StringBuffer(4096);
         thisThread = this;
         ct_client = client; // 占쏙옙체占쏙옙占쏙옙占쏙옙 占쌀댐옙
      }catch(IOException e){
         MessageBoxLess msgout = new MessageBoxLess(client, "connectrion error", "cannot connet to server.");
         msgout.show();
      }
   }
   public void run(){
      try{
         Thread currThread = Thread.currentThread();
         while(currThread == thisThread){ //exit in LOG_OFF by thisThread=null;
        	
        	String recvData = ct_in.readUTF();
            System.out.println("recvData "+recvData);
            StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
            int command = Integer.parseInt(st.nextToken());
            switch(command){
               // logon success message  PACKET : YES_LOGON|logon time|ID1`ID2`ID3...
               case YES_LOGON:{
                  logonbox.dispose();
                  ct_client.cc_tfStatus.setText("logon success.");
                  String date = st.nextToken(); // get message
                  ct_client.cc_tfDate.setText(date);
                  ct_client.cc_tfLogon.setEditable(false);
                  ct_client.logonstate = true;
                  break;
               }
               case EXIST_USERIDS:{
            	   String ids = st.nextToken(); //get exsist user ids
                   StringTokenizer users = new StringTokenizer(ids, DELIMETER);
                   ct_client.cc_lstMember.removeAll();
                   while(users.hasMoreTokens()){
                      ct_client.cc_lstMember.add(users.nextToken());
                      System.out.println(recvData);
                   }
            	   break;
               }
               case EXSIST_ROOMIDS:{
            	   String ids = st.nextToken(); //get exsist user ids
            	   if(ids.equals("NO_ROOMOPEN")){
            		   ct_client.cc_lstRoom.removeAll();
            	   }
            	   else {
            		   StringTokenizer users = new StringTokenizer(ids, DELIMETER);
	                   ct_client.cc_lstRoom.removeAll();
	                   while(users.hasMoreTokens()){
	                      ct_client.cc_lstRoom.add(users.nextToken());
	                      System.out.println(recvData);
	                   }
            	   }
                   
                   break;
               }
               // no logon
               // PACKET : NO_LOGON|errCode
               case NO_LOGON:{
                  int errcode = Integer.parseInt(st.nextToken());
                  if(errcode == MSG_ALREADYUSER){
                     logonbox.dispose();
                     msgBox = new MessageBox(ct_client, "logon", "your id already exsist.");
                     msgBox.show();
                  }else if(errcode == MSG_SERVERFULL){
                     logonbox.dispose();
                     msgBox = new MessageBox(ct_client, "logon", "full ChatRoom.");
                     msgBox.show();
                  }
                  break;
               }
               // chat room enter success message  PACKET : YES_ENTERROOM
               case YES_ENTERROOM:{
                  ct_client.dispose(); //erase logon window.
                  room = new DisplayRoom(this, "ChatRoom");
                  room.pack();
                  room.show(); //show chat room.
                  room.systemGreeting.setText("Hi "+ct_client.msg_logon+" !" + " this is room "+ct_client.roomOwner);
                  room.systemGreeting.setEditable(false);
                  break;
               }
               // denied enter room  PACKET : NO_ENTERROOM|errCode
               case NO_ENTERROOM:{
                  int roomerrcode = Integer.parseInt(st.nextToken());
                  if(roomerrcode == MSG_CANNOTOPEN){
                     msgBox = new MessageBox(ct_client, "enter ChatRoom", "you are not logoned.");
                     msgBox.show();
                  }   
                  break;
               }
               // chat room user list.
               // PACKET : MDY_USERIDS|id1'id2'id3.....
               case MDY_USERIDS:{
                  room.dr_lstMember.removeAll(); //remove all id list.
                  String ids = st.nextToken(); //chatting user list...트
                  StringTokenizer roomusers = new StringTokenizer(ids, DELIMETER);
                  while(roomusers.hasMoreTokens()){
                     room.dr_lstMember.add(roomusers.nextToken());
                     System.out.println(recvData);
                  }
                  break;
               }
               // 占쏙옙占쏙옙 占쌨쏙옙占쏙옙 占쏙옙占�  PACKET : YES_SENDWORDS|ID|占쏙옙화占쏙옙
               case YES_SENDWORDS:{
                  String id = st.nextToken(); // 占쏙옙화占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 ID占쏙옙 占쏙옙占싼댐옙.
                  try{
                     String data = st.nextToken();
                     room.dr_taContents.append(id+" : "+data+"\n");
                  }catch(NoSuchElementException e){}
                  room.dr_tfInput.setText(""); // erase input textfield.
                  break;
               }
               // LOGOUT message routine
               // PACKET : YES_LOGOUT|Logout id| Exsists id1, id2,....
               case YES_LOGOUT:{
            	  logonbox.dispose();
            	  ct_client.cc_tfStatus.setText("logout success.");
            	  ct_client.cc_lstMember.removeAll();
            	  ct_client.cc_lstRoom.removeAll();
            	  ct_client.logonstate = false;
            	  ct_client.cc_tfLogon.setText("");
                  break;
               }
               // exit room ok PACKET : YES_QUITROOM
               case YES_QUITROOM:{
            	   ct_client.setVisible(true);
            	   room.dispose();
                  break;
               }
               case SEND_WHISPER:{
            	   String id = st.nextToken();
            	   try {
            		   String data = st.nextToken();
            		   room.dr_taContents.append("wispered form "+id+" : "+data+"\n");
            	   }catch(NoSuchElementException e) {
            		   
            	   }
            	   break;
               }
               case NO_DELETEROOM:{
            	   msgBox = new MessageBox(ct_client, "you can't enter ChatRoom", "you are not room owner.");
                   msgBox.setVisible(true);
                   break;
               }
               case NO_CREATEROOM:{
            	   msgBox = new MessageBox(ct_client, "error","Room aleady exsist");
            	   msgBox.setVisible(true);
            	   break;
               }
               
            } // switch 占쏙옙占쏙옙
            Thread.sleep(200);
         } // while 占쏙옙占쏙옙(占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙)
      }catch(InterruptedException e){
         System.out.println(e);
         release();
      }catch(IOException e){
         System.out.println(e);
         release();
      }
   }
   // 占쏙옙트占쏙옙크 占쌘울옙占쏙옙 占쏙옙占쏙옙占싼댐옙.
   public void release(){ };
   
   public void requestExitRoom() {
	try {
	   ct_buffer.setLength(0);
	   ct_buffer.append(REQ_QUITROOM);
	   ct_buffer.append(SEPARATOR);
	   ct_buffer.append(ct_client.roomOwner);
	   ct_buffer.append(SEPARATOR);
	   ct_buffer.append(ct_client.msg_logon);
	   send(ct_buffer.toString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println(e);
	}
   }
   // Logon 占쏙옙킷(REQ_LOGON|ID)占쏙옙 占쏙옙占쏙옙占싹곤옙 占쏙옙占쏙옙占싼댐옙.
   public void requestLogon(String id) {
      try{
         logonbox = new MessageBox(ct_client, "logon", "wait for connect to server.");
         logonbox.show();
         ct_buffer.setLength(0);   // Logon 占쏙옙킷占쏙옙 占쏙옙占쏙옙占싼댐옙.
         ct_buffer.append(REQ_LOGON);
         ct_buffer.append(SEPARATOR);
         ct_buffer.append(id);
         send(ct_buffer.toString());   // Logon 占쏙옙킷占쏙옙 占쏙옙占쏙옙占싼댐옙.
      }catch(IOException e){
         System.out.println(e);
      }
   }
   // EnterRoom packet(REQ_ENTERROOM|roomOwner|ID)to packet.
   public void requestEnterRoom(String roomOwner, String id) {
      try{
         ct_buffer.setLength(0);   // EnterRoom make.
         ct_buffer.append(REQ_ENTERROOM);
         ct_buffer.append(SEPARATOR);
         ct_buffer.append(roomOwner);
         ct_buffer.append(SEPARATOR);
         ct_buffer.append(id);
         send(ct_buffer.toString());   //send EnterRoom packet to server.
      }catch(IOException e){
         System.out.println(e);
      }
   }
   // SendWords packet(REQ_SENDWORDS|ID|message) to server.
   public void requestSendWords(String words) {
      try{
         ct_buffer.setLength(0);   // SendWords 占쏙옙킷占쏙옙 占쏙옙占쏙옙占싼댐옙.
         ct_buffer.append(REQ_SENDWORDS);
         ct_buffer.append(SEPARATOR);
         ct_buffer.append(ct_client.msg_logon);
         ct_buffer.append(SEPARATOR);
         ct_buffer.append(words);
         send(ct_buffer.toString());   // SendWords 占쏙옙킷占쏙옙 占쏙옙占쏙옙占싼댐옙.
      }catch(IOException e){
         System.out.println(e);
      }
   }
   //REQ_WHISPER|id|Wid|message
   public void requestWhisper(String words, String WID) {
		try {
		   ct_buffer.setLength(0);
		   ct_buffer.append(REQ_WHISPER);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(ct_client.msg_logon);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(WID);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(words);
		   send(ct_buffer.toString());
		   room.dr_tfInput.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   public void requestLogout() {
	   try{
		   
	         logonbox = new MessageBox(ct_client, "logout", "Requesting logout...");
	         logonbox.show();
	         ct_buffer.setLength(0);   //make logout packet.
	         ct_buffer.append(REQ_LOGOUT);
	         ct_buffer.append(SEPARATOR);
	         ct_buffer.append(ct_client.msg_logon);
	         send(ct_buffer.toString());   //send logout packet.
        	 if(ct_client.cc_lstRoom.getRows() > 0 ) {
        		 //requestDeleteRoom();
   
		         String[] rooms = ct_client.cc_lstRoom.getItems();
		         
		         boolean flag = false;
		         for(int item = 0; item < rooms.length; item++) {
		        	 if(rooms[item].equals(ct_client.msg_logon)) {
		        		 flag = rooms[item].equals(ct_client.msg_logon);
		        		 requestDeleteRoom();
		        	 }
		         }
	         }
	         
	      }catch(IOException e){
	         System.out.println(e);
	      }
   }
   public void requestCreateRoom() {
	   try {
		   ct_buffer.setLength(0);
		   ct_buffer.append(REQ_CREATEROOM);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(ct_client.msg_logon);
		   send(ct_buffer.toString());
	   }catch(IOException e) {
		   System.out.println(e);
	   }
   }
   public void requestDeleteRoom() {
	   try {
		   ct_buffer.setLength(0);
		   ct_buffer.append(REQ_DELETEROOM);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(ct_client.msg_logon);
		   ct_buffer.append(SEPARATOR);
		   ct_buffer.append(ct_client.msg_logon);
		   
		   send(ct_buffer.toString());
	   }catch(IOException e) {
		   System.out.println(e);
	   }
   }
   // send packet to server.
   private void send(String sendData) throws IOException {
	   System.out.println("send "+sendData);
      ct_out.writeUTF(sendData);
      ct_out.flush();
   }
}