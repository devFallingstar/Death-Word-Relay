package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;

import Data.Room;
import RoomClient.*;

public class Waiting extends JFrame {
	private Client clnt;
	private JPanel chatPanel = new JPanel();

	private JLabel ranklbl = new JLabel("Top 10");
	private JLabel roomlbl = new JLabel("Available Rooms");

	private List roomList = new List(10, false);
	private List rankList = new List(10, false);

	private JButton MakeRoomBtn = new JButton("Make Room");
	private JButton EnterBtn = new JButton("Enter");
	private JButton RefreshBtn = new JButton("Refresh");

	private JTextField msgTxt = new JTextField(41);
	private JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);
	private JScrollPane roomScrlPane = new JScrollPane(roomList);

	private Container cont;

	private static HashMap<Integer, String> newRoomList;
	private static Vector<String> rooms = new Vector<String>();

	public Waiting() {
		super("Death Word Relay ver.0.01");

		clnt = new Client();

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		cont = this.getContentPane();

		cont.add(MakeRoomBtn);
		cont.add(EnterBtn);
		cont.add(RefreshBtn);
		cont.add(roomScrlPane);
		cont.add(roomlbl);
		cont.add(rankList);
		cont.add(ranklbl);

		cont.add(chatPanel);

		RefreshBtn.setBounds(284, 324, 113, 32);
		EnterBtn.setBounds(534, 324, 113, 32);
		MakeRoomBtn.setBounds(409, 324, 113, 32);

		roomScrlPane.setBounds(10, 41, 637, 277);
		rankList.setBounds(653, 41, 113, 319);

		ranklbl.setBounds(659, 21, 100, 15);
		ranklbl.setHorizontalAlignment(SwingConstants.CENTER);
		ranklbl.setFont(new Font("����", Font.BOLD, 16));

		roomlbl.setBounds(248, 21, 151, 15);
		roomlbl.setHorizontalAlignment(SwingConstants.CENTER);
		roomlbl.setFont(new Font("����", Font.BOLD, 16));

		msgScrlPane.setBounds(0, 10, 529, 334);
		msgTxt.setBounds(0, 354, 529, 21);
		msgArea.setEditable(false);

		chatPanel.setBounds(10, 366, 534, 385);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);

		msgTxt.addActionListener(new ActionListener() {
			/**
			 * Responds to pressing the enter key in the textfield by sending
			 * the contents of the text field to the server. Then clear the text
			 * area in preparation for the next message. If user input "/clear",
			 * all of log are cleared.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = msgTxt.getText();

				if (msg.equalsIgnoreCase("/clear")) {
					msgArea.setText("");
				} else {
					try {
						clnt.sendMessage(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				msgTxt.setText("");
			}
		});

		MakeRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Client.makeNewRoom() == 1) {
					dispose();
				}
			}
		});

		RefreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					reloadRoomList();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		EnterBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					enterToRoom();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		roomList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {

			}
		});

		this.setResizable(false);
	}

	/**
	 * When user press "Make Room" Button, this function will take title value
	 * and return it.
	 * 
	 * @return
	 */
	public String getRoomName() {
		return JOptionPane.showInputDialog(this, "Enter the Room Name", "Make new room", JOptionPane.QUESTION_MESSAGE);
	}

	private void noRoomAlert() {
		JOptionPane.showMessageDialog(this, "No way! Room was vanished!");
	}

	private void fullRoomAlert() {
		JOptionPane.showMessageDialog(this, "Uh-oh! The room was fulled!");
	}

	public void gotMessage(String msg) {
		int pos;

		msgArea.append(msg + "\n");

		/*
		 * After get a message, because user can feel uncomfortable, reload a
		 * scroll bar to bottom of Area. Plus, after reload a scroll bar, make a
		 * focus on textField.
		 */
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}

	private void enterToRoom() throws ClassNotFoundException, IOException {
		String[] elemStr = roomList.getSelectedItems();
		if (elemStr.length != 0) {
			StringTokenizer toks = new StringTokenizer(elemStr[0].substring(0), " ");
			String roomNoStr = toks.nextToken();

			int roomNo = Integer.parseInt(roomNoStr);
			if (Client.getInfoForRoom(roomNo).getNo() == -1) {
				noRoomAlert();
				reloadRoomList();
			} else if (Client.getInfoForRoom(roomNo).getNo() == -2) {
				fullRoomAlert();
				reloadRoomList();
			} else {
				Client.enterToCurrentRoom();
				dispose();
			}
		}
	}

	public void reloadRoomList() throws ClassNotFoundException, IOException {
		newRoomList = Client.getNewRoomList();
		rooms.removeAllElements();
		for (Integer rNo : newRoomList.keySet()) {
			rooms.add(rNo + "  |         " + newRoomList.get(rNo));
		}

		roomList.removeAll();
		for (String str : rooms) {
			roomList.add(str);
		}
	}
}
