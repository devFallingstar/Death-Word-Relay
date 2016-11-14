package GUI;

import java.awt.Container;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Data.Room;
import Data.User;
import RoomClient.*;

public class GameRoom extends JFrame {
	JFrame mainframe = new JFrame("GameRoom");

	Client clnt;
	User myUser;
	User oppUser;
	JPanel chatPanel = new JPanel();

	JTextField msgTxt = new JTextField(41);
	JTextArea msgArea = new JTextArea(9, 65);
	JScrollPane msgScrlPane = new JScrollPane(msgArea);

	JButton exitBtn = new JButton("Exit");
	JButton readyBtn = new JButton("Ready");

	Container cont;

	public GameRoom(int rNo, String title) {
		super(rNo + ". " + title);
		mainframe = this;
		clnt = new Client();
		this.myUser = Client.myUser;

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
						clnt.sendMessage("ROOMMSG " + myUser.getrNo() + " " + msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				msgTxt.setText("");
			}
		});

		exitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Client.exitCurrentRoom();
				dispose();
			}

		});
		this.setResizable(false);
	}
}
