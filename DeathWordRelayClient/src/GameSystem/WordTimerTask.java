package GameSystem;

import java.util.TimerTask;

import RoomClient.Client;

public class WordTimerTask extends TimerTask {

	static int time = 11;

	@Override
	public void run() {
		time--;
		System.out.println(time);
		if (time == 0) {
			Client.TimerIsEnd();
			time = 11;
			this.cancel();
		}
	}
}
