package chattingProgram;

import java.awt.*;
import java.awt.event.*;

class MessageBox extends Dialog implements ActionListener
{
   public MessageBox(Frame parent, String title, String message){
      super(parent, title, false); // modal Dialog ����
      setLayout(new BorderLayout());
      Label lbl = new Label(message);
      add("Center",lbl);
      Button bt = new Button("OK");
      bt.addActionListener(this);
      add("South", bt);
      pack();
   }
   public void actionPerformed(ActionEvent ae){
      setVisible(false); // OK ��ư�� ������ �޽��� �ڽ��� �����Ѵ�.
   }

}
