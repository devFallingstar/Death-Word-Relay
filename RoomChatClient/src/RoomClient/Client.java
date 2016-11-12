package RoomClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Vector;

import GUI.*;


public class Client {
	/*
	 * sockets for 
	 * input and output.
	 */
	static BufferedReader in;
    static PrintWriter out;
    Socket socket;
    
    String ID,PW;
    
    private static Login myLogin;
    private static Waiting myWait;
    
    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
    	Client myClnt = new Client();
    	myLogin = new Login();
        myWait = new Waiting();
        
    	myClnt.run();
    }
    
    public Client(){
    	
    }
    
    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
//        String serverAddress = getServerAddress();
        
        String serverAddress = "127.0.0.1";
        
        try{
        	socket = new Socket(serverAddress, 9001);
        }catch(ConnectException ce){
        	System.out.println("Server is down.");
        	System.exit(-1);
        }
        
        
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            System.out.print(line);
            if (line.startsWith("SUBMITNAME")) {
                myLogin.setVisible(true);
                
            } else if (line.startsWith("NAMEACCEPTED")) {
                myLogin.setVisible(false);
                myWait.setVisible(true);
                
                myWait.reloadRoomList();
            } else if (line.startsWith("MESSAGE")) {
            	myWait.gotMessage(line);
            }
        }
    }
    
    public void sendLoginRequest(String _ID, String _PW) throws IOException{
    	this.ID = _ID;
    	this.PW = _PW;
    	
    	if (ID.isEmpty() || PW.isEmpty()
    			|| (checkStringPattern(ID) == -1)){
    		myLogin.wrongParam();
    	}else{
    		out.println(ID+"|"+PW);
    	}
    }
    public void sendMessage(String msg) throws IOException{
    	out.println(msg);
    }
    
    public Vector getNewRoomList(){
    	out.println("REQROOMLIST");
    	
		return null;
    }
    
    /**
     * Check is there any special character in ID.
     * @param str
     * @return 0 when there's no special character
     */
    private int checkStringPattern(String str){
    	if (!str.matches("[0-9|a-z|A-Z]*")){
    		return -1;
    	}else{
    		return 0;
    	}
    }
}
