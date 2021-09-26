/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Map {
    
    protected Character hero;
    protected ArrayList<Room> rooms;
    protected int width;
    protected int height;

    public Map(ArrayList<Room> rooms, Character hero, int width, int height) {
        this.rooms = rooms;
        this.hero = hero;
        this.width = width;
        this.height = height;
    }
    
    public void buildFromInputStream(InputStream istream) throws Exception, NumberFormatException {
        
        
        Parser p = new Parser(new Stream(istream));
        
        boolean mapsize = false;
        
        int buildedrooms = 0;
        
        while (true) {
            Token token = p.getNextToken();
            
            // if token is "!"
            if (token.getData().equals("!")) {
                if (mapsize) throw new Exception("Map size already defined");
                this.width = Integer.parseInt(p.getNextToken().getData());
                this.height = Integer.parseInt(p.getNextToken().getData());
                mapsize = true;
                continue;
            }
            
            // if comment '#' and mapsize not defined then ignore
            if (!mapsize && token.getData().charAt(0)=='#') continue;
            
            // else, map size must be defined
            if (!mapsize) throw new Exception("Map size must be defined at first");
            
            buildedrooms++;
            
            if (buildedrooms >= this.width*this.height) return;
        }
    }
    
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public Character getHero() {
        return hero;
    }

    public void setHero(Character hero) {
        this.hero = hero;
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
            if(Entrances.charAt(i) == 'N' && isRoomAvailable(x-1,y)){
                System.out.print("La porte du Nord mene a :");
                System.out.println(rooms.get(x-1*width+y).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'S' && isRoomAvailable(x+1,y)){
                System.out.print("La porte du Sud mene a :");
                System.out.println(rooms.get(x+1*width+y).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'E' && isRoomAvailable(x,y+1)){
                System.out.print("La porte de l'Est mene a :");
                System.out.println(rooms.get(x*width+y+1).getName());
                continue;
            }
            if(Entrances.charAt(i) == 'W' && isRoomAvailable(x,y-1)){
                System.out.print("La porte de l'Ouest mene a :");
                System.out.println(rooms.get(x-1*width+y-1).getName());
            }
        }
    }
    public void moveCharacter(){
        int actualRoom = hero.getRoom().getX()+hero.getRoom().getY()*width;
        char nextRoom = '\0';
        for(int i=0;i<hero.getRoom().getEntrances().length();i++){
            nextRoom = hero.moveWhere();
            if(hero.getRoom().getEntrances().charAt(i) == nextRoom){
                break;
            }
            i++;
        }
        int nextRoomIs;
        switch(nextRoom){
            case('N'):
                if(isRoomAvailable(actualRoom%width,actualRoom/width-1)){
                    nextRoomIs = actualRoom - width;
                }else{
                    System.err.println("Erreur, position incorrecte");
                    nextRoomIs = actualRoom;
                }
            break;
            case('S'):
                if(isRoomAvailable(actualRoom%width,actualRoom/width+1)){
                    nextRoomIs = actualRoom + width;
                }else{
                    System.err.println("Erreur, position incorrecte");
                    nextRoomIs = actualRoom;
                }
            break;
            case('E'):
                if(isRoomAvailable(actualRoom%width+1,actualRoom/width)){
                    nextRoomIs = actualRoom + 1;
                }else{
                    System.err.println("Erreur, position incorrecte");
                    nextRoomIs = actualRoom;
                }
            break;
            case('W'):
                if(isRoomAvailable(actualRoom%width+1,actualRoom/width)){
                    nextRoomIs = actualRoom - 1;
                }else{
                    System.err.println("Erreur, position incorrecte");
                    nextRoomIs = actualRoom;
                }
            break;
            default:
                System.err.println("Mauvaise entrer position salle");
                nextRoomIs = actualRoom;
            break;
        }
        hero.setRoom(rooms.get(nextRoomIs));
    }
    private boolean isRoomAvailable(int x,int y){
        if(x<0 || y<0 || x>width-1 || y>height-1){
            return false;
        }
        return true;
    }
}
