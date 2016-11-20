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

	private ImageIcon waiting = new ImageIcon("watingBg.png");
	private ImageIcon rank = new ImageIcon("rankList.png");
	private ImageIcon room = new ImageIcon("roomList.png");
	
	private JLabel waitBg = new JLabel(waiting);
	private JLabel ranklbl = new JLabel(rank);
	private JLabel roomlbl = new JLabel(room);

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
		cont.add(waitBg);
		
		RefreshBtn.setBounds(225, 400, 113, 32);
		MakeRoomBtn.setBounds(350, 400, 113, 32);
		EnterBtn.setBounds(475, 400, 113, 32);
		

		roomScrlPane.setBounds(15, 121, 588, 277);
		rankList.setBounds(630, 121, 150, 319);
		
		roomlbl.setBounds(220, 5, room.getIconWidth(), room.getIconHeight());
		//roomlbl.setHorizontalAlignment(SwingConstants.CENTER);
		//roomlbl.setFont(new Font("±¼¸²", Font.BOLD, 16));

		ranklbl.setBounds(620, 40, rank.getIconWidth() , rank.getIconHeight());
		//ranklbl.setHorizontalAlignment(SwingConstants.CENTER);
		//ranklbl.setFont(new Font("±¼¸²", Font.BOLD, 16));

		
		msgScrlPane.setBounds(0, 10, 583, 255);
		msgTxt.setBounds(0,270, 583, 21);
		msgArea.setEditable(false);

		
		chatPanel.setBounds(15, 450,  588, 295);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);
		
		waitBg.setBounds(0,0,waiting.getIconWidth(),waiting.getIconHeight());
		
		
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
				}
				try {
					clnt.sendMessage(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				msgTxt.setText("");
			}
		});

		MakeRoomBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Client.makeNewRoom();
				dispose();
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
		
		EnterBtn.addActionListener(new ActionListener(){
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
	 * When user press "Make Room" Button, this function
	 * will take title value and return it.
	 * @return
	 */
	public String getRoomName() {
		return JOptionPane.showInputDialog(this, "Enter the Room Name", "Make new room", JOptionPane.QUESTION_MESSAGE);
	}
	private void noRoomAlert() {
		JOptionPane.showMessageDialog(this, "No way! Room was vanished!");
	}

	public void gotMessage(String msg) {
		int pos;

		msgArea.append(msg + "\n");

		/* After get a message, because user can feel uncomfortable,
		 * reload a scroll bar to bottom of Area.
		 * Plus, after reload a scroll bar, make a focus on textField.
		 */
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}

	private void enterToRoom() throws ClassNotFoundException, IOException{
		String[] elemStr = roomList.getSelectedItems();
		if(elemStr.length != 0){
			StringTokenizer toks = new StringTokenizer(elemStr[0].substring(0), " ");
			String roomNoStr = toks.nextToken();
			
			int roomNo = Integer.parseInt(roomNoStr);
			if(Client.getInfoForRoom(roomNo) == null){
				noRoomAlert();
				reloadRoomList();
			}else{
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
