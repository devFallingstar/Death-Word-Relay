package Data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;


public class User implements Serializable {
	private String name;
	private BufferedReader in;
	private PrintWriter out;
	private int rNo;
	private boolean isReady;
	private boolean isPlaying;
	private int win, lose;
	private int round;
	
	public User(){
		//Make blank user.
	}
	
	public User(String _name){
		this.name = _name;
	}
	
	public User(String _name, BufferedReader _in, PrintWriter _out){
		this.name = _name;
		this.in = _in;
		this.out = _out;
		this.rNo = -1;
		isReady = false;
		this.win = 0;
		this.lose = 0;
		this.round = 0;
	}
	
	public int getrNo() {
		return rNo;
	}

	public String getName() {
		return name;
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}
	
	public void setrNo(int rNo) {
		this.rNo = rNo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}
	
	public void sendMsg(String msg){
		this.out.println(msg);
	}
	
	public void setReady(){
		isReady = true;
	}
	public void setUnReady(){
		isReady = false;
	}
	public boolean getReady(){
		return isReady;
	}
	
	public void InitRoundScore(){
		lose = 0;
		win = 0;
		round = 0;
	}
	
	public void Lose(){
		round++;
		lose++;
	}
	public void Win(){
		round++;
		win++;
	}
	public boolean isFin(){
		if(lose == 3 || win == 3){
			return true;
		}
		return false;
	}
	
	public void setPlaying(){
		isPlaying = true;
	}
	public void setUnPlaying(){
		isPlaying = false;
	}
	public boolean getPlaying(){
		return isPlaying;
	}
}
