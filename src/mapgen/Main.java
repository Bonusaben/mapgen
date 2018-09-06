/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel{

    private int boxSize;
    static Random rand = new Random();
    static List<List<Integer>> mapArray = new ArrayList<>(); 
    static int roomCount = 0;
    static int maxRooms = 10;
    
    public static void main(String[] args) {
        JFrame f = new JFrame("MapGen");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new Main());
        f.setSize(290, 325);
        f.setLocation(550, 25);
        f.setVisible(true);
        
        generateMapArray(8,8);
        
        renderMap();
    }
    
    /*
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //custom color
        String hexColor = "0x45e5B";
        g.setColor(Color.decode(hexColor));
        //draw a line (starting x,y; ending x,y)
        //g.drawLine(10, 10, 40, 10);
        g2.drawRect(10, 20, 150, 40);
        g2.setColor(Color.decode(hexColor));
    }
    */
    
    public static void generateMapArray(int mapWidth, int mapHeight){
        maxRooms = mapHeight+mapWidth;
        int randLocX = rand.nextInt(mapWidth-1);
        int randLocY = rand.nextInt(mapHeight-1);
        //int currentX = randLocX;
        //int currentY = randLocY;
        //Boolean walking = true;
        
        //Initialize empty map array
        for(int i=0;i<mapHeight;i++){
            mapArray.add(new ArrayList<>());
            for(int j=0;j<mapWidth;j++){
                mapArray.get(i).add(j,0);
                
            }
        }
        
        createRooms(randLocX,randLocY,true);
        
    
    }
    
    private static void renderMap(){
        for(List l : mapArray){
            System.out.println(l);
        }
        //System.out.println(mapArray.toString());
    }
    
    private static void createRooms(int x, int y, Boolean startRoom) {
        String[] dir = {"east","south","west","north"};
        
        //Open door?
        if(rand.nextBoolean() || startRoom && roomCount<maxRooms){
            
            //Pick direction
            switch(dir[rand.nextInt(dir.length)]){
                case "east":
                    if(mapArray.get(x+1).get(y)!=1){
                        mapArray.get(x+1).set(y,1);
                        roomCount++;
                    }
                    break;
                case "south":
                    break;
                case "west":
                    break;
                case "north":
                    break;
                default:
                    break;
            }
        }
    }
    
}

/*

Tile numbers: 
  8
4 - 1
  2

*/