package GameSystem;

import java.util.TimerTask;

import RoomClient.Client;

/**
 * This class is used for check timer of the game. It will check if the user
 * right a word in 10 seconds or not.
 * 
 * @author YYS
 *
 */
public class WordTimerTask extends TimerTask {
	int time;

	public WordTimerTask() {
		this.time = 11;
	}

	@Override
	public void run() {
		this.time--;
		System.out.println(this.time);
		if (this.time == 0) {
			Client.TimerIsEnd();
			this.time = 11;
			this.cancel();
		}
	}
}
