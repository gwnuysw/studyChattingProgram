package chattingProgram;

import java.awt.*;
import java.awt.event.*;

public class DisplayRoom extends Frame implements ActionListener, KeyListener
{
   private Button dr_btClear; //
   private Button dr_btLogout; //logout execute button
   private Button dr_whisper;

   public TextArea dr_taContents; //dialog list
   public List dr_lstMember; //attender

   public TextField dr_tfInput; //dialog input field

   public static ClientThread dr_thread;
   
   public TextField systemGreeting;

   public DisplayRoom(ClientThread client, String title){
      super(title);
      setLayout(new BorderLayout());

      //chat room components
      Panel northpanel = new Panel();
      northpanel.setLayout(new FlowLayout());
      
      systemGreeting = new TextField(20);
      systemGreeting.setText("");
      northpanel.add(systemGreeting);
      
      dr_btClear = new Button("Clear screen"); 
      dr_btClear.addActionListener(this);
      northpanel.add(dr_btClear);
   
      dr_btLogout = new Button("Exit");
      dr_btLogout.addActionListener(this);
      northpanel.add(dr_btLogout);

      Panel centerpanel = new Panel();
      centerpanel.setLayout(new FlowLayout());
      dr_taContents = new TextArea(10, 27);
      dr_taContents.setEditable(false);
      centerpanel.add(dr_taContents);
     
      dr_lstMember = new List(10);
      centerpanel.add(dr_lstMember);

      Panel southpanel = new Panel();
      southpanel.setLayout(new FlowLayout());
      dr_tfInput = new TextField(41);
      dr_tfInput.addKeyListener(this);
      southpanel.add(dr_tfInput);

      dr_whisper = new Button("Whisper");
      dr_whisper.addActionListener(this);
      southpanel.add(dr_whisper);
      
      add("North", northpanel);
      add("Center", centerpanel);
      add("South", southpanel);

      dr_thread = client; //connect ClientThread.

      //focus on input text field

      addWindowListener(new WinListener());

   }

   class WinListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent we){
    	 dr_thread.requestExitRoom();
    	 dr_thread.requestLogout();
         System.exit(0); // change to logout routine.
      }
   }

   //screen clear, logout event.
   public void actionPerformed(ActionEvent ae){
      Button b = (Button)ae.getSource();
      String Wid = dr_lstMember.getSelectedItem();
      if(b.getLabel().equals("Clear screen")){
    	  dr_taContents.setText("");
      //clear screen routine

      }else if(b.getLabel().equals("Exit")){
    	  dr_thread.requestExitRoom();
      //Exit routine
      }
      else if(b.getLabel().equals("Whisper") && Wid != null) {
    	  String words = dr_tfInput.getText();
    	  dr_thread.requestWhisper(words,Wid);
      }
      else if(b.getLabel().equals("Whisper") && Wid == null) {
    	  //no select
    	  MessageBox msgBox = new MessageBox(this, "ok", "who do you want to whisper? select a user on the user list");
    	  msgBox.setVisible(true);
      }
   }

   //send to server.
   public void keyPressed(KeyEvent ke){
      if(ke.getKeyChar() == KeyEvent.VK_ENTER){
         String words = dr_tfInput.getText(); // get message form window.
         dr_thread.requestSendWords(words); // send message to all in the room.
      }
   }

   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}

}
