package chattingProgram;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread 
{
   private Socket st_sock;
   private DataInputStream st_in;
   private DataOutputStream st_out;
   private StringBuffer st_buffer;
   /* save logon users */
   private static Hashtable<String,ServerThread> logonHash; 
   private static Vector<String> logonVector;
   /* save chat room users */
   private  Hashtable<String,ServerThread> roomHash; 
   private  Vector<String> roomVector;
   
   //it is for room list, separate rooms
   private static Hashtable<String, Hashtable> ownerHash;
   private static Hashtable<String, Vector> ownerVectorHash;
   private static Vector<String> ownerVector;
   
   private static int isOpenRoom = 0; // chat room not open

   private static final String SEPARATOR = "|"; // message separator
   private static final String DELIMETER = "`"; // small message separator
   private static Date starttime;  	// logon time

   public String st_ID; 			// ID save

   //definition of message packet code and data

   //receiving message code from client
   private static final int REQ_LOGON = 1001;
   private static final int REQ_ENTERROOM = 1011;
   private static final int REQ_CREATEROOM = 1012;
   private static final int REQ_SENDWORDS = 1021;
   private static final int REQ_LOGOUT = 1031;
   private static final int REQ_QUITROOM = 1041;
   private static final int REQ_WHISPER = 1051;
   private static final int REQ_DELETEROOM = 1061;

   //sending message code to client
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
   private static final int MDY_ROOMUSERS = 4002;
   private static final int CHATROOM = 4003;

   //broad cast case
   private static final int WAITROOM = 4001;
   private static final int OPENROOMS = 5001;
   private static final int INTHEROOM = 6001;
   
   static{	
      logonHash = new Hashtable<String,ServerThread>(ChatServer.cs_maxclient);
      logonVector = new Vector<String>(ChatServer.cs_maxclient); 
      ownerHash = new Hashtable<String, Hashtable>(ChatServer.cs_maxclient);
      ownerVectorHash = new Hashtable<String, Vector>(ChatServer.cs_maxclient); 
      ownerVector = new Vector<String>(ChatServer.cs_maxclient);
   }

   public ServerThread(Socket sock){
      try{
         st_sock = sock;
         st_in = new DataInputStream(sock.getInputStream()); 
         st_out = new DataOutputStream(sock.getOutputStream());
         st_buffer = new StringBuffer(2048);
      }catch(IOException e){
         System.out.println(e);
      }
   }

   public void run(){
      try{
         while(true){
            String recvData = st_in.readUTF();
            StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
            int command = Integer.parseInt(st.nextToken());
            switch(command){

               //logon request message PACKET : REQ_LOGON|ID
               case REQ_LOGON:{
                  int result;
                  String id = st.nextToken(); //ID of client.
                  result = addUser(id, this);
                  st_buffer.setLength(0);
                  if(result ==0){  //connection permitted
                	 starttime = new Date();
                     st_buffer.append(YES_LOGON);
                     st_buffer.append(SEPARATOR);
                     st_buffer.append(starttime);
                     st_buffer.append(SEPARATOR);
                     send(st_buffer.toString());
                     
                     //send logoned users id
                     modifyWaitUsers();
                  }else{  //connection denied
                     st_buffer.append(NO_LOGON);  // NO_LOGON|errCode
                     st_buffer.append(SEPARATOR);
                     st_buffer.append(result); //send cause of connection deny
                     send(st_buffer.toString());
                  }
                  modifyRooms();
                  break;
               }

               //chat room open request message PACKET : REQ_ENTERROOM|roomOwner|id
               case REQ_ENTERROOM:{
                  st_buffer.setLength(0);
                  
                  String owner = st.nextToken();
                  String id = st.nextToken(); //get client id.
                  
                  if(checkUserID(id) == null){

                  // NO_ENTERROOM PACKET : NO_ENTERROOM|errCode
                     st_buffer.append(NO_ENTERROOM);
                     st_buffer.append(SEPARATOR);
                     st_buffer.append(MSG_CANNOTOPEN);
                     send(st_buffer.toString());  // NO_ENTERROOM packet send client.
                     break;
                  }

                  roomVector = ownerVectorHash.get(owner);
                  roomHash = ownerHash.get(owner);
                  
                  ownerVectorHash.remove(owner);
                  ownerHash.remove(owner);
                  
                  roomVector.addElement(id);  //add user id
                  roomHash.put(id, this); // store thread that contain userid and thread
                  
                  ownerVectorHash.put(owner, roomVector);
                  ownerHash.put(owner, roomHash);
                  
                  if(isOpenRoom == 0){  // set room open time
                     isOpenRoom = 1;
                  //   starttime = new Date();
                  }

                  // YES_ENTERROOM PACKET : YES_ENTERROOM
                  st_buffer.append(YES_ENTERROOM); 
                  send(st_buffer.toString()); // YES_ENTERROOM packet send.
                  modifyRoomUsers(owner);
                  break;
               }

               // send message PACKET : REQ_SENDWORDS|ID|message
               case REQ_SENDWORDS:{
                  st_buffer.setLength(0);
                  st_buffer.append(YES_SENDWORDS);
                  st_buffer.append(SEPARATOR);
                  String id = st.nextToken(); // sender id.
                  st_buffer.append(id);
                  st_buffer.append(SEPARATOR);
                  try{
                     String data = st.nextToken(); // message to send.
                     st_buffer.append(data);
                  }catch(NoSuchElementException e){}
                  broadcast(st_buffer.toString(),INTHEROOM); // YES_SENDWORDS in the room users.
                  break;
               }

               // LOGOUT request message 
               // PACKET : YES_LOGOUT|logoutID|other user's ids
               case REQ_LOGOUT:{
                  String id = st.nextToken(); //ID of client.
                  
                  if(deleteUser(id, this) == 0) {
                	  
                	  st_buffer.setLength(0);
                	  st_buffer.append(YES_LOGOUT);
                	  st_buffer.append(SEPARATOR);
                	  st_buffer.append(id);
                	  send(st_buffer.toString());
                	  
                	  modifyWaitUsers();
                  }
                  else {
                	  //logout error
                  }
                  break;
               }

               // request quit room PACKET : YES_QUITROOM
               case REQ_QUITROOM:{
            	  String owner = st.nextToken();
            	  String id = st.nextToken();
            	  
            	  if(deleteRoomUser(id, this) == 0) {
            		  st_buffer.setLength(0);
            		  st_buffer.append(YES_QUITROOM);
            		  st_buffer.append(SEPARATOR);
            		  st_buffer.append(id);
            		  send(st_buffer.toString());
             	  
            		  roomVector = ownerVectorHash.get(owner);
                      roomHash = ownerHash.get(owner);
                      
                      ownerVectorHash.remove(owner);
                      ownerHash.remove(owner);
                      
                      roomVector.remove(id);  //add user id
                      roomHash.remove(id, this); // store thread that contain userid and thread
                      
                      ownerVectorHash.put(owner, roomVector);
                      ownerHash.put(owner, roomHash);
                      
            		  modifyRoomUsers(owner);
            	  }
            	  else {
            		  //quit room error
            	  }
                  break;
               }
               //SEND_WHISPER|wid|message
               case REQ_WHISPER:{
            	   String id = st.nextToken();
            	   
            	   String Wid = st.nextToken();
            	   
            	   String message = st.nextToken();
            	   
            	   st_buffer.setLength(0);
            	   st_buffer.append(SEND_WHISPER);
            	   st_buffer.append(SEPARATOR);
            	   st_buffer.append(id);
            	   st_buffer.append(SEPARATOR);
            	   st_buffer.append(message);
            	   whisperSend(st_buffer.toString(),Wid);
            	   break;
               }
               case REQ_CREATEROOM:{
            	   String owner = st.nextToken();
            	   st_buffer.setLength(0); 
            	   if(ownerVector.contains(owner)) {
            		   st_buffer.append(NO_CREATEROOM);
            		   st_buffer.append(SEPARATOR);
            		   st_buffer.append(owner);
            		   
            		   send(st_buffer.toString());
            	   }
            	   else {
            		   ownerVector.add(owner);
            	   
	            	   st_buffer.append(YES_CREATEROOM);
	            	   st_buffer.append(SEPARATOR);
	            	   st_buffer.append(owner);
	            	   
	            	   send(st_buffer.toString());
	            	   
	            	   ownerHash.put(owner, new Hashtable<String, ServerThread>(ChatServer.cs_maxclient));
	            	   ownerVectorHash.put(owner, new Vector<String>(ChatServer.cs_maxclient));
	            	   modifyRooms();
            	   }
            	   
            	   break;
               }
               case REQ_DELETEROOM:{
            	   String owner = st.nextToken();
            	   String id = st.nextToken();
            	   
            	   if(owner.equals(id)) {
            		    ownerVector.removeElement(owner);
            		    ownerHash.remove(owner);
            		    ownerVectorHash.remove(owner);
            		    
            		    modifyRooms();
            	   }
            	   else {
            		   st_buffer.setLength(0);
            		   st_buffer.append(NO_DELETEROOM);
            		   st_buffer.append(SEPARATOR);
            		   send(st_buffer.toString());
            	   }
            	   
            	   break;
               }

            } // switch expandable

            Thread.sleep(100);
         } //while 占쏙옙占쏙옙

      }catch(NullPointerException e){ // 占싸그아울옙占쏙옙 st_in占쏙옙 占쏙옙 占쏙옙占쌤몌옙 占쌩삼옙占싹므뤄옙
      }catch(InterruptedException e){
      }catch(IOException e){
      }
   }
   // release all resource.
   public void release(){}

   /*save client id and thread in hash table. */
    private static synchronized int addUser(String id, ServerThread client){
      if(checkUserID(id) != null){
         return MSG_ALREADYUSER;
      }  
      if(logonHash.size() >= ChatServer.cs_maxclient){
         return MSG_SERVERFULL;
      }
      logonVector.addElement(id);  //add user id
      logonHash.put(id, client); //save userid and thread.
      client.st_ID = id;
      return 0; // connection success, chat room open
   }
    private static synchronized int deleteUser(String id, ServerThread client) {
    	if(checkUserID(id) != null) {
    		logonVector.removeElement(id);
    		logonHash.remove(id, client);
    	}
    	return 0;//logout success
    }
    private synchronized int deleteRoomUser(String id, ServerThread client) {
    	roomVector.removeElement(id);
    	roomHash.remove(id, client);
    	return 0;//logout success
    }
   /* check user id if it uses by others already if it returns null the id can use. */
   private static ServerThread checkUserID(String id){
      ServerThread alreadyClient = null;
      alreadyClient = (ServerThread) logonHash.get(id);
      return alreadyClient;
   }

   // get users in the room

   private String getRoomUsers(String owner){
      StringBuffer id = new StringBuffer();
      String ids;
      Enumeration<String> enu = ownerVectorHash.get(owner).elements();
      while(enu.hasMoreElements()){
         id.append(enu.nextElement());
         id.append(DELIMETER); 
      }
      try{
         ids = new String(id);
         ids = ids.substring(0, ids.length()-1); // separate users using "`" but delete last separator.
      }catch(StringIndexOutOfBoundsException e){
         return "";
      }
      return ids;
   }

   // broad cast to all user.
   public synchronized void broadcast(String sendData, int room) throws IOException{
	      ServerThread client;
	      Enumeration<String> enu;// = roomVector.elements();
	      if(room == WAITROOM) {
	    	  enu = logonVector.elements();
	    	   while(enu.hasMoreElements()){
		         client = (ServerThread) logonHash.get(enu.nextElement());
		         client.send(sendData);
		      }
	      }
	      else if(room == INTHEROOM){
	    	  enu = roomVector.elements();
	    	  while(enu.hasMoreElements()) {
	    		  client = (ServerThread)roomHash.get(enu.nextElement());
	    		  client.send(sendData);
	    	  }
	      }
  }
   // get ids logon users.
   private String getUsers(){
      StringBuffer id = new StringBuffer();
      String ids;
      Enumeration<String> enu = logonVector.elements();
      while(enu.hasMoreElements()){
         id.append(enu.nextElement());
         id.append(DELIMETER); 
      }
      try{
         ids = new String(id);  // 占쏙옙占쌘울옙占쏙옙 占쏙옙환占싼댐옙.
         ids = ids.substring(0, ids.length()-1); // 占쏙옙占쏙옙占쏙옙 "`"占쏙옙 占쏙옙占쏙옙占싼댐옙.
      }catch(StringIndexOutOfBoundsException e){
         return "";
      }
      return ids;
   }
   //get owners opened rooms
   private String getRooms() {
	   StringBuffer id = new StringBuffer();
	   String ids;
	   if(ownerVector.isEmpty() == false) {
		   Enumeration<String> enu = ownerVector.elements();
		   while(enu.hasMoreElements()) {
			   id.append(enu.nextElement());
			   id.append(DELIMETER);
		   }
		   try {
			   ids = new String(id);  // convert to string.
	         ids = ids.substring(0, ids.length()-1); // delete "`" form end of it.
	      }catch(StringIndexOutOfBoundsException e){
	         return "";
	      }
	      return ids;
	   }
	   else {
		   return "NO_ROOMOPEN";
	   }
	   
   }
  private void modifyRooms() throws IOException {
	  String rooms = getRooms();
	  st_buffer.setLength(0);
	  st_buffer.append(EXSIST_ROOMIDS);
	  st_buffer.append(SEPARATOR);
	  st_buffer.append(rooms);
	  broadcast(st_buffer.toString(), WAITROOM);
  }
  private void whisperSend(String sendData, String wid) throws IOException{
	  ServerThread client;
      
      client = (ServerThread) logonHash.get(wid);
         
      client.send(sendData);
  }
 private void modifyWaitUsers()throws IOException{
	   String ids = getUsers();
	   st_buffer.setLength(0);
	   st_buffer.append(EXIST_USERIDS);
	   st_buffer.append(SEPARATOR);
	   st_buffer.append(ids);
	   broadcast(st_buffer.toString(), WAITROOM);
 }
 private void modifyRoomUsers(String owner) throws IOException{
	   String ids = getRoomUsers(owner);
	   st_buffer.setLength(0);
	   st_buffer.append(MDY_USERIDS);
	   st_buffer.append(SEPARATOR);
	   st_buffer.append(ids);
	   broadcast(st_buffer.toString(), INTHEROOM);
 }
   //send message.
   public void send(String sendData) throws IOException{
      synchronized(st_out){
    	  System.out.println("send "+sendData);
         st_out.writeUTF(sendData);
         st_out.flush();
      }
   }
}   
