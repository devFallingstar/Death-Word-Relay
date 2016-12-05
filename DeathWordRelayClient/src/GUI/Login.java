package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import RoomClient.*;

/**
 * This class is used for define a GUI system of login window, and process all
 * of methods that execute in login window.
 * 
 * @author YYS
 *
 */
public class Login extends JFrame {
	/* Basic GUI variables */
	private ImageIcon idImg = new ImageIcon("Img/idLb.png");
	private ImageIcon pwImg = new ImageIcon("Img/pwLb.png");
	private ImageIcon goImg = new ImageIcon("Img/goBt.png");
	private ImageIcon mainImg = new ImageIcon("Img/main.png");
	private ImageIcon regImg1 = new ImageIcon("Img/regLb.png");
	private ImageIcon regImg2 = new ImageIcon("Img/regBt.png");

	private JLabel main = new JLabel(mainImg);
	private JLabel IDlbl = new JLabel(idImg);
	private JLabel PWlbl = new JLabel(pwImg);
	private JTextField IDtxt = new JTextField();
	private JPasswordField PWtxt = new JPasswordField();
	private JButton loginBtn = new JButton(goImg);

	private JLabel REGlbl = new JLabel(regImg1);
	private JButton regBtn = new JButton(regImg2);

	/* Default container */
	private Container cont;

	/**
	 * Constructor for GUI system.
	 * 
	 * @throws IOException
	 */
	public Login() throws IOException {
		/* Initialize default informations */
		super("Login");

		/* Set default frame informations */
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 500, 780);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		/* Set default container and add components */
		cont = this.getContentPane();
		cont.add(main);
		cont.add(IDlbl);
		cont.add(PWlbl);
		cont.add(IDtxt);
		cont.add(PWtxt);
		cont.add(loginBtn);
		cont.add(REGlbl);
		cont.add(regBtn);

		/* Initialize default background design and system */
		cont.setBackground(Color.white);

//		main.setBounds(0, 0, mainImg.getIconWidth(), mainImg.getIconHeight());
//		IDlbl.setBounds(127, 601, idImg.getIconWidth(), idImg.getIconHeight());
//		PWlbl.setBounds(125, 636, pwImg.getIconWidth(), pwImg.getIconHeight());
//		IDtxt.setBounds(185, 601, 100, 23);
//		PWtxt.setBounds(186, 636, 100, 23);
//		loginBtn.setBounds(306, 594, goImg.getIconWidth(), goImg.getIconHeight());
//		REGlbl.setBounds(87, 680, regImg1.getIconWidth(), regImg1.getIconHeight());
//		regBtn.setBounds(275, 682, regImg2.getIconWidth(), regImg2.getIconHeight());
//
//		loginBtn.setBackground(Color.red);
//		loginBtn.setBorderPainted(false);
//		loginBtn.setFocusPainted(false);
//		loginBtn.setContentAreaFilled(false);
//
//		regBtn.setBackground(Color.red);
//		regBtn.setBorderPainted(false);
//		regBtn.setFocusPainted(false);
//		regBtn.setContentAreaFilled(false);

		/* When press enter, login button will be clicked. */
		this.getRootPane().setDefaultButton(loginBtn);
		
		cont = this.getContentPane();
		cont.add(main);
		cont.add(IDlbl);
		cont.add(PWlbl);
		cont.add(IDtxt);
		cont.add(PWtxt);
		cont.add(loginBtn);
		cont.add(REGlbl);
		cont.add(regBtn);
		cont.setBackground(Color.black);

		main.setBounds(0, 0, mainImg.getIconWidth(), mainImg.getIconHeight());
		IDlbl.setBounds(127, 601, idImg.getIconWidth(), idImg.getIconHeight());
		PWlbl.setBounds(125, 636, pwImg.getIconWidth(), pwImg.getIconHeight());
		IDtxt.setBounds(185, 601, 100, 23);
		IDtxt.setOpaque(false);
		IDtxt.setForeground(Color.GRAY);
		
		PWtxt.setBounds(186, 636, 100, 23);
		PWtxt.setOpaque(false);
		PWtxt.setForeground(Color.GRAY);
		
		loginBtn.setBounds(306, 594, goImg.getIconWidth(), goImg.getIconHeight());
		REGlbl.setBounds(87, 680, regImg1.getIconWidth(), regImg1.getIconHeight());
		regBtn.setBounds(275, 682, regImg2.getIconWidth(), regImg2.getIconHeight());

		/* Send a login request with some door opening sound. */
		loginBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.playSound("music/SE/DoorOpen.wav", false);
				try {
					if (Client.sendLoginRequest(IDtxt.getText(), PWtxt.getText())) {
						dispose();
					} else {
						wrongParam();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		/* Open register menu. */
		regBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Register();
				dispose();
			}
		});
	}

	/**
	 * Make toast message when login information is wrong.
	 */
	public void wrongParam() {
		JOptionPane.showMessageDialog(this, "Wrong informations!", "Login failed!", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Make toast message when same user is already in server.
	 */
	public void DupLoginAlert() {
		JOptionPane.showMessageDialog(this, "Login blocked - duplicates login");
	}
}