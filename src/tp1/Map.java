/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Map {
    
    protected ArrayList<Room> rooms;
    protected int width;
    protected int height;

    public Map(ArrayList<Room> rooms, int width, int height) {
        this.rooms = rooms;
        this.width = width;
        this.height = height;
    }
    
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void descriptionAlentoure(int x,int y){
        Room actualRoom = rooms.get(x*width+y);
        String Entrances = actualRoom.getEntrances();
        System.out.println("Description alentour:");
        int i = 0;
        while(i!=Entrances.length()){
            if(Entrances.charAt(i) == 'N'){
                System.out.print("La porte du Nord mene a :");
                System.out.println(rooms.get(x-1*width+y).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'S'){
                System.out.print("La porte du Sud mene a :");
                System.out.println(rooms.get(x+1*width+y).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'E'){
                System.out.print("La porte de l'Est mene a :");
                System.out.println(rooms.get(x*width+y+1).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'W'){
                System.out.print("La porte de l'Ouest mene a :");
                System.out.println(rooms.get(x-1*width+y-1).getName());
            }
        }
    }
}
