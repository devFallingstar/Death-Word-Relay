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

public class Register extends JFrame {
	Client clnt;
	Login myLogin;

	private Container cont;

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
	JLabel NickNameCautionlbl = new JLabel("at least 1 to 12 chracters");

	public Register() {
		super("Register");
		clnt = new Client();

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 470, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		cont = this.getContentPane();

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

		IDCautionlbl.setBounds(223, 60, 200, 16);
		IDCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		IDCautionlbl2.setBounds(220, 80, 200, 16);
		IDCautionlbl2.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		PWCautionlbl.setBounds(223, 135, 200, 16);
		PWCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		NickNameCautionlbl.setBounds(223, 192, 200, 16);
		NickNameCautionlbl.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

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
		cont.setBackground(Color.white);

		SubmitBtn.addActionListener(new ActionListener() {
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

		CancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new Login();
					dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

	}

	public void alertComplete() {
		JOptionPane.showMessageDialog(this, "You are now the member of death word relay!");
	}

	public void alertWrong(int code) {
		if (code == -1) {
			JOptionPane.showMessageDialog(this, "Duplicate ID!");
		} else if (code == -2) {
			JOptionPane.showMessageDialog(this, "Duplicate Nickname!");
		} else{
			JOptionPane.showMessageDialog(this, "Unknow error. Try again later!");
		}
	}
}
