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
//	private static HashMap<Integer, Vector> map = new HashMap<Integer, Vector>();
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
    	int i = 1;

    	while(true){
    		if(roomMap.containsKey(i)){
    			i++;
    		}else{
    			RoomManager newRoom = new RoomManager(i, roomName);
    			roomMap.put(i, newRoom);
    			break;
    		}
    	}
    	return i;
    }
    
    public static void removeRoom(int roomID){
    	roomMap.remove(roomID);
    }
    
    public static void addUserToRoom(User u){
    	RoomManager destRoom = roomMap.get(u.getrNo());
    	destRoom.myRoom.addUser(u);
    	
    	destRoom.broadCastRoom("MESSAGE [SYSTEM] "+u.getName()+" connected.");
    	
    	//자기 정보 화면 초기화 (GUI)해야함.
    }
    
    public static void removeUserFromRoom(User u){
    	RoomManager destRoom = roomMap.get(u.getrNo());
    	destRoom.myRoom.removeUser(u);
    	u.setrNo(-1);
    	
    	if(destRoom.myRoom.roomV.isEmpty()){
    		roomMap.remove(u.getrNo());
    	}else{
    		destRoom.broadCastRoom("MESSAGE [SYSTEM] "+u.getName()+" disconnected.");
    	}
    }
    
    public static void broadCast(String msg, int rNo){
    	if (rNo == -1){
    		for (User u : users){
        		u.sendMsg(msg);
        	}
    	}else{
    		RoomManager newRoom = roomMap.get(rNo);
    		
    		newRoom.broadCastRoom(msg);
    	}
    } 
    
}
