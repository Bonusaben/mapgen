package mapgen;

//import java.awt.List;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel{

    private int boxSize;
    static Random rand = new Random();
    static List<List<Room>> rooms = new ArrayList<>(); 
    static int roomCount;
    static int maxRooms = 25;
    static int roomWidth = 64;
    static int roomHeight = 32;
    static int roomMargin = 4;
    static int doorWidth = 8;
    static int doorHeight = 6;
    static int mapWidth = 5; //number of rooms across
    static int mapHeight = 5; //number of rooms down
    static String roomColorCode = "#468499";
    static String doorColorCode = "#a91818";
    //Color roomColor,doorColor,startColor;
    static private HashMap<Integer, String[]> roomTypes = new HashMap<Integer,String[]>();
    
    
    public static void main(String[] args) {
        JFrame f = new JFrame("MapGen");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new Main());
        //f.setUndecorated(true);
        f.setSize((mapWidth*roomWidth)+(mapWidth*(roomMargin+1)), (mapHeight*roomHeight)+(mapHeight*(roomMargin+1)));
        //System.out.println(f.getSize());
        f.setLocation(550, 25);
        f.setVisible(true);
        
        setRoomTypes();
        
        generateMapArray(mapWidth,mapHeight);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        renderMap(g,g2);
    }
    
    
    public static void generateMapArray(int mapWidth, int mapHeight){
        // Set the number of rooms dependant on the size of the map
        //maxRooms = mapHeight+mapWidth;
        
        // Get a starting location
        int randLocX = rand.nextInt(mapWidth-1);
        int randLocY = rand.nextInt(mapHeight-1);
                
        // Initialize empty map array
        for(int i=0;i<mapHeight;i++){
            rooms.add(new ArrayList<>());
            for(int j=0;j<mapWidth;j++){
                rooms.get(i).add(j,null);
            }
        }
        
        traverseRooms(randLocX,randLocY,true,null);
    
    }
    
    private static void renderMap(Graphics g,Graphics2D g2){
        rooms.forEach((l) -> { 
            l.forEach((r) -> {
                if(r!=null){
                    r.updateRoomType();
                } else {
                    
                }
            });
        });
        
        for(int i=0;i<rooms.size();i++){
            for(int j=0; j<rooms.get(i).size();j++){
                // Draw rooms
                if(rooms.get(i).get(j)!=null){
                    // Calculate room center
                    Point p = new Point();
                    p.x = roomMargin+(j*roomWidth)+(j*roomMargin)+(roomWidth/2);
                    p.y = roomMargin+(i*roomHeight)+(i*roomMargin)+(roomHeight/2);
                    
                    // Make room
                    g.setColor(Color.decode(roomColorCode));
                    //g2.fillRect(roomMargin+(j*roomWidth)+(j*roomMargin), roomMargin+(i*roomHeight)+(i*roomMargin), roomWidth, roomHeight);
                    g2.fillRect(p.x-roomWidth/2, p.y-roomHeight/2, roomWidth, roomHeight);
                    
                    // Make door
                    g.setColor(Color.decode(doorColorCode));
                    if((boolean)rooms.get(i).get(j).getOpenDoors().get("north")){
                        g2.fillRect(p.x-doorWidth/2,p.y-roomHeight/2,doorWidth,doorHeight);
                    }
                    
                    if((boolean)rooms.get(i).get(j).getOpenDoors().get("east")){
                        g2.fillRect(p.x+roomWidth/2-doorHeight,p.y-doorWidth/2,doorHeight,doorWidth);
                    }
                    
                    if((boolean)rooms.get(i).get(j).getOpenDoors().get("south")){
                        g2.fillRect(p.x-doorWidth/2,p.y+roomHeight/2-doorHeight,doorWidth,doorHeight);
                    }
                    
                    if((boolean)rooms.get(i).get(j).getOpenDoors().get("west")){
                        g2.fillRect(p.x-roomWidth/2,p.y-doorWidth/2,doorHeight,doorWidth);
                    }
                    
                } else {
                    
                }
            }
        }        
    }
    
    
    private static void traverseRooms(int currentX, int currentY, Boolean startRoom, String prevRoomDir) {
        String[] dirs = {"north","east","south","west"};
        String dir = dirs[rand.nextInt(dirs.length)];
        
        // Check if room has already been visited
        if(rooms.get(currentY).get(currentX)!=null){
            Room room = rooms.get(currentY).get(currentX);
            //room.openDoor(prevRoomDir);
            switch(dir){
                case "north": 
                    if(currentY>0){
                        if(rooms.get(currentY-1).get(currentX)==null){
                                room.openDoor(dir);
                            }
                        traverseRooms(currentX,currentY-1,false,"south");
                    } else {
                        traverseRooms(currentX,currentY,false,prevRoomDir);
                    }
                    break;
                case "east": 
                    if(currentX<rooms.get(currentY).size()-1){
                        if(rooms.get(currentY).get(currentX+1)==null){
                                room.openDoor(dir);
                            }
                        traverseRooms(currentX+1,currentY,false,"west");
                    } else {
                        traverseRooms(currentX,currentY,false,prevRoomDir);
                    }
                    break;
                case "south": 
                    if(currentY<rooms.size()-1){
                        if(rooms.get(currentY+1).get(currentX)==null){
                                room.openDoor(dir);
                            }
                        traverseRooms(currentX,currentY+1,false,"north");
                    } else {
                        traverseRooms(currentX,currentY,false,prevRoomDir);
                    }                    
                    break;
                case "west": 
                    if(currentX>0){
                        if(rooms.get(currentY).get(currentX-1)==null){
                                room.openDoor(dir);
                            }
                        traverseRooms(currentX-1,currentY,false,"east");
                    } else {
                        traverseRooms(currentX,currentY,false,prevRoomDir);
                    }                    
                    break;
                default:
                    break;
            }
        } else {
        
            Room room = new Room();
            if(startRoom) {
                room.setRoomContents("Start");
            }

            rooms.get(currentY).set(currentX,room);
            roomCount++;
            room.openDoor(prevRoomDir);
            if(roomCount<maxRooms){
                switch(dir){
                    case "north": 
                        if(currentY>0){
                            if(rooms.get(currentY-1).get(currentX)==null){
                                room.openDoor(dir);
                            }
                            traverseRooms(currentX,currentY-1,false,"south");
                        } else {
                            traverseRooms(currentX,currentY,false,prevRoomDir);
                        }
                        break;
                    case "east": 
                        if(currentX<rooms.get(currentY).size()-1){
                            if(rooms.get(currentY).get(currentX+1)==null){
                                room.openDoor(dir);
                            }
                            traverseRooms(currentX+1,currentY,false,"west");
                        } else {
                            traverseRooms(currentX,currentY,false,prevRoomDir);
                        }
                        break;
                    case "south": 
                        if(currentY<rooms.size()-1){
                            if(rooms.get(currentY+1).get(currentX)==null){
                                room.openDoor(dir);
                            }
                            traverseRooms(currentX,currentY+1,false,"north");
                        } else {
                            traverseRooms(currentX,currentY,false,prevRoomDir);
                        }                    
                        break;
                    case "west": 
                        if(currentX>0){
                            if(rooms.get(currentY).get(currentX-1)==null){
                                room.openDoor(dir);
                            }
                            traverseRooms(currentX-1,currentY,false,"east");
                        } else {
                            traverseRooms(currentX,currentY,false,prevRoomDir);
                        }                    
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    private static void setRoomTypes() {
        roomTypes.put(1, new String[] {"north"});
        roomTypes.put(2, new String[] {"east"});
        roomTypes.put(3, new String[] {"north","east"});
        roomTypes.put(4, new String[] {"south"});
        roomTypes.put(5, new String[] {"north","south"});
        roomTypes.put(6, new String[] {"east","south"});
        roomTypes.put(7, new String[] {"north","east","south"});
        roomTypes.put(8, new String[] {"west"});
        roomTypes.put(9, new String[] {"north","west"});
        roomTypes.put(10, new String[] {"east","west"});
        roomTypes.put(11, new String[] {"north","east","west"});
        roomTypes.put(12, new String[] {"south","west"});
        roomTypes.put(13, new String[] {"north","south","west"});
        roomTypes.put(14, new String[] {"east","south","west"});
        roomTypes.put(15, new String[] {"north","east","south","west"});
    }
    
}