package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class test extends JFrame {
	private JTextField textField;
	private JTextField textField_1;
	public test() {
		getContentPane().setLayout(null);
		
		JLabel lblId = new JLabel("ID :");
		lblId.setBounds(12, 33, 57, 15);
		getContentPane().add(lblId);
		
		JLabel lblPw = new JLabel("PW :");
		lblPw.setBounds(12, 58, 57, 15);
		getContentPane().add(lblPw);
		
		textField = new JTextField();
		textField.setBounds(65, 30, 151, 21);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(65, 58, 151, 21);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(231, 33, 74, 40);
		getContentPane().add(btnNewButton);
	}
}
