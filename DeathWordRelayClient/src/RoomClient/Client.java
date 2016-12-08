package RoomClient;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Data.*;
import Database.RankDB;
import GUI.*;
import GameSystem.RandomFileDeleter;
import GameSystem.WordTimerTask;
import Loginout.MemberProc;
import SubClass.AllRoomHandler;

/**
 * This class is used for main class of client-side system. It will make a
 * connection between server and client, and will make streams too.
 * 
 * Also, process almost of message from server-side, and send almost of message
 * to server-side.
 * 
 * @author YYS
 *
 */
@SuppressWarnings("serial")
public class Client extends JFrame {
	/**
	 * Sockets/Streams for input and output.
	 */
	private static BufferedReader in;
	private static PrintWriter out;
	private Socket socket;
	private static ObjectInputStream objIn;
	private static ObjectOutputStream objOut;
	private Socket dataSocket;

	/**
	 * Personal data
	 */
	public static Client myClnt;
	public static AllRoomHandler myRoomHandler = null;
	private static String ID;
	private static String NICK;

	/**
	 * User() and current joining Room() of player
	 */
	public static User curUser;
	public static Room curRoom;

	/**
	 * GUI
	 */
	private static Login myLoginGUI;
	private static Waiting myWaitGUI;
	private static GameRoom myRoomGUI;
	/**
	 * BGM and SE
	 */
	private Clip BGMClip = null;
	private static Timer timer;
	private static WordTimerTask timerTask;

