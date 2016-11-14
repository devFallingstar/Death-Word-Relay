package Data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;


public class User implements Serializable {
	private String name;
	BufferedReader in;
	PrintWriter out;
	int rNo;
	
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
	
	
}
