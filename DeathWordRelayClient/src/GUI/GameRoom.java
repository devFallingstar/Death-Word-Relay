package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	private static ImageIcon bgImg = new ImageIcon("Img/gameRoomBg.png");
	private ImageIcon chat = new ImageIcon("Img/gchatBg.png");
	private static ImageIcon readyImg = new ImageIcon("Img/readyBt.png");
	private static ImageIcon unreadyImg = new ImageIcon("Img/unreadyBt.png");
	private static ImageIcon exitImg = new ImageIcon("Img/exitBt.png");
	private static ImageIcon cptImg = new ImageIcon("Img/userImg.png");

	private JPanel chatPanel = new JPanel() {

		public void paintComponent(Graphics g) {
			g.drawImage(chat.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);
		}
	};

	private static JLabel background = new JLabel(bgImg);
	private static JLabel competitor = new JLabel(cptImg);

	private static JTextField msgTxt = new JTextField(41);
	private static JTextField answerTxt = new JTextField(41);
	private static JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);

	private static JButton exitBtn = new JButton(exitImg);
	private static JButton readyBtn = new JButton(readyImg);

	private Container cont;

	public GameRoom(int rNo, String title) {
		super(rNo + ". " + title);

		isReady = false;
		this.myUser = Client.curUser;

		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 1080, 628);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		cont = this.getContentPane();

		cont.add(chatPanel);
		cont.add(readyBtn);
		cont.add(exitBtn);
		cont.add(competitor);
		cont.add(background);

		msgScrlPane.setBounds(23, 30, 529, 430);
		msgScrlPane.setOpaque(false);
		msgScrlPane.getViewport().setOpaque(false);

		msgTxt.setBounds(23, 490, 529, 31);
		msgTxt.setOpaque(false);
		msgTxt.setForeground(Color.GRAY);

		msgArea.setSize(529, 430);
		msgArea.setOpaque(false);
		msgArea.setEditable(false);
		msgArea.setForeground(Color.white);

		answerTxt.setBounds(23, 490, 529, 31);

		chatPanel.setBounds(23, 30, 555, 540);

		// readyBtn.setPressedIcon(unreadyImg);
		readyBtn.setBounds(670, 345, readyImg.getIconWidth(), readyImg.getIconHeight());
		readyBtn.setBackground(Color.red);
		readyBtn.setBorderPainted(false);
		readyBtn.setFocusPainted(false);
		readyBtn.setContentAreaFilled(false);

		exitBtn.setBounds(910, 510, exitImg.getIconWidth(), exitImg.getIconHeight());
		exitBtn.setBackground(Color.red);
		exitBtn.setBorderPainted(false);
		exitBtn.setFocusPainted(false);
		exitBtn.setContentAreaFilled(false);

		competitor.setBounds(640, 40, cptImg.getIconWidth(), cptImg.getIconHeight());

		background.setBounds(0, 0, bgImg.getIconWidth(), bgImg.getIconHeight());

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
					msgArea.append("Waiting for other player...\n");
					readyBtn.setIcon(unreadyImg);
					exitBtn.setEnabled(false);
				} else {
					msgArea.append("Press Ready button when your ready to play!\n");
					readyBtn.setIcon(readyImg);
					exitBtn.setEnabled(true);
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

		msgArea.setText("");
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

	public void checkAndSendAnswer() {
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
				if (!(myGame.checkLength() && myGame.checkWithOnline())) {
					Client.Lose();
				} else {
					Client.requestResume();
					answerTxt.setEnabled(false);
				}
			} else {
				lastWord = lastLine.split(":")[1].trim();

				myGame.setPrevWord(lastWord);
				myGame.setCurrentWord(msg);

				Client.sendAnswer(msg, myUser.getrNo());
				if (myGame.isCorrect()) {
					Client.requestResume();
					answerTxt.setEnabled(false);
				} else {
					Client.Lose();
				}
			}
		} catch (Exception e1) {

		}
	}

	public static void enableAnswerField() {
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

	public static void loseNotice() {
		msgArea.append("YOU LOSE!!!\n");
		myGame.youLose();
		answerTxt.setEnabled(false);
	}

	public static void winNotice() {
		msgArea.append("YOU WIN!!!!\n");
		myGame.youWin();
		answerTxt.setEnabled(false);
	}

	public static void readyForNewRound() {
		msgArea.append("--------------------------------------------------------------\n");
		msgArea.append("You can't rewind death.\n");
		msgArea.append("--------------------------------------------------------------\n");
		answerTxt.setEnabled(false);
	}

	public static void gameFin() {
		msgArea.append("Finished!!!\n");
		msgArea.append("--------------------------------------------------------------\n");
		changeTxtField();
		Client.sendResult();
		myUser.gameFinInit();
		isReady = false;
		readyBtn.setIcon(readyImg);
	}
}
