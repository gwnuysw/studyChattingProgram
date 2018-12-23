package chattingJava;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class homework4_2 extends Frame implements ActionListener
 {
	Label lfile, ldata;
	TextField tfile;
	TextArea txarea;
	Button cpy, prt,exit;
	String filename1;
	byte buffer[] = new byte[80];
	
	public homework4_2(String str)
	{
		super(str);
		this.setLayout(new FlowLayout());

		tfile = new TextField(20);
		add(tfile);
		
		prt = new Button("RUN");
		prt.addActionListener(this);
		add(prt);
		
		txarea = new TextArea(10, 50);
		add(txarea);
		addWindowListener(new WinListener());
		
		exit = new Button("CLOSE");
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		add(exit);
		
		addWindowListener(new WinListener());
	}
	
	public static void main(String[] args)
	{
		homework4_2 text = new homework4_2("HOMEWORK4_2");
		text.setSize(425,300); 
		text.setVisible(true);
	}
	public void actionPerformed(ActionEvent ae)
	{
		filename1 = tfile.getText(); 
		
		String buf;
		FileReader fin=null;
		FileWriter fout=null;
		
		try
		{
			fin = new FileReader(filename1);
			fout = new FileWriter("numbered_"+filename1);
		}catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		LineNumberReader read = new LineNumberReader(fin);
		PrintWriter write = new PrintWriter(fout);
		int num;
		
		while(true)
		{
			try
			{
				buf=read.readLine();
				num = read.getLineNumber();
				if(buf == null)
					break;
			}catch (IOException e)
			{
				System.out.println(e);
				break;
			}
			
			buf = num + " : " + buf;
			txarea.setText(txarea.getText()+"\n"+buf);
			write.println(buf);
		}
		
		try
		{
			fin.close();
			fout.close();
		} catch (IOException e)
		{
			System.out.println(e);
		}
	}

	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);
		}
	}

}
