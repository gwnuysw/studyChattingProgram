package chattingJava;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class FileText extends Frame implements ActionListener
{
	private TextField enter;
	private TextArea output;
	private TextArea outputSouth;
	
	public FileText()
	{
		super("File Class test");
		
		enter = new TextField("type file and directory name");
		enter.addActionListener(this);
		
		output = new TextArea();
		outputSouth = new TextArea();
		add(enter, BorderLayout.NORTH);
		add(output, BorderLayout.CENTER);
		add(outputSouth, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		
		setSize(400, 400);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		File name = new File(e.getActionCommand());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd (E) hh mm");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		if(name.exists())
		{
			long time = name.lastModified();
			
			output.setText(name.getName() + "is exsist.\n"+ (name.isFile() ? "It is a file\n" : "It is not a file.\n")+(name.isDirectory() ? "It is a Directory.\n" : "It is not a directory.\n") +(name.isAbsolute() ? "It is a Absolute Path.\n" : "It is not a Absolute Path.\n")+  "last Modified Date : " + sdf.format(new Date(time)) +"\nFile Length : " + name.length() + "\nFile Path : " + name.getPath() +"\nFile Absolute Path : " + name.getAbsolutePath() +"\nParent Directory : " + name.getParent()); 

			if(name.isFile())
			{
				try
				{
					RandomAccessFile r = new RandomAccessFile(name, "r");
					StringBuffer buf = new StringBuffer();
					String text;
					
					outputSouth.append("CanonicalPath : "+name.getCanonicalPath());
					
					outputSouth.append("\n\n");
					
					while((text = r.readLine()) != null)
						buf.append(text + "\n");
					
					outputSouth.append(buf.toString());
				}
				catch (IOException e2) {}
				
			}
			else if(name.isDirectory())
			{
				String directory[] = name.list();
				
				outputSouth.append("\n\nDirectory content :\n");
				
				for(int i=0; i<directory.length; i++)
					outputSouth.append(directory[i] + "\n");
			}
		}
		else
		{
			output.setText(e.getActionCommand() + " doesn't exsist\n");
		}
	}
	
	public static void main(String[] args)
	{
		FileText f = new FileText();
	}
	
	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);
		}
	}
}