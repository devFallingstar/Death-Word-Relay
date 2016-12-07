package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;

import Data.Room;
import Database.RankDB;
import RoomClient.*;

/**
 * This class is used for define a GUI system of waiting room, and process all
 * of methods that execute in waiting room.
 * 
 * @author YYS
 *
 */
@SuppressWarnings("serial")
public class Waiting extends JFrame {

	/* Basic variables */
	private HashMap<Integer, String> newRoomList;
	private Vector<String> rooms = new Vector<String>();

	/* Basic background GUI variables */
	private ImageIcon waiting = new ImageIcon("Img/waitingBg.jpg");

	private ImageIcon rank = new ImageIcon("Img/rankList1.png");
	private ImageIcon room = new ImageIcon("Img/roomList.png");
	private ImageIcon mkr = new ImageIcon("Img/mkrBt.png");
	private ImageIcon ent = new ImageIcon("Img/entBt.png");
	private ImageIcon rf = new ImageIcon("Img/rfBt.png");
	private ImageIcon chat = new ImageIcon("Img/chatBg.png");
	private ImageIcon userImg = new ImageIcon("Img/userInform.png");
	/* Basic background GUI variables */
	private JLabel waitBg = new JLabel(waiting);
	private JLabel ranklbl = new JLabel(rank);
	private JLabel roomlbl = new JLabel(room);

	private JLabel userInfo = new JLabel(userImg);
	private JLabel userId = new JLabel("userNICK");
	private JLabel userRate = new JLabel("userRate");
	private JLabel chatlbl = new JLabel(chat);

	private List roomList = new List(15, false);
	private JTextArea rankList = new JTextArea("  ID  | Rate\n", 9, 65);
	private JButton MakeRoomBtn = new JButton(mkr);
	private JButton EnterBtn = new JButton(ent);
	private JButton RefreshBtn = new JButton(rf);

