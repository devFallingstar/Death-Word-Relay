package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Data.User;
import Database.RankDB;
import GameSystem.WordGame;
import RoomClient.*;
import SubClass.WindowHandler;

/**
 * This class is used for define a GUI system of game room, and process all of
 * methods that execute in game room.
 * 
 * @author YYS
 *
 */
@SuppressWarnings("serial")
public class GameRoom extends JFrame {

	/* Basic variables */
	public WordGame myGame;
	private boolean isReady;
	private User myUser;

	/* Get Opposite user info */
	String oppName;
	double oppRate;
	/* Basic background GUI variables */
	private ImageIcon bgImg = new ImageIcon("Img/gameRoomBg.png");
	private ImageIcon chat = new ImageIcon("Img/gchatBg.png");
	private ImageIcon readyImg = new ImageIcon("Img/readyBt.png");
	private ImageIcon unreadyImg = new ImageIcon("Img/unreadyBt.png");
	private ImageIcon exitImg = new ImageIcon("Img/exitBt.png");
	private ImageIcon cptImg = new ImageIcon("Img/userImg.png");

	private JLabel background = new JLabel(bgImg);
	private JLabel competitor = new JLabel(cptImg);
	private JLabel compId = new JLabel("");
	private JLabel compR = new JLabel("");

	private JButton exitBtn = new JButton(exitImg);
	private JButton readyBtn = new JButton(readyImg);

	/* countDown */
	private static ImageIcon oneImg = new ImageIcon("Img/count1.png");
	private static ImageIcon twoImg = new ImageIcon("Img/count2.png");
	private static ImageIcon threeImg = new ImageIcon("Img/count3.png");
	private static ImageIcon fourImg = new ImageIcon("Img/count4.png");
	private static ImageIcon fiveImg = new ImageIcon("Img/count5.png");
	private static ImageIcon startImg = new ImageIcon("Img/start.png");

	/* Basic Chatting panel */
	private JPanel chatPanel = new JPanel() {

		public void paintComponent(Graphics g) {
			g.drawImage(chat.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponent(g);
		}
	};

	private static JLabel countDown = new JLabel();

	private static JTextField msgTxt = new JTextField(41);
	private static JTextField answerTxt = new JTextField(41);
	private static JTextArea msgArea = new JTextArea(9, 65);
	private JScrollPane msgScrlPane = new JScrollPane(msgArea);

	/* Default container */
	private Container cont;

	/**
	 * Constructor for GUI system.
	 * 
	 * @param room-number
	 * @param room-title
	 */
	public GameRoom(int rNo, String title) {
		/* Initialize default informations */
		super(rNo + ". " + title);
		this.myUser = Client.curUser;
		addWindowListener(new WindowHandler(this));

		/* Set default frame informations */
		this.getContentPane().setLayout(null);
		this.setBounds(0, 0, 1080, 628);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		chatPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);

		/* Set default container and add components */
		cont = this.getContentPane();
		cont.add(compId);
		cont.add(compR);
		cont.add(chatPanel);
		cont.add(readyBtn);
		cont.add(exitBtn);
		cont.add(countDown);
		cont.add(competitor);
		cont.add(background);

		/* Initialize default background design and system */
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
		compId.setBounds(900, 133, 80, 25);
		compId.setFont(new Font("chiller", Font.BOLD, 25));
		compId.setForeground(Color.red);
		compR.setBounds(900, 233, 80, 25);
		compR.setFont(new Font("chiller", Font.BOLD, 25));
		compR.setForeground(Color.red);

		background.setBounds(0, 0, bgImg.getIconWidth(), bgImg.getIconHeight());

		/* Initialize default chatting panel's design and system */
		chatPanel.setBounds(23, 30, 555, 540);
		chatPanel.add(msgScrlPane);
		chatPanel.add(msgTxt);
		chatPanel.add(answerTxt);

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
		msgArea.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
		answerTxt.setBounds(23, 490, 529, 31);

		/* Initialize answer text field and text area for chatting */
		chatPanel.setBounds(23, 30, 555, 540);

		readyBtn.setBounds(670, 345, readyImg.getIconWidth(), readyImg.getIconHeight());
		readyBtn.setBackground(Color.red);
		readyBtn.setBorderPainted(false);
		readyBtn.setFocusPainted(false);
		readyBtn.setContentAreaFilled(false);

		countDown.setBounds(900, 330, fiveImg.getIconWidth(), fiveImg.getIconHeight());

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
		msgArea.setText("");

		/* Send message to server with Client.sendMessageAtRoom() */
		msgTxt.addActionListener(new ActionListener() {
			/**
			 * Responds to pressing the enter key in the textfield by sending
			 * the contents of the text field to the server. Then clear the text
			 * area in preparation for the next message. If user input "/clear",
			 * all of log are cleared.
			 */
			public void actionPerformed(ActionEvent e) {
				String msg = msgTxt.getText();
				if (!msg.trim().isEmpty()) {
					try {
						msg = msg.trim();
						msg = new String(msg.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e2) {
						e2.printStackTrace();
					}

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
			}
		});

		/* Send answer to server with checkAndSendAnswer() */
		answerTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkAndSendAnswer();
				answerTxt.setText("");
			}
		});

