package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import RoomClient.*;

public class Login extends JFrame {
	Client clnt;
	
	JLabel IDlbl = new JLabel("ID :");
	JLabel PWlbl = new JLabel("PW :");
	JTextField IDtxt = new JTextField();
	JTextField PWtxt = new JTextField();
	JButton loginBtn = new JButton("Login");
	
	JDialog loginDlg = new JDialog(this, "Notice");
	JLabel loginWronglbl = new JLabel("Wrong ID or Password!");
	JButton loginWrongBtn = new JButton("Okay");
	
	Container cont;
	
	public Login(){
		super("Login");
		clnt = new Client();
		
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 270, 130);
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		cont = this.getContentPane();
		
		cont.add(IDlbl);
		cont.add(PWlbl);
		cont.add(IDtxt);
		cont.add(PWtxt);
		cont.add(loginBtn);
		
		IDlbl.setBounds(12, 15, 30, 21);
		PWlbl.setBounds(12, 46, 30, 21);
		IDtxt.setBounds(54, 15, 100, 21);
		PWtxt.setBounds(54, 46, 100, 21);
		loginBtn.setBounds(166, 10, 70, 70);
		
		loginBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.print(IDtxt.getText() +"  |||  "+PWtxt.getText());
				try {
					System.out.print(IDtxt.getText() +"  |||  "+PWtxt.getText());
					clnt.sendLoginRequest(IDtxt.getText(), PWtxt.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		loginWronglbl.setVerticalAlignment(SwingConstants.CENTER);
		loginDlg.setSize(200,100);
		loginDlg.setVisible(false);
		loginDlg.setLocationRelativeTo(this);
		loginDlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		loginDlg.getContentPane().add(loginWronglbl, "Center");
		loginDlg.getContentPane().add(loginWrongBtn, "South");
		loginWrongBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				loginDlg.setVisible(false);
			}
		});
			
	}
	
	public void wrongParam(){
		loginDlg.setVisible(true);
	}
	
}
