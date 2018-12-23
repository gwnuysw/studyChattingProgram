package chattingJava;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame implements ActionListener{
	private TextField monitor;
	private Button bt; 
	private boolean state=true;//입력진행모드 true 면 한자리 숫자. false 면 한자리 이상
	private int operator;//연산자 구분 번호로 +는1, -는 2, *는 3, /는 4
	private double buffer;//숫자 저장소
	private boolean OperatorStatus=true;//연산자를 버튼을 연속으로 눌렀는지의 여부, 연속으로 누른다면 true

	// 생성자
		public Calculator(){

			super("AWT 계산기");

			Panel main = new Panel();
			main.setLayout(new BorderLayout());

			monitor = new TextField();
			main.add("North",monitor);

			Panel p=new Panel();
			p.setLayout(new GridLayout(4,4));

//	 	버튼 부착하고 이벤트 리스너 연결하기
			String buttonLabel="789+456-123*0C=/"; 
			for(int i=0;i<buttonLabel.length();i++){
				bt=new Button(buttonLabel.substring(i,i+1));
				bt.addActionListener(this);
				p.add(bt);
			}

			main.add("Center",p);
			add(main);

	//종료버튼 달기
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					System.exit(0);
				}
			});

			setSize(180,150);
		}

	// ActionListener 오버라이딩 메서드 actionPerformed()
	public void actionPerformed(ActionEvent ae){

		String val=ae.getActionCommand();//이벤트가 발생한 버튼을 문자열로 리턴
		if(val.equals("C")){
			monitor.setText("");
			buffer=0;
	//숫자버튼을 눌렸는지 검사. 이때 숫자를 연속해서 누르면 십단위가 되고 아님 1단위가 된다.
	//연속해서 눌렀는지를 체크하는 변수 state
		}else if('0' <= val.charAt(0) && val.charAt(0) <= '9'){
			if (state) monitor.setText(val);
			else monitor.setText(monitor.getText()+val);
			state = false;

	//숫자 버튼이 아니라면 연산버튼이므로 연산을 처리하는 메서드 operatorerator(var)를 호출.
		}else
			operatorerator(val);
		}

	// 연산을 처리하는 메서드 operatorerator(var)
		public void operatorerator(String val){

			if(val.equals("+")){
				operator=1;
				if(OperatorStatus) result();
				OperatorStatus=true; 
			}else if(val.equals("-")){
				operator=2;
				if(OperatorStatus) result();
				OperatorStatus=true; 
			}else if(val.equals("*")){
				operator=3;
				if(OperatorStatus) result();
				OperatorStatus=true;
			}else if(val.equals("/")){
				operator=4;
				if(OperatorStatus) result();
				OperatorStatus=true;
			}else {
				result(); 
			}
			buffer= (new Double(monitor.getText())).doubleValue();
			state = true;
		}
	//= 버튼을 누르면 호출되는 결과처리 메서드.
		public void result(){
			double result=0;
			double in=(new Double(monitor.getText())).doubleValue();
			switch(operator){
			case 1:
				result=buffer+in;
				break;
			case 2:
				result=buffer-in;
				break;
			case 3:
				result=buffer*in;
				break;
			case 4:
				result=buffer/in;
				break;
			}
			monitor.setText(result+"");
			OperatorStatus=false;
		}
}
