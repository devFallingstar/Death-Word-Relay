package GUI;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;

public class Register extends JFrame {
	Login myLogin;
	
	private Container cont;
	private JTextField IDtxt = new JTextField();
	private JTextField PWtxt = new JTextField();
	private JTextField NickNametxt = new JTextField();
	
	JButton SubmitBtn = new JButton("Submit");
	JButton CancelBtn = new JButton("Cancel");
	
	JLabel IDlbl = new JLabel("ID : ");
	JLabel PWlbl = new JLabel("PW : ");
	JLabel NickNamelbl = new JLabel("Nick Name : ");
	
	JLabel IDCautionlbl = new JLabel("at least 6 to 12 chracters");
	JLabel IDCautionlbl2 = new JLabel("only english and number accepted");
	JLabel PWCautionlbl = new JLabel("at least 6 to 12 chracters");
	JLabel NickNameCautionlbl = new JLabel("at least 1 to 12 chracters");
	
	public Register(){
		super("Register");
		
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 350, 290);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		cont = this.getContentPane();
		
		SubmitBtn.setBounds(31, 216, 117, 29);
		CancelBtn.setBounds(192, 216, 117, 29);
		NickNamelbl.setBounds(43, 149, 81, 16);
		NickNametxt.setBounds(137, 146, 172, 26);
		IDlbl.setBounds(43, 29, 61, 16);
		IDtxt.setBounds(102, 26, 207, 26);
		PWlbl.setBounds(43, 97, 61, 16);
		PWtxt.setBounds(102, 92, 207, 26);
		IDCautionlbl.setBounds(206, 49, 103, 16);
		IDCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		IDCautionlbl2.setBounds(206, 65, 138, 16);
		IDCautionlbl2.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		PWCautionlbl.setBounds(206, 118, 103, 16);
		PWCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		NickNameCautionlbl.setBounds(206, 172, 103, 16);
		NickNameCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
		
		cont.add(SubmitBtn);
		cont.add(CancelBtn);
		cont.add(NickNamelbl);
		cont.add(NickNametxt);
		cont.add(IDlbl);
		cont.add(IDtxt);
		cont.add(PWlbl);
		cont.add(PWtxt);
		cont.add(IDCautionlbl);
		cont.add(IDCautionlbl2);
		cont.add(PWCautionlbl);
		cont.add(NickNameCautionlbl);
	}
}
