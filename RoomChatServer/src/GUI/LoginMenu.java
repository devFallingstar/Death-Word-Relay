package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginMenu extends JFrame {
	private JTextField textField;
	public LoginMenu() {
		getContentPane().setLayout(null);
		
		JLabel lblWelcomeTester = new JLabel("Welcome! Tester!");
		lblWelcomeTester.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeTester.setBounds(12, 10, 279, 15);
		getContentPane().add(lblWelcomeTester);
		
		textField = new JTextField();
		textField.setBounds(83, 50, 127, 21);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(12, 53, 72, 15);
		getContentPane().add(lblPassword);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(225, 41, 66, 38);
		getContentPane().add(btnNewButton);
	}

}
