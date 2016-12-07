package GUI;

import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Loginout.*;
import RoomClient.Client;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

/**
 * This class is used for define a GUI system of register window, and process
 * all of methods that execute in register window.
 * 
 * @author YYS
 *
 */
@SuppressWarnings("serial")
public class Register extends JFrame {

	/* Basic variables */
	Client clnt;
	Login myLogin;

	/* Basic GUI variables */
	private ImageIcon idImg = new ImageIcon("Img/idLb.png");
	private ImageIcon pwImg = new ImageIcon("Img/pwLb.png");
	private ImageIcon nickImg = new ImageIcon("Img/nickLb.png");
	private ImageIcon submitImg = new ImageIcon("Img/submitBt.png");
	private ImageIcon cancelImg = new ImageIcon("Img/cancelBt.png");

	private JTextField IDtxt = new JTextField();
	private JPasswordField PWtxt = new JPasswordField();
	private JTextField NickNametxt = new JTextField();

	JButton SubmitBtn = new JButton(submitImg);
	JButton CancelBtn = new JButton(cancelImg);

	JLabel IDlbl = new JLabel(idImg);
	JLabel PWlbl = new JLabel(pwImg);
	JLabel NickNamelbl = new JLabel(nickImg);

	JLabel IDCautionlbl = new JLabel("at least 6 to 12 chracters");
	JLabel IDCautionlbl2 = new JLabel("only english and number accepted");
	JLabel PWCautionlbl = new JLabel("at least 6 to 12 chracters");
	JLabel NickNameCautionlbl = new JLabel("at least 1 to 8 chracters");

	/* Default container */
	private Container cont;

	/**
	 * Constructor for GUI system.
	 */
	public Register() {
		/* Initialize default informations */
		super("Register");
		clnt = new Client();

		/* Set default frame informations */
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 430, 380);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		/* When press enter, submit button will be clicked */
		this.getRootPane().setDefaultButton(SubmitBtn);

		/* Set default container and add components */
		cont = this.getContentPane();
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

		/* Initialize default background design and system */
		cont.setBackground(Color.black);

		SubmitBtn.setBounds(60, 230, submitImg.getIconWidth(), submitImg.getIconHeight());
		SubmitBtn.setBackground(Color.red);
		SubmitBtn.setBorderPainted(false);
		SubmitBtn.setFocusPainted(false);
		SubmitBtn.setContentAreaFilled(false);

		CancelBtn.setBounds(222, 230, cancelImg.getIconWidth(), cancelImg.getIconHeight());
		CancelBtn.setBackground(Color.red);
		CancelBtn.setBorderPainted(false);
		CancelBtn.setFocusPainted(false);
		CancelBtn.setContentAreaFilled(false);

		IDlbl.setBounds(43, 34, idImg.getIconWidth(), idImg.getIconHeight());
		IDtxt.setBounds(190, 34, 172, 26);
		PWlbl.setBounds(43, 105, pwImg.getIconWidth(), pwImg.getIconHeight());
		PWtxt.setBounds(190, 105, 172, 26);
		NickNamelbl.setBounds(40, 160, nickImg.getIconWidth(), nickImg.getIconHeight());
		NickNametxt.setBounds(190, 160, 172, 26);

		IDCautionlbl.setBounds(190, 60, 200, 16);
		IDCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		IDCautionlbl2.setBounds(190, 80, 200, 16);
		IDCautionlbl2.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		PWCautionlbl.setBounds(190, 135, 200, 16);
		PWCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		NickNameCautionlbl.setBounds(190, 192, 200, 16);
		NickNameCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		/*
		 * When submit button is clicked, check a duplicates of ID and nick
		 * name, and add user to DB Server.
		 */
		SubmitBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				MemberInfo newMem = new MemberInfo(IDtxt.getText(), PWtxt.getText(), NickNametxt.getText());
				try {
					int code = MemberProc.create(newMem);
					if (code == 1) {
						alertComplete();
						dispose();

						new Login();
					} else {
						alertWrong(code);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		/* Close the register menu */
		CancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new Login();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Submit and register is complete.
	 */
	public void alertComplete() {
		JOptionPane.showMessageDialog(this, "You are now the member of death word relay!");
	}

	/**
	 * If registering leads error, alert it.
	 * 
	 * @param code
	 */
	public void alertWrong(int code) {
		if (code == -1) {
			JOptionPane.showMessageDialog(this, "Duplicated ID! \nTry another.", "Register failed!",
					JOptionPane.WARNING_MESSAGE);
		} else if (code == -2) {
			JOptionPane.showMessageDialog(this, "Duplicated Nickname! \nTry another.", "Register failed!",
					JOptionPane.WARNING_MESSAGE);
		} else if (code == 0) {
			JOptionPane.showMessageDialog(this, "Wrong ID, PW or Nickname.", "Register failed!",
					JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Unknow error. Try again later!", "Register failed!",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
