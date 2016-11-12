
/*
 * Handler class for allow user to join the server, or release from server.
 * Plus, broadcast its message too.
 */

package RoomServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.StringTokenizer;

public class UserHandler extends Thread {
	private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User myUser;
    
    /* 
	 * HashSet for users that joining.
	 */
	public static HashSet<String> names = new HashSet<String>();
	public static HashSet<User> users = new HashSet<User>();
    
    
    /*
     * construct handler class, and get a scoket for one client.
     * Also, almost methods are done in run() method.
     * 
     */
    public UserHandler(Socket _socket){
    	this.socket = _socket;
    }
    
    
    /*
     * Services this user client by requesting the name
     * and broadcasting, with input and output streams.
     * 
     */
    public void run(){
    	try{
    	// Create streams for the socket.
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        /*
         * Keep requesting for user's name that is not already used.
         * 
         */
        while(true){
        	out.println("SUBMITNAME");
        	name = in.readLine();
        	if (name == null){
        		continue;
        	}
        	
        	synchronized(names){
        		if(!names.contains(name)){
        			names.add(name);
        			break;
        		}
        	}
        }
        
        /*
         * If name is accepted, add it to user hashset
         * to manage its stream for broadcasting a message,
         * and care its room.
         */
        
        out.println("NAMEACCEPTED");
        out.println("MESSAGE Welcome to Death Word Relay!");
        
        if (!isNameExist(name)){
        	User newUser = new User(name, in, out);
        	users.add(newUser);
        }
        System.out.println(users);
        
        myUser = new User(name, in, out);
        Server.addUser(myUser);
        
        broadCast("MESSAGE [SYSTEM] User [" + name + "] is connected.");
        System.out.println("LOG : User ["+name+"] connected.");
        
        
        /*
         * wait for the message from client
         * and send it to others.
         * If User sends message in a room,
         * Check its room number, and send the message to room members only.
         */
        while (true) {
            String input = in.readLine();
            if (input == null) {
                return;
            }
            if (input.startsWith("/")){
            	if(input.startsWith("ENTERROOM")){
            		try{
                		StringTokenizer toks = new StringTokenizer(input.substring(7)," ");
                    	String roomTitle = toks.nextToken();
                    	int roomNo = Server.addRoom(roomTitle);
                    	this.myUser.setrNo(roomNo);
                    	
                    	Server.addUserToRoom(this.myUser);
                    	
                    	
                	}catch(Exception e){
                		out.println("MESSAGE [SYSTEM] Wrong parameter!");
                	}
            	}else if(input.equals("REQROOMLIST")){
            		
            		
            	}else if(input.equals("/clear")){
            		
            	}else{
            		out.println("MESSAGE [SYSTEM] Wrong command!");
            	}
            	
            }else{
            	broadCast("MESSAGE " + name + ": " + input);
            	System.out.println("LOG : ["+name+"] : "+input);
            }
            
            
        }
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if (name != null) {
                names.remove(name);
                broadCast("MESSAGE User [" + name + "] is disconnected.");
                System.out.println("LOG : User ["+name+"] disconnected.");
            }
            if (out != null) {
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
    	}
    }
    
    
    
    private boolean isNameExist(String name){
    	for(User requestedUser : users){
    		if (requestedUser.getName().equalsIgnoreCase(name)){
        		return true;
        	}
    	}
    	return false;
    }
    
    private PrintWriter getoutStream(String name){
    	
    	for(User requestedUser : users){
        	if (requestedUser.getName().equalsIgnoreCase(name)){
        		return requestedUser.getOut();
        	}
        }
    	return null;
    }
    
    public void broadCast(String msg){
    	
    	Server.broadCast(msg, myUser.getrNo());
//    	for(User bUser : users){
//    		bUser.getOut().println(msg);
//    	}
    }
    
}
