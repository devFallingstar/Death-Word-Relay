package SubClass;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import GUI.*;
import RoomClient.Client;

/**
 * This class is used for the server to check off-line users.
 * 
 * @author YYS
 */
public class AllRoomHandler extends WindowAdapter {
	Client myClnt;
	Login myLogin;
	Register myReg;
	Waiting myWait;

	static PrintWriter out;
	static BufferedReader in;

	/**
	 * Constructor for Login GUI
	 * 
	 * @param _login
	 */
	public AllRoomHandler(Login _login) {
		myLogin = _login;
	}

	/**
	 * Constructor for Register GUI
	 * 
	 * @param _reg
	 */
	public AllRoomHandler(Register _reg) {
		myReg = _reg;
	}

	/**
	 * Constructor for Waiting GUI
	 * 
	 * @param _wait
	 */
	public AllRoomHandler(Waiting _wait) {
		myWait = _wait;
	}

	/**
	 * Constructor for I/O Streams
	 * 
	 * @param _out
	 * @param _in
	 */
	public AllRoomHandler(PrintWriter _out, BufferedReader _in) {
		out = _out;
		in = _in;
	}

	/**
	 * When user click x button, before the program is closed, send protocol
	 * message that means "I'm closing now", to server.
	 */
	public void windowClosing(WindowEvent e) {
		out.println("ICLOSEMYGAMEBECAUSEIMANGRY");

		System.exit(0);
	}
}
