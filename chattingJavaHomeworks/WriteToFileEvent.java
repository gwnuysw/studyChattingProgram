package chattingJava;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class WriteToFileEvent extends Frame implements ActionListener{
	Label lfile, ldata;
	TextField tfile, tdata;
	Button save, close;
	String filename, data;
	byte buffer[] = new byte[80];
	
	public WriteToFileEvent(String str) {
		super(str);
		setLayout(new FlowLayout());
		lfile = new Label("파일이름을 입력하세요");
		add(lfile);
		tfile = new TextField(20);
		add(tfile);
		ldata = new Label("저장할 데이터를 입력하세요");
		add(ldata);
		tdata = new TextField(20);
		add(tdata);
		save = new Button("저장하기");
		save.addActionListener(this);
		add(save);
		close = new Button("닫기");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(close);
		addWindowListener(new WinListener());
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		filename = tfile.getText();
		data = tdata.getText();
		buffer = data.getBytes();
		
		try
		{
			FileOutputStream fout = new FileOutputStream(filename);
			fout.write(buffer);
		}
		catch(IOException e)
		{
			System.out.println(e.toString());
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