		/* Set ready state to true or false, and notice it to server */
		readyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isReady = !isReady;
				Client.AreYouReady(isReady);
				if (isReady) {
					msgArea.append("[SYSTEM] Waiting for other player...\n");
					readyBtn.setIcon(unreadyImg);
					exitBtn.setEnabled(false);
				} else {
					msgArea.append("[SYSTEM] Press Ready button when your ready to play!\n");
					readyBtn.setIcon(readyImg);
					exitBtn.setEnabled(true);
				}
			}
		});

		/*
		 * Get out of game room. X button will not working while user is in game
		 * room
		 */
		exitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.exitCurrentRoom();
				dispose();
			}
		});
	}

	/**
	 * When client got room message, this method will print it to message area.
	 * 
	 * @param message
	 *            from server.
	 */
	public void gotMessage(String msg) {
		msgArea.append(msg + "\n");

		reloadMsgArea();
	}

	/**
	 * While 2 of users are ready at the same time, game will start by notice of
	 * game rule to each user.
	 * 
	 * @param playing
	 */
	public void playGame(boolean playing) {
		if (playing) {
			msgArea.append("--------------------------------------------------------------\n");
			msgArea.append("[RULE] The game system is [Best of Five].\n");
			msgArea.append("[RULE] From now on, all of uncorrectable message will make you LOSE.\n");
			msgArea.append("[RULE] Think carefully before you type your word.\n");
			myGame = new WordGame();
			changeTxtField();
			answerTxt.setEnabled(false);
		} else {
			exitBtn.setEnabled(true);
			readyBtn.setEnabled(true);
		}
		reloadMsgArea();
	}

	/**
	 * Check if the word is correct or not and send a result to server.
	 */
	public void checkAndSendAnswer() {
		try {
			String msg = answerTxt.getText();
			msg = msg.trim();
			if (!msg.isEmpty()) {
				try {
					msg = new String(msg.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}
				String lastLine = null, lastWord;

				/* Get last line of msgArea */
				for (String line : msgArea.getText().split("\n")) {
					lastLine = line;
				}

				/* Check the word is correct or not */
				lastWord = lastLine.split(":")[1].trim();

				myGame.setPrevWord(lastWord);
				myGame.setCurrentWord(msg);

				Client.sendAnswer(msg, myUser.getrNo());
				if (myGame.isCorrect()) {
					Client.requestResume();
					answerTxt.setEnabled(false);
				} else {
					Client.Lose(false);
				}
			}
		} catch (Exception e1) {
		}
	}

	/**
	 * Enable answer text field if user got MYTURN message.
	 */
	public void enableAnswerField() {
		answerTxt.setEnabled(true);
	}

	/**
	 * Change text field between answer field and message field.
	 */
	public void changeTxtField() {
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
		reloadMsgArea();
	}

	/**
	 * If current user is lose, it prints a lose message to message area.
	 */
	public void loseNotice() {
		msgArea.append("YOU LOSE!!!\n");
		myGame.youLose();
		answerTxt.setEnabled(false);
		reloadMsgArea();
	}

	/**
	 * If current user is win, it prints a win message to message area.
	 */
	public void winNotice() {
		msgArea.append("YOU WIN!!!!\n");
		myGame.youWin();
		answerTxt.setEnabled(false);
		reloadMsgArea();
	}

	/**
	 * If new round is started, it prints a new round notice to message area.
	 */
	public void readyForNewRound() {
		if (!((myGame.getLose() == 3) || (myGame.getWin() == 3))) {
			msgArea.append("--------------------------------------------------------------\n");
			msgArea.append("--------------------------------------------------------------\n");
			msgArea.append("[SYSTEM] Game will start in 5 seconds...\n");
			msgArea.append("[SYSTEM] You can't rewind death.\n");
			reloadMsgArea();
			fiveCountDown();
			answerTxt.setEnabled(false);
		}
	}

	/**
	 * If game is finished, it prints a game finished message to message area.
	 */
	public void gameFin() {
		msgArea.append("--------------------------------------------------------------\n");
		msgArea.append("--------------------------------------------------------------\n");
		msgArea.append("[SYSTEM] Game Set.\n");
		msgArea.append("--------------------------------------------------------------\n");
		reloadMsgArea();
		changeTxtField();
		myUser.gameFinInit();
		isReady = false;
		readyBtn.setIcon(readyImg);
	}

	/**
	 * Make count down image before start match.
	 */
	public static void fiveCountDown() {
		countDown.setVisible(true);
		countDown.setIcon(fiveImg);
		sleep(1000);
		countDown.setIcon(fourImg);
		sleep(1000);
		countDown.setIcon(threeImg);
		sleep(1000);
		countDown.setIcon(twoImg);
		sleep(1000);
		countDown.setIcon(oneImg);
		sleep(1000);
		countDown.setIcon(startImg);
		sleep(1000);
		countDown.setVisible(false);
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Reload message area when new message income.
	 */
	public void reloadMsgArea() {
		int pos;
		pos = msgArea.getText().length();
		msgArea.setCaretPosition(pos);
		msgArea.requestFocus();
		msgTxt.requestFocus();
	}

	/**
	 * Set opposite user label to opposite user's name.
	 * 
	 * @param _name
	 */
	public void setOppUserName(String _name) {
		if (_name.isEmpty()) {
			compId.setText("");
			oppRate = 0;
			compR.setText(oppRate + "%");
		} else {
			compId.setText(_name);
			oppRate = RankDB.getRate(_name);
			compR.setText(oppRate + "%");

		}
	}
}
