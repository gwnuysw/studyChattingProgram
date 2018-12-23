package chattingProgram;

import java.awt.*;
import java.awt.event.*;

public class ChatClient extends Frame implements ActionListener
{
   
   public TextField cc_tfLogon; //logon user input window
   private Button cc_btLogon; // logon button
   private Button cc_btEnter; //chat room enter button
   private Button cc_btLogout; // logout button
   private Button cc_openRoom; // create Room with id

   public TextField cc_tfStatus; // system message window
   public TextField cc_tfDate; // logon time window
   public List cc_lstMember; // logon user list
   public List cc_lstRoom; //opened room list

   public static ClientThread cc_thread;
   public static ChatClient client;
   public String msg_logon="";
   public boolean logonstate;
   
   public String roomOwner;
   public ChatClient(String str){
      super(str);
      setLayout(new BorderLayout());
      logonstate = false;
      // set buttons
      Panel bt_panel = new Panel();
      bt_panel.setLayout(new FlowLayout());
      cc_btLogon = new Button("logon");
      cc_btLogon.addActionListener(this);
      bt_panel.add(cc_btLogon);
      
      cc_tfLogon = new TextField(10);
      bt_panel.add(cc_tfLogon);
      
      cc_btEnter = new Button("Enter ChatRoom");
      cc_btEnter.addActionListener(this);
      bt_panel.add(cc_btEnter);
      
      cc_openRoom = new Button("create Room");
      cc_openRoom.addActionListener(this);
      bt_panel.add(cc_openRoom);
      
      cc_btLogout = new Button("logout");
      cc_btLogout.addActionListener(this);
      bt_panel.add(cc_btLogout);
      add("Center", bt_panel);
      
      // print room information using 4 panel objects.
      Panel roompanel = new Panel(); // panel object contains 3 panels
      roompanel.setLayout(new BorderLayout());

      Panel northpanel = new Panel();
      northpanel.setLayout(new FlowLayout());
      cc_tfStatus = new TextField("Enter your id in the text field below",43); 
      													// notify chatroom open status
      cc_tfStatus.setEditable(false);
      northpanel.add(cc_tfStatus);
      
      Panel centerpanel = new Panel();
      centerpanel.setLayout(new FlowLayout());
      centerpanel.add(new Label("log on at : "));
      cc_tfDate = new TextField("you loged on at ...",31);
      cc_tfDate.setEditable(false);
      centerpanel.add(cc_tfDate);

      Panel southpanel = new Panel();
      southpanel.setLayout(new FlowLayout());
      
      southpanel.add(new Label("logon users"));
      cc_lstMember = new List(10);
      southpanel.add(cc_lstMember);
      
      southpanel.add(new Label("opened Rooms"));
      cc_lstRoom = new List(10);
      southpanel.add(cc_lstRoom);

      roompanel.add("North", northpanel);
      roompanel.add("Center", centerpanel);
      roompanel.add("South", southpanel);
      add("North", roompanel);

      // 占싸그울옙 占쌔쏙옙트 占십드에 占쏙옙커占쏙옙占쏙옙 占쏙옙占쌩댐옙 占쌨소듸옙 占쌩곤옙

      addWindowListener(new WinListener());
   }

   class WinListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent we){
    	  msg_logon = cc_tfLogon.getText(); //read id.
    	  
    	  if(logonstate){
              cc_thread.requestLogout();
              System.exit(0); //add logout routine
           }else{
              System.exit(0); //add logout routine
           }

      }
   }

   // detect any button that mouse clicked.
   public void actionPerformed(ActionEvent ae){
	   
      Button b = (Button)ae.getSource();
      roomOwner = cc_lstRoom.getSelectedItem();
      
      if(b.getLabel().equals("logon")){
         //logon routine
         msg_logon = cc_tfLogon.getText(); //read logon id.
         if(!msg_logon.equals("") && logonstate == false){
            cc_thread.requestLogon(msg_logon); //invoke method of ClientThread
         }else{
            MessageBox msgBox = new  MessageBox(this, "logon", "enter your id or you already loged");
            msgBox.show();
         }
      }
      //enter request room
      else if(b.getLabel().equals("Enter ChatRoom")){

         //chatroom open and enter routine
    	 
         msg_logon = cc_tfLogon.getText(); //read id.
         if(logonstate && roomOwner != null){
            cc_thread.requestEnterRoom(roomOwner, msg_logon); //invoke method of ClientThread
         }else{
        	MessageBox msgBox = new  MessageBox(this, "logout", "you didn't logon or didn't select room");
            msgBox.show();
         }
      }else if(b.getLabel().equals("logout")){
    	  
    	  //logout routine
    	  msg_logon = cc_tfLogon.getText(); //read id.
    	  if(logonstate){
              cc_thread.requestLogout();
              cc_tfLogon.setEditable(true);
          }else{
              MessageBox msgBox = new  MessageBox(this, "logout", "you didn't logon.");
              msgBox.show();
          }
      }else if(b.getLabel().equals("create Room")){
    	 //create room
    	  if(logonstate) {
    		  cc_thread.requestCreateRoom();
    	  }
    	  else {
    		  MessageBox msgBox = new  MessageBox(this, "logout", "you didn't logon.");
              msgBox.show();
    	  }
      }
   }

   public static void main(String args[]){
      client = new ChatClient("open chatRom and enter");
      client.setSize(570, 350);
      client.show();
      // create socket and invoke communitaction thread to server
      // if server and host is different system
      // execute : java ChatClient [need hostname and port number.]
      // To DO
      // if server and host is same system
      // execute : java ChatClient [no need hostname and port number.]
      try{
         cc_thread = new ClientThread(client); //localhost constructor
         cc_thread.start(); //start thread of client.
      }catch(Exception e){
         System.out.println(e);
      }
   }
}
