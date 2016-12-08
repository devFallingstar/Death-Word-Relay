package SubClass;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import GUI.GameRoom;
import RoomClient.Client;

/**
 * This class is used for blocking a escape that execute while game is online.
 * 
 * @author YYS
 */
public class GameRoomHandler extends WindowAdapter {
	GameRoom myGame;

	/**
	 * Constructor for GameRoom GUI
	 * @param _myGame
	 */
	public GameRoomHandler(GameRoom _myGame) {
		myGame = _myGame;
	}

	/**
	 * Blocking a closing in a game room.
	 */
	public void windowClosing(WindowEvent e) {
		CloseBlockingAlert();
	}

	/**
	 * Alert to the user that user can not get out while the game is playing.
	 */
	private void CloseBlockingAlert() {
		Client.CowardAlert();
	}
}
