package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import RoomClient.*;

public class Login extends JFrame {
	private Client clnt;

	private JLabel IDlbl = new JLabel("ID :");
	private JLabel PWlbl = new JLabel("PW :");
	private JTextField IDtxt = new JTextField();
	private JTextField PWtxt = new JTextField();
	private JButton loginBtn = new JButton("Login");

	private JDialog loginDlg = new JDialog(this, "Notice");
	private JLabel loginWronglbl = new JLabel("Wrong ID or Password!");
	private JButton loginWrongBtn = new JButton("Okay");

	private Container cont;

	public Login() {
		super("Login");
		clnt = new Client();

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 270, 130);
		this.setVisible(true);
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