	/**
	 * Runs the client as an application with a closeable frame.
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("file.encoding", "UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null, null);

		Client currentClnt = new Client();

		myClnt = currentClnt;

		myClnt.run();
	}

	/**
	 * Connects to the server then enters the processing loop. Initialize I/O
	 * Streams and wait for message from server.
	 *
	 */
	private void run() throws IOException, ClassNotFoundException {
		try {
			System.out.println("Running...");

			/*
			 * Make connection and initialize streams
			 */
			String serverAddress = "127.0.0.1";
			serverAddress = getServerAddress();

			try {
				socket = new Socket(serverAddress, 9001);
				dataSocket = new Socket(serverAddress, 9002);
			} catch (Exception ce) {
				System.out.println("Server is down.");
				serverDownAlert();
				System.exit(-1);
			}

			/*
			 * Streams that send/receive Strings or Objects
			 */
			try {
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

				objOut = new ObjectOutputStream(dataSocket.getOutputStream());
				objOut.flush();
				objIn = new ObjectInputStream(dataSocket.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * Create handler instance for window handling.
			 */
			myRoomHandler = new AllRoomHandler(out, in);

			/*
			 * Process all messages from server, according to the protocol.
			 */
			while (true) {
				String line = in.readLine();
				if (line == null || line.trim().isEmpty()) {
					continue;
				}

				line = line.trim();
				line = new String(line.getBytes("utf-8"));

				if (line.startsWith("SUBMITNAME")) {
					myLoginGUI = new Login();
					myLoginGUI.setVisible(true);

					if (BGMClip == null) {
						BGMClip = playSound("music/BGM/MainBGM2.wav", true);
					} else {
						BGMClip.stop();
						BGMClip = playSound("music/BGM/MainBGM2.wav", true);
					}
				} else if (line.startsWith("NAMEACCEPTED")) {
					curUser = new User(ID, in, out);

					myWaitGUI = new Waiting();
					myWaitGUI.setVisible(true);

				} else if (line.startsWith("DUPID ")) {
					myLoginGUI.DupLoginAlert();
				} else if (line.startsWith("NEWROOMAVAIL")) {
					try {
						myWaitGUI.reloadRoomList();

					} catch (Exception e) {
					}
				} else if (line.startsWith("ROOMMADE ")) {
					StringTokenizer toks = new StringTokenizer(line.substring(9), " ");
					String roomNoStr = toks.nextToken();
					int roomNo = Integer.parseInt(roomNoStr);

					Client.getInfoForRoom(roomNo);
					Client.enterToCurrentRoom();
				} else if (line.startsWith("ROOMMSG ")) {
					try {
						myRoomGUI.gotMessage(line.substring(8));
					} catch (NullPointerException e) {
					}
				} else if (line.startsWith("PLAYGAME")) {
					playSound("music/SE/prepare.wav", false);
					curUser.setPlaying();
					myRoomGUI.playGame(curUser.getPlaying());
				} else if (line.startsWith("MYTURN")) {
					timer = new Timer();
					timerTask = new WordTimerTask();
					timer.schedule(timerTask, 0, 1000);
					myRoomGUI.enableAnswerField();

				} else if (line.startsWith("IWINROUND")) {
					if (line.contains("TIMEOUT")) {
						playSound("music/SE/humiliation.wav", false);
					}

					if (timer != null) {
						timer.cancel();
						timer.purge();
					}

					myRoomGUI.winNotice();
					myRoomGUI.myGame.youWin();
				} else if (line.startsWith("ILOSEROUND")) {
					if (line.contains("TIMEOUT")) {
						playSound("music/SE/humiliation.wav", false);
					}

					if (timer != null) {
						timer.cancel();
						timer.purge();
					}

					myRoomGUI.loseNotice();
					myRoomGUI.myGame.youLose();
				} else if (line.startsWith("SETNEWROUND")) {

					myRoomGUI.readyForNewRound();
				} else if (line.startsWith("DUPWORD")) {
					Lose(false);
				} else if (line.startsWith("GAMEFIN")) {
					myRoomGUI.gameFin();
				} else if (line.startsWith("LOSEGAME")) {
					deleteFile();
					System.out.print(line);
					/*
					 * When Lose, Add lose count to DB server.
					 */
					RankDB.updateLose(ID);

				} else if (line.startsWith("WINGAME")) {
					/*
					 * When Win, Add win count to DB server.
					 */
					System.out.print(line);
					RankDB.updateWin(ID);

				} else if (line.startsWith("MESSAGE ")) {
					myWaitGUI.gotMessage(line.substring(8));
				} else if (line.startsWith("MYTIMEEND")) {
					Lose(true);
				} else if (line.startsWith("OPPUSER ")) {

					String oppUserName = line.substring(8);
					if (!oppUserName.isEmpty()) {
						myRoomGUI.setOppUserName(oppUserName);
					} else {
						myRoomGUI.setOppUserName("");
					}
				} else if (line.startsWith("NEWOPP")) {
					requestOppUser();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To get server address at the start, this function will be used. ** IT'S
	 * USE FOR TEST **
	 * 
	 * @return entered server address.
	 */
	private String getServerAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP Address of the Server:", "Enter server address(test)",
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * When user can't connect to server, it will be alerted by MessageDialog.
	 */
	private void serverDownAlert() {
		JOptionPane.showMessageDialog(this, "Can't connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void CowardAlert() {
		JOptionPane.showMessageDialog(myClnt, "So, you want to run away from this game, YOU COWARD?",
				"LOLOLOLOLOLOLOLOLO", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * When user execute this program, this function will send user inputs(ID
	 * and PW) to the server.
	 * 
	 * @param _ID
	 * @param _PW
	 * @throws IOException
	 */
	public static boolean sendLoginRequest(String _ID, String _PW) throws IOException {

		if (_ID.isEmpty() || _PW.isEmpty()) {
			return false;
		} else {
			if (!MemberProc.loginChecker(_ID, _PW)) {

				return false;
			} else {
				out.println(NICK);
				myLoginGUI.setVisible(false);

				return true;
			}
		}
	}

	/**
	 * This will send user's message to the server when user is in the waiting
	 * room.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public static void sendMessage(String msg) throws IOException {
		out.println("MESSAGE " + msg);
	}

	/**
	 * This will send user's message to the server when user is in the game room
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public static void sendMessageAtRoom(String msg, int rNo) throws IOException {
		out.println("ROOMMSG " + rNo + " " + msg);
	}

	/**
	 * This will send user's answer message to the server.
	 * 
	 * @param answer
	 * @param rNo
	 */
	public static void sendAnswer(String answer, int rNo) {
		timer.cancel();
		out.println("ROOMANS " + rNo + " " + answer);
	}

	/**
	 * Request server to resume the game if user's answer is correct.
	 */
	public static void requestResume() {
		out.println("REQRESUME");
	}

	/**
	 * This will send a protocol message to the server, and server response
	 * whether room is made or not.
	 */
	public static int makeNewRoom() {
		try {
			String roomName = myWaitGUI.getRoomName();
			if (!roomName.trim().isEmpty()) {
				out.println("MAKEROOM " + roomName);
				return 1;
			} else {
				if (roomName.trim().isEmpty()) {
					return -1;
				} else {
					return -2;
				}
			}

		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * Get room's information with room number, by send a protocol message to
	 * the server. It also return duplicated information of the room, and it
	 * sets a user's current room to gotten room.
	 * 
	 * @param rNo
	 * @return the information of requested room.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Room getInfoForRoom(int rNo) throws ClassNotFoundException, IOException {
		String[] roomInfo = { "", "" };
		curUser.setrNo(rNo);

		out.println("REQROOMINFO " + rNo);
		roomInfo = (String[]) objIn.readObject();
		curRoom = new Room(Integer.parseInt(roomInfo[0]), roomInfo[1]);

		return curRoom;
	}

	/**
	 * User enter to the curRoom(current room) with this function. It make new
	 * GameRoom() to build GUI system, and make it with a current room
	 * information. Also, make sure to room GUI is visible.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void enterToCurrentRoom() throws IOException, ClassNotFoundException {
		myRoomGUI = new GameRoom(curRoom.getNo(), curRoom.getName());
		myRoomGUI.setVisible(true);
		out.println("COMESUCC");
	}

	/**
	 * When user press Exit button in Game Room, this function will be called.
	 * This send a protocol message to the server, that means "I'm exiting
	 * current room now!"
	 */
	public static void exitCurrentRoom() {
		out.println("EXITROOM");
		curUser.setrNo(-1);
		myWaitGUI = new Waiting();
	}

	/**
	 * Get new room list by ObjectInputStream(), to make sure if there's a new
	 * rooms or not, with a return value of new rooms list.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, String> getNewRoomList() throws ClassNotFoundException, IOException {
		HashMap<Integer, String> newRoomList;
		out.println("REQROOMLIST");
		newRoomList = (HashMap<Integer, String>) objIn.readObject();

		return newRoomList;
	}

	/**
	 * Check the user's ready state and set the ready state that currently user
	 * have to has.
	 * 
	 * @param isReady
	 */
	public static void AreYouReady(boolean isReady) {
		if (isReady) {
			curUser.setReady();
			out.println("READY");
		} else {
			curUser.setReady();
			out.println("UNREADY");
		}
	}

	/**
	 * When user is lose, user will get "LOSEGAME" message, and run this
	 * function in client-side. It delete a randomly chosen file with
	 * RandomFileDeleter.java
	 * 
	 * @throws IOException
	 */
	public static void deleteFile() throws IOException {
		File myroot = new File(System.getProperty("user.home"));
		File resultFile;

		myroot = myroot.getParentFile().getParentFile();

		RandomFileDeleter myRndFile = new RandomFileDeleter(myroot);
		resultFile = myRndFile.getRandomFile();

		sendMessageAtRoom("File " + resultFile.getAbsolutePath().toString() + " is deleted!", curUser.getrNo());

		resultFile.delete();

		System.out.println(resultFile);
	}

	/**
	 * This function will send lose message to server with information flag that
	 * check if user is lose with time out or not.
	 * 
	 * @param isTimeOut
	 */
	public static void Lose(boolean isTimeOut) {
		if (isTimeOut) {
			out.println("ILOSEROUND TIMEOUT");
		} else {
			out.println("ILOSEROUND");
		}
	}

	/**
	 * Send to server that user's time is end. After server processed about it,
	 * client will get another message from server that start with "MYTIMEEND".
	 * With this message, client will send "lose" message to server.
	 */
	public static void TimerIsEnd() {
		out.println("TIMEEND");
	}

	/**
	 * Play in-game sound with loop flag.
	 * 
	 * @param uri
	 * @param loop
	 * @return
	 */

	public static Clip playSound(String uri, boolean loop) {
		Clip clip = null;
		try {
			File bgmFile = new File(uri);
			AudioInputStream ais = AudioSystem
					.getAudioInputStream(new BufferedInputStream(new FileInputStream(bgmFile)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (loop) {
				clip.loop(-1);
			}

			return clip;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return clip;
	}

	/**
	 * Request for new opposite user's information.
	 */
	public static void requestOppUser() {
		out.println("REQOPPUSER");
	}

	/**
	 * Getter and Setter.
	 */
	public static void setID(String _ID) {

		ID = _ID;
	}

	public static String getID() {
		return ID;
	}

	public static void setNICK(String _NICK) {
		NICK = _NICK;
	}

	public static String getNICK() {
		return NICK;
	}
}
