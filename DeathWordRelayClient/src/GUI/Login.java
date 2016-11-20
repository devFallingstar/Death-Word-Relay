package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import RoomClient.*;

public class Login extends JFrame {
	private Client clnt;
	private ImageIcon idImg = new ImageIcon("idLb.png");
	private ImageIcon pwImg = new ImageIcon("pwLb.png");
	private ImageIcon goImg = new ImageIcon("goBt.png");
	private ImageIcon mainImg = new ImageIcon("main.png");
	
	private JLabel main = new JLabel(mainImg);
	private JLabel IDlbl = new JLabel(idImg);
	private JLabel PWlbl = new JLabel(pwImg);
	private JTextField IDtxt = new JTextField();
	private JTextField PWtxt = new JTextField();
	private JButton loginBtn = new JButton(goImg);

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
		cont.setBackground(Color.white);
		
		main.setBounds(0,0,mainImg.getIconWidth(),mainImg.getIconHeight());
		IDlbl.setBounds(127, 601, idImg.getIconWidth(), idImg.getIconHeight());
		PWlbl.setBounds(125, 636, pwImg.getIconWidth(), pwImg.getIconHeight());
		IDtxt.setBounds(185, 601, 100, 23);
		PWtxt.setBounds(186, 636, 100, 23);
		loginBtn.setBounds(306, 594, goImg.getIconWidth(), goImg.getIconHeight());
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					clnt.sendLoginRequest(IDtxt.getText(), PWtxt.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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

