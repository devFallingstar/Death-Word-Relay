package RoomServer;

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	/* 
	 * port for server 
	 */
	private static final int PORT = 9001;
	private static Integer roomId = 0;
	
	private static Vector<User> users = new Vector<User>();
	private static HashMap<Integer, Vector> map = new HashMap<Integer, Vector>();
	private static HashMap<Integer, RoomManager> roomMap = new HashMap<Integer, RoomManager>();
	
	/**
     * The appplication main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running. AKAKA");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
            	if(listener.accept() != null){
            		new UserHandler(listener.accept()).start();
            		System.out.println("User entered");
            	}
            }
        } finally {
            listener.close();
        }
    }
    
    public static void addUser(User u){
    	users.add(u);
    }
    public static void removeUser(User u){
    	users.remove(u);
    }
    
    public static int addRoom(String roomName){
    	RoomManager newRoom = new RoomManager(roomName, );
    	int i = 1;
    	Vector<User> roomV = new Vector<User>();
    	while(true){
    		if(map.containsKey(i)){
    			i++;
    		}else{
    			map.put(i, roomV);
    			break;
    		}
    	}
    	return i;
    }
    
    public static void removeRoom(int roomID){
    	map.remove(roomID);
    }
    
    public static void addUserToRoom(User u){
    	Vector<User> roomV = map.get(u.getrNo());
    	roomV.add(u);
    	
    	broadCast("MESSAGE [SYSTEM] "+u.getName()+" entered in room ["+roomV.+, )
    }
    
    public static void removeUserFromRoom(User u){
    	Vector<User> roomV = map.get(u.getrNo());
    	roomV.remove(u);
    	u.setrNo(-1);
    	if(roomV.isEmpty()){
    		removeRoom(u.getrNo());
    	}
    }
    
    public static void broadCast(String msg, int rNo){
    	if (rNo == -1){
    		for (User u : users){
        		u.sendMsg(msg);
        	}
    	}else{
    		Vector<User> msgUsers = map.get(rNo);
    		for (User u : msgUsers){
    			u.sendMsg(msg);
    		}
    	}
    } 
    
}
