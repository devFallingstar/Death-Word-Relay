package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;

import RoomClient.*;


public class Waiting extends JFrame {
	Client clnt;
	JPanel chatPanel = new JPanel();
	
	JLabel ranklbl = new JLabel("Top 10");
	JLabel roomlbl = new JLabel("Available Rooms");
	
	List roomList = new List(10, false);
	List rankList = new List(10, false);
	
	JButton MakeRoomBtn = new JButton("Make Room");
	JButton AutoEnterBtn = new JButton("Auto Enter");
	JButton RefreshBtn = new JButton("Refresh");
	
	JTextField msgTxt = new JTextField(41);
	JTextArea msgArea = new JTextArea(9, 65);
	JScrollPane msgScrlPane = new JScrollPane(msgArea);
	JScrollPane roomScrlPane = new JScrollPane(roomList);
	
	Container cont;
	private static Vector<String> rooms = new Vector<String>();
	
	
	public Waiting(){
		super("Death Word Relay ver.0.01");
		clnt = new Client();
		
		
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		
		rooms.add("Room Hello guys!");
		rooms.add("This is not a test room");
		rooms.add("Sorry. It's a test room");
		
		
		for(String str : rooms){
			roomList.add(str);
		}
		
		
		cont = this.getContentPane();
		
		cont.add(MakeRoomBtn);
		cont.add(AutoEnterBtn);
		cont.add(RefreshBtn);
		cont.add(roomScrlPane);
		cont.add(roomlbl);
		cont.add(rankList);
		cont.add(ranklbl);
		
		cont.add(chatPanel);
		
		RefreshBtn.setBounds(284, 324, 113, 32);
		AutoEnterBtn.setBounds(534, 324, 113, 32);
		MakeRoomBtn.setBounds(409, 324, 113, 32);
		
		roomScrlPane.setBounds(10, 41, 637, 277);
		rankList.setBounds(653, 41, 113, 319);
		
		ranklbl.setBounds(659, 21, 100, 15);
		ranklbl.setHorizontalAlignment(SwingConstants.CENTER);
		ranklbl.setFont(new Font("±¼¸²", Font.BOLD, 16));
		
		roomlbl.setBounds(248, 21, 151, 15);
		roomlbl.setHorizontalAlignment(SwingConstants.CENTER);
		roomlbl.setFont(new Font("±¼¸²", Font.BOLD, 16));
		
		msgScrlPane.setBounds(0, 10, 529, 334);
		msgTxt.setBounds(0, 354, 529, 21);
		
		chatPanel.setBounds(10, 366, 534, 385);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);
		
		msgTxt.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             * If user input "/clear", all of log are cleared.
             */
            public void actionPerformed(ActionEvent e) {
            	String msg = msgTxt.getText();
            	
            	if (msg.equalsIgnoreCase("/clear")){
            		msgArea.setText("");
            	}
            	
                try {
					clnt.sendMessage(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                msgTxt.setText("");
            }
        });
		
		
			
	}
	
	
	public void gotMessage(String msg){
		int pos;
		
		msgArea.append(msg.substring(8) + "\n");
		
		// After get a message, because user can feel uncomfortable, 
        // reload a scroll bar to bottom of Area.
        // Plus, after reload a scroll bar, make a focus on textField.
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}
	
	public void reloadRoomList(){
		Vector<String> newRoomList;
	}
}