	/* Basic Chatting panel */
	private JPanel chatPanel = new JPanel() {
		public void paintComponent(Graphics g) {
			g.drawImage(chat.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);
		}
	};
	private JTextField msgTxt = new JTextField(41);
	private JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);
	private JScrollPane roomScrlPane = new JScrollPane(roomList);

	/* Default container */
	private Container cont;

	/**
	 * Constructor for GUI system
	 */
	public Waiting() {
		/* Initialize default informations */
		super("Death Word Relay");

		/* Set default frame informations */
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);

		/* Set default container and add components */
		cont = this.getContentPane();
		cont.add(MakeRoomBtn);
		cont.add(EnterBtn);
		cont.add(RefreshBtn);
		cont.add(roomScrlPane);
		cont.add(roomlbl);
		cont.add(rankList);
		cont.add(ranklbl);
		cont.add(chatPanel);
		cont.add(userId);
		cont.add(userRate);

		cont.add(userInfo);

		cont.add(waitBg);
		cont.add(chatlbl);

		/* Initialize default background design and system */
		waitBg.setBounds(0, 0, waiting.getIconWidth(), waiting.getIconHeight());

		MakeRoomBtn.setBounds(45, 30, mkr.getIconWidth(), mkr.getIconHeight());
		MakeRoomBtn.setBackground(Color.red);
		MakeRoomBtn.setBorderPainted(false);
		MakeRoomBtn.setFocusPainted(false);
		MakeRoomBtn.setContentAreaFilled(false);

		EnterBtn.setBounds(405, 30, ent.getIconWidth(), ent.getIconHeight());
		EnterBtn.setBackground(Color.red);
		EnterBtn.setBorderPainted(false);
		EnterBtn.setFocusPainted(false);
		EnterBtn.setContentAreaFilled(false);

		RefreshBtn.setBounds(515, 30, rf.getIconWidth(), rf.getIconHeight());
		RefreshBtn.setBackground(Color.red);
		RefreshBtn.setBorderPainted(false);
		RefreshBtn.setFocusPainted(false);
		RefreshBtn.setContentAreaFilled(false);

		roomScrlPane.setBounds(15, 121, 588, 277);
		roomList.setFont(new Font("Malgun Gothic", Font.BOLD, 15));

		rankList.setBounds(630, 121, 150, 277);
		rankList.setOpaque(false);
		rankList.setEditable(false);
		rankList.setForeground(Color.white);
		rankList.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
		rankList.setText(RankDB.floatingRank());

		roomlbl.setBounds(205, 3, room.getIconWidth(), room.getIconHeight());
		ranklbl.setBounds(630, 40, rank.getIconWidth(), rank.getIconHeight());
		userInfo.setBounds(610, 450, userImg.getIconWidth(), userImg.getIconHeight());

		/* Initialize default chatting panel's design and system */
		chatPanel.setBounds(15, 450, 588, 295);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);

		userId.setBounds(635, 525, 80, 25);
		userId.setFont(new Font("chiller", Font.BOLD, 25));
		userId.setForeground(Color.red);
		userId.setText(Client.getNICK());

		userRate.setBounds(635, 600, 80, 25);
		userRate.setFont(new Font("chiller", Font.BOLD, 25));
		userRate.setForeground(Color.red);
		userRate.setText(RankDB.getRate(Client.getID()) + "%");

		msgScrlPane.setBounds(0, 10, 583, 255);
		msgScrlPane.setOpaque(false);
		msgScrlPane.getViewport().setOpaque(false);
		msgTxt.setBounds(0, 270, 583, 21);
		msgTxt.setOpaque(false);
		msgTxt.setForeground(Color.GRAY);
		msgArea.setOpaque(false);
		msgArea.setEditable(false);
		msgArea.setForeground(Color.white);
		msgArea.setSize(583, 255);
		msgArea.setFont(new Font("Malgun Gothic", Font.BOLD, 15));

		/*
		 * Send message to server when user press enter key with some text in
		 * msgTxt(TextField). If user enter /clear, msgArea will be cleared.
		 */
		msgTxt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = msgTxt.getText();
				try {
					msg = new String(msg.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}

				if (msg.equalsIgnoreCase("/clear")) {
					msgArea.setText("");
				}
				try {
					Client.sendMessage(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				msgTxt.setText("");
			}
		});

		/*
		 * If user press make room button, room will be created, and
		 * automatically join to the room.
		 */
		MakeRoomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int code = Client.makeNewRoom();
				if (code == 1) {
					dispose();
				} else if (code == -1) {
					// Cancel
				} else {
					WrongRoomAlert();
				}
			}
		});

		/*
		 * If user press refresh button, room list will be refreshed.
		 */
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

		/*
		 * If user press enter button with selection of room that listed, user
		 * will be enter to room if the room is not full.
		 */
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

		/*
		 * When user click double time the room that listed, user will be enter
		 * to room if the room is not full.
		 */
		roomList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					try {
						enterToRoom();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * When user press "Make Room" Button, this function will take title string.
	 * 
	 * @return title that user entered.
	 */
	public String getRoomName() {
		return JOptionPane.showInputDialog(this, "Enter the Room Name", "Make new room", JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * If the room is removed before entering, this method will alert to user.
	 */
	private void noRoomAlert() {
		JOptionPane.showMessageDialog(this, "No way! Room is vanished!", "Alert", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * If the room is full, this method will alert to user.
	 */
	private void fullRoomAlert() {
		JOptionPane.showMessageDialog(this, "No way! Room is fulled!", "Alert", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * If the room make is failed, this method will alert to user.
	 */
	private void WrongRoomAlert() {
		JOptionPane.showMessageDialog(this, "Wrong room name.", "Room make failed", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * When client got room message, this method will print it to message area.
	 * 
	 * @param message
	 *            from server.
	 */
	public void gotMessage(String msg) {
		int pos;

		msgArea.append(msg + "\n");

		/* reload text area to below */
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}

	/**
	 * Enter to room that selected.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void enterToRoom() throws ClassNotFoundException, IOException {
		String[] elemStr = roomList.getSelectedItems();
		if (elemStr.length != 0) {
			StringTokenizer toks = new StringTokenizer(elemStr[0].substring(0), " ");
			String roomNoStr = toks.nextToken();
			int roomNo = Integer.parseInt(roomNoStr);
			Room destRoom = (Room) Client.getInfoForRoom(roomNo);

			if (destRoom.getNo() == -1) {
				noRoomAlert();
				reloadRoomList();
			} else if (destRoom.getNo() == -2) {
				fullRoomAlert();
				reloadRoomList();
			} else {
				Client.enterToCurrentRoom();
				dispose();
			}
		}
	}

	/**
	 * reload room list to new version.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void reloadRoomList() throws ClassNotFoundException, IOException {
		newRoomList = Client.getNewRoomList();
		rooms.removeAllElements();
		for (Integer rNo : newRoomList.keySet()) {
			rooms.add(rNo + " |     " + newRoomList.get(rNo));
		}

		roomList.removeAll();
		for (String str : rooms) {
			roomList.add(str);
		}
	}

}