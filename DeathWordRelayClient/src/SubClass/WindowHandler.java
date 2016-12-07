package SubClass;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import GUI.GameRoom;

/**
 * This class is used for blocking a escape that execute while game is online.
 * 
 * @author YYS
 */
public class WindowHandler extends WindowAdapter {
	GameRoom myGame;

	public WindowHandler(GameRoom _myGame) {
		myGame = _myGame;
	}

	public void windowClosing(WindowEvent e) {
		fullRoomAlert();
	}

	private void fullRoomAlert() {
		JOptionPane.showMessageDialog(myGame, "So, you want to run away from this game, YOU COWARD?");
	}
}
