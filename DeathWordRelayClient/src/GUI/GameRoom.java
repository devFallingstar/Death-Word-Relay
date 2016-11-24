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
import GameSystem.WordGame;
import RoomClient.*;

public class GameRoom extends JFrame {
	public static WordGame myGame;
	private static boolean isReady;
	private static User myUser;
	// private User oppUser; <- Not implemented yet. DO NOT ERASE.
	private JPanel chatPanel = new JPanel();

	private static JTextField msgTxt = new JTextField(41);
	private static JTextField answerTxt = new JTextField(41);
	private static JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);

	private static JButton exitBtn = new JButton("Exit");
	private static JButton readyBtn = new JButton("Ready");

	private Container cont;

	public GameRoom(int rNo, String title) {
		super(rNo + ". " + title);

		isReady = false;
		this.myUser = Client.curUser;

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
		answerTxt.setBounds(0, 710, 529, 21);
		chatPanel.setBounds(10, 10, 534, 741);
		readyBtn.setBounds(679, 460, 93, 49);
		exitBtn.setBounds(556, 460, 93, 49);

		answerTxt.setEnabled(false);
		answerTxt.setVisible(false);
		msgArea.setEditable(false);

		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);
		chatPanel.add(answerTxt);

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
						Client.sendMessageAtRoom(msg, myUser.getrNo());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				msgTxt.setText("");
			}
		});

		answerTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkAndSendAnswer();
				answerTxt.setText("");
			}
		});

		readyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isReady = !isReady;
				Client.AreYouReady(isReady);
				if (isReady) {
					readyBtn.setText("Unready");
					msgArea.append("Waiting for other player...\n");
				} else {
					readyBtn.setText("Ready");
					msgArea.append("Press Ready button when your ready to play!\n");
				}
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

	public static void playGame(boolean playing) {
		if (playing) {
			msgArea.append("The game system is [Best of Five].\n");
			msgArea.append("From now on, all of uncorrectable message will make you lose.\n");

			msgArea.append("Think carefully before you type your word.\n");
			msgArea.append("--------------------------------------------------------------\n");
			msgArea.append("You can't rewind death.\n");
			msgArea.append("--------------------------------------------------------------\n");
			myGame = new WordGame();
			changeTxtField();
			answerTxt.setEnabled(false);
		} else {
			exitBtn.setEnabled(true);
			readyBtn.setEnabled(true);
		}
	}
	
	public void checkAndSendAnswer(){
		try {
			String msg = answerTxt.getText();
			String lastLine = null, lastWord;

			/* Get last line of msgArea */
			for (String line : msgArea.getText().split("\\n")) {
				lastLine = line;
			}

			/* "---" means you're a first */
			if (lastLine.startsWith("---")) {
				myGame.setCurrentWord(msg);
				
				Client.sendAnswer(msg, myUser.getrNo());
				if(!(myGame.checkLength() && myGame.checkWithOnline())){
					Client.Lose();
				}else{
					Client.requestResume();
					answerTxt.setEnabled(false);
				}
			} else {
				lastWord = lastLine.split(":")[1].trim();

				myGame.setPrevWord(lastWord);
				myGame.setCurrentWord(msg);
				
				Client.sendAnswer(msg, myUser.getrNo());
				if(myGame.isCorrect()){
					Client.requestResume();
					answerTxt.setEnabled(false);
				}else{
					Client.Lose();
				}
			}
		} catch (Exception e1) {

		}
	}
	
	public static void enableAnswerField(){
		answerTxt.setEnabled(true);
	}
	public static void changeTxtField() {
		if (msgTxt.isVisible()) {
			msgTxt.setVisible(false);
			msgTxt.setEnabled(false);
			answerTxt.setVisible(true);
			answerTxt.setEnabled(true);
			exitBtn.setEnabled(false);
			readyBtn.setEnabled(false);
		} else if (answerTxt.isVisible()) {
			msgTxt.setVisible(true);
			msgTxt.setEnabled(true);
			answerTxt.setVisible(false);
			answerTxt.setEnabled(false);
			exitBtn.setEnabled(true);
			readyBtn.setEnabled(true);
			readyBtn.setText("Ready");
		}
	}
	public static void loseNotice(){
		msgArea.append("YOU LOSE!!!\n");
		myGame.youLose();
		answerTxt.setEnabled(false);
	}
	public static void winNotice(){
		msgArea.append("YOU WIN!!!!\n");
		myGame.youWin();
		answerTxt.setEnabled(false);
	}
	public static void readyForNewRound(){
		msgArea.append("--------------------------------------------------------------\n");
		msgArea.append("You can't rewind death.\n");
		msgArea.append("--------------------------------------------------------------\n");
		answerTxt.setEnabled(false);
	}
	public static void gameFin(){
		msgArea.append("Finished!!!\n");
		msgArea.append("--------------------------------------------------------------\n");
		changeTxtField();
		Client.sendResult();
		myUser.gameFinInit();
		isReady = false;
	}
	public static void startNextRound(){
		
		
	}
}
