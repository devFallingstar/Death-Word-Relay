package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import RoomClient.*;

public class Login extends JFrame {
	private Client clnt;
	private Register myReg;
	private ImageIcon idImg = new ImageIcon("resources/idLb.png");
	private ImageIcon pwImg = new ImageIcon("resources/pwLb.png");
	private ImageIcon goImg = new ImageIcon("resources/goBt.png");
	private ImageIcon mainImg = new ImageIcon("resources/main.png");
	
	private JLabel main = new JLabel(mainImg);
	private JLabel IDlbl = new JLabel(idImg);
	private JLabel PWlbl = new JLabel(pwImg);
	private JTextField IDtxt = new JTextField();
	private JTextField PWtxt = new JTextField();
	private JButton loginBtn = new JButton(goImg);
	private JButton regBtn = new JButton("Register");

	private JDialog loginDlg = new JDialog(this, "Notice");
	private JLabel loginWronglbl = new JLabel("Wrong ID or Password!");
	private JButton loginWrongBtn = new JButton("Okay");

	private Container cont;

	public Login() throws IOException {
		super("Login");
		clnt = new Client();
		
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 500, 720);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		loginBtn.setBackground(Color.red);
		loginBtn.setBorderPainted(false);
		loginBtn.setFocusPainted(false);
		loginBtn.setContentAreaFilled(false);

		cont = this.getContentPane();

		cont.add(main);
		cont.add(IDlbl);
		cont.add(PWlbl);
		cont.add(IDtxt);
		cont.add(PWtxt);
		cont.add(loginBtn);
		cont.add(regBtn);
		cont.setBackground(Color.white);
		
		main.setBounds(0,0,mainImg.getIconWidth(),mainImg.getIconHeight());
		IDlbl.setBounds(85, 601, idImg.getIconWidth(), idImg.getIconHeight());
		PWlbl.setBounds(85, 636, pwImg.getIconWidth(), pwImg.getIconHeight());
		IDtxt.setBounds(140, 601, 100, 23);
		PWtxt.setBounds(140, 636, 100, 23);
		regBtn.setBounds(340, 594, 70, 70);
		loginBtn.setBounds(260, 594, goImg.getIconWidth(), goImg.getIconHeight());
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Client.sendLoginRequest(IDtxt.getText(), PWtxt.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		regBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myReg = new Register();
				dispose();
			}
			
		});
		
		loginWronglbl.setVerticalAlignment(SwingConstants.CENTER);
		loginDlg.setSize(200, 100);
		loginDlg.setVisible(false);
		loginDlg.setLocationRelativeTo(this);
		loginDlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		loginDlg.getContentPane().add(loginWronglbl, "Center");
		loginDlg.getContentPane().add(loginWrongBtn, "South");
		loginWrongBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginDlg.setVisible(false);
			}
		});
		this.setResizable(false);
	}

	public void wrongParam() {
		loginDlg.setVisible(true);
	}
}

