package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import Data.User;
import RoomClient.*;

public class Login extends JFrame {
	private Client clnt;

	private Register register;
	private ImageIcon idImg = new ImageIcon("Img/idLb.png");
	private ImageIcon pwImg = new ImageIcon("Img/pwLb.png");
	private ImageIcon goImg = new ImageIcon("Img/goBt.png");
	private ImageIcon mainImg = new ImageIcon("Img/main.png");
	private ImageIcon regImg1 = new ImageIcon("Img/regLb.png");
	private ImageIcon regImg2 = new ImageIcon("Img/regBt.png");

	private Register myReg;

	private JLabel main = new JLabel(mainImg);
	private JLabel IDlbl = new JLabel(idImg);
	private JLabel PWlbl = new JLabel(pwImg);
	private JTextField IDtxt = new JTextField();
	private JPasswordField PWtxt = new JPasswordField();
	private JButton loginBtn = new JButton(goImg);

	private JLabel REGlbl = new JLabel(regImg1);
	private JButton regBtn = new JButton(regImg2);

	private JDialog loginDlg = new JDialog(this, "Notice");
	private JLabel loginWronglbl = new JLabel("Wrong ID or Password!");
	private JButton loginWrongBtn = new JButton("Okay");

	private Container cont;

	public Login() throws IOException {
		super("Login");
		clnt = new Client();

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 500, 780);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		loginBtn.setBackground(Color.red);
		loginBtn.setBorderPainted(false);
		loginBtn.setFocusPainted(false);
		loginBtn.setContentAreaFilled(false);

		regBtn.setBackground(Color.red);
		regBtn.setBorderPainted(false);
		regBtn.setFocusPainted(false);
		regBtn.setContentAreaFilled(false);

		cont = this.getContentPane();

		cont.add(main);
		cont.add(IDlbl);
		cont.add(PWlbl);
		cont.add(IDtxt);
		cont.add(PWtxt);
		cont.add(loginBtn);
		cont.add(REGlbl);
		cont.add(regBtn);
		cont.setBackground(Color.white);

		// Press enter to login
		this.getRootPane().setDefaultButton(loginBtn);

		main.setBounds(0, 0, mainImg.getIconWidth(), mainImg.getIconHeight());
		IDlbl.setBounds(127, 601, idImg.getIconWidth(), idImg.getIconHeight());
		PWlbl.setBounds(125, 636, pwImg.getIconWidth(), pwImg.getIconHeight());
		IDtxt.setBounds(185, 601, 100, 23);
		PWtxt.setBounds(186, 636, 100, 23);
		loginBtn.setBounds(306, 594, goImg.getIconWidth(), goImg.getIconHeight());
		REGlbl.setBounds(87, 680, regImg1.getIconWidth(), regImg1.getIconHeight());
		regBtn.setBounds(275, 682, regImg2.getIconWidth(), regImg2.getIconHeight());

		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.playSound("music/SE/DoorOpen.wav", false);
				try {
					if (Client.sendLoginRequest(IDtxt.getText(), PWtxt.getText())) {
						dispose();
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		regBtn.addActionListener(new ActionListener() {

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

	public void DupLoginAlert() {
		JOptionPane.showMessageDialog(this, "Login blocked - duplicates login");
	}
}
