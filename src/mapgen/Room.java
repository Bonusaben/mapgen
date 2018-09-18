/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgen;

import java.util.HashMap;

/**
 *
 * @author renha
 */
public class Room {
    private int roomType; // Where doors are open
    private String roomContents = "empty";
    private HashMap<String, Boolean> openDoors;
    
    public Room(){
        this.openDoors = new HashMap<>();
        this.openDoors.put("north", false);
        this.openDoors.put("east", false);
        this.openDoors.put("south", false);
        this.openDoors.put("west", false);
    }
    
    public HashMap getOpenDoors(){
        return openDoors;
    }
    
    public void openDoor(String dir){
        if(dir!=null){
            this.openDoors.put(dir, true);
        }
    }
    
    public void closeDoor(String dir){
        this.openDoors.put(dir, false);
    }
    
    public void updateRoomType(){
        int typeCounter = 0;
        if (this.openDoors.get("north").equals(true)){ typeCounter += 1; }
        if (this.openDoors.get("east").equals(true)){ typeCounter += 2; }
        if (this.openDoors.get("south").equals(true)){ typeCounter += 4; }
        if (this.openDoors.get("west").equals(true)){ typeCounter += 8; }
        this.roomType = typeCounter;
    }
    
    public int getRoomType(){
        return roomType;
    }
    
    public void setRoomContents(String s){
        this.roomContents = s;
    }
    
    public String getRoomContents(){
        return roomContents;
    }
}
