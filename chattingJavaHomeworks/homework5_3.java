package chattingJava;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.net.*;
import java.io.*;

public class homework5_3 extends Frame implements ActionListener
{
	private TextField enter;
	private TextArea contents1, contents2;
	
	public homework5_3()
	{
		super("Read Host File");
		setLayout(new BorderLayout());
		
		enter = new TextField("type on URL.");
		enter.addActionListener(this);
		add(enter, BorderLayout.NORTH);
		
		contents1 = new TextArea("");
		add(contents1, BorderLayout.CENTER);
		
		contents2 = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(contents2, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		setSize(350, 300);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		URL url;
		InputStream is;
		BufferedReader input;
		String line;
		StringBuffer buffer = new StringBuffer();
		String location = e.getActionCommand();
		
		try
		{
			url = new URL(location);
			String firstText="";
			firstText += url.getProtocol()+"\n"+url.getHost()+"\n"+url.getPort()+"\n"+url.hashCode();
			contents1.setText(firstText);
			
			is = url.openStream();
			input = new BufferedReader(new InputStreamReader(is));
			
			contents2.setText("READING....");
			
			URLConnection o = url.openConnection();
			if(o.getContentType().contains("text")) {
				while((line = input.readLine()) != null)
					buffer.append(line).append('\n');

				contents2.setText(buffer.toString());
			}
			else if (o.getContentType().contains("image")) {
				contents2.setText(o.getContentType());
			}
			else if(o.getContentType().contains("video")){
				contents2.setText(o.getContentType());
			}
			else if(o.getContentType().contains("audio")) {
				contents2.setText(o.getContentType());
			}
			else {
				contents2.setText(o.getContentType());
			}
			input.close();
		}
		catch (MalformedURLException e2)
		{
			contents2.setText("URL Malformed.");
		}
		catch (IOException e2)
		{
			contents2.setText(e2.toString());
		}
		catch (Exception e2)
		{
			contents2.setText("Only read file of host computer.");
		}
	}
	
	public static void main(String[] args)
	{
		homework5_3 read = new homework5_3();
	}
	
	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);
		}
	}
}
