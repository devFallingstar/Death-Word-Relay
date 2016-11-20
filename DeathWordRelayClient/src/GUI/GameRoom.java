package GUI;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Data.User;
import RoomClient.*;

public class GameRoom extends JFrame {
	private boolean isReady;
	private Client clnt;
	private User myUser;
//	private User oppUser; <- Not implemented yet. DO NOT ERASE.
	private JPanel chatPanel = new JPanel();

	private JTextField msgTxt = new JTextField(41);
	private JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);

	private JButton exitBtn = new JButton("Exit");
	private JButton readyBtn = new JButton("Ready");

	private Container cont;

	public GameRoom(int rNo, String title) {
		super(rNo + ". " + title);
		clnt = new Client();
		this.myUser = Client.curUser;
		isReady = false;

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		cont = this.getContentPane();

		cont.add(chatPanel);
		cont.add(readyBtn);
		cont.add(exitBtn);

		msgScrlPane.setBounds(0, 10, 529, 690);
		msgTxt.setBounds(0, 710, 529, 21);
		msgArea.setEditable(false);

		chatPanel.setBounds(10, 10, 534, 741);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);
		readyBtn.setBounds(679, 460, 93, 49);
		exitBtn.setBounds(556, 460, 93, 49);

		msgTxt.addActionListener(new ActionListener() {
			/**
			 * Responds to pressing the enter key in the textfield by sending
			 * the contents of the text field to the server. Then clear the text
			 * area in preparation for the next message. If user input "/clear",
			 * all of log are cleared.
			 */
			public void actionPerformed(ActionEvent e) {
				String msg = msgTxt.getText();

				if (msg.equalsIgnoreCase("/clear")) {
					msgArea.setText("");
				} else {
					try {
						clnt.sendMessageAtRoom("ROOMMSG " + myUser.getrNo() + " " + msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				msgTxt.setText("");
			}
		});

		exitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.exitCurrentRoom();
				dispose();
			}
		});
		this.setResizable(false);
		
		readyBtn.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public void gotMessage(String msg) {
		msgArea.append(msg + "\n");
		
		// After get a message, because user can feel uncomfortable,
		// reload a scroll bar to bottom of Area.
		// Plus, after reload a scroll bar, make a focus on textField.
		int pos;
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}
}
