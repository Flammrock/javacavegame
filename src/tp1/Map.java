/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Map {
    
    protected Character hero;
    protected ArrayList<Room> rooms;
    protected int width;
    protected int height;
    
    public Map() {
        this.rooms = new ArrayList<>();
        this.hero = null;
        this.width = 0;
        this.height = 0;
    }

    public Map(ArrayList<Room> rooms, Character hero, int width, int height) {
        this.rooms = rooms;
        this.hero = hero;
        this.width = width;
        this.height = height;
    }
    
    protected List<Room> buildRoomFromParser(int totalbuiledrooms, Parser p) throws Exception {
        // ignore useless comment '#'
        
        List<Room> buildedrooms = new ArrayList<>();
        
        while (true) {

            Token token = p.peekToken();
            String data = token.getData();

            // get usefull comments
            if (data.charAt(0) == '#') {

                // try to get position
                Parser p2 = new Parser(new Stream(data.substring(1)));
                List<Token> tokens = p2.getTokenList();
                if (tokens.size() >= 2) {
                    int x = Integer.parseInt(tokens.get(0).getData());
                    int y = Integer.parseInt(tokens.get(1).getData());
                    p.skipComments();
                    String roomName = p.getNextToken().getData();
                    String roomEntrances = p.getNextToken().getData();
                    String roomStartPosition = p.getNextToken().getData();
                    System.out.println(x+","+y+" : "+roomName+" - "+roomEntrances+" - "+roomStartPosition);
                    buildedrooms.add(new Room(x, y, roomName, roomEntrances));
                    return buildedrooms;
                }

                // try to get row info
                else if (data.contains("row")) {
                    int x = this.width;
                    int y = totalbuiledrooms/this.height;
                    while (x!=0) {
                        p.skipComments();
                        String roomName = p.getNextToken().getData();
                        String roomEntrances = p.getNextToken().getData();
                        String roomStartPosition = p.getNextToken().getData();
                        System.out.println((this.width-x)+","+y+" : "+roomName+" - "+roomEntrances+" - "+roomStartPosition);
                        buildedrooms.add(new Room(this.width-x, y, roomName, roomEntrances));
                        x--;
                    }
                    return buildedrooms;
                }
                
                p.getNextToken();
                continue;

            }
            
            throw new Exception("a line which start by '#' with position or row informations was expected");
        
        }
        
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
                System.out.println("Map Size : width="+this.width+", height="+this.height);
                continue;
            }
            
            // if comment '#' and mapsize not defined then ignore
            if (!mapsize && token.getData().charAt(0)=='#') continue;
            
            // else, map size must be defined
            if (!mapsize) throw new Exception("Map size must be defined at first");
            
            List<Room> builedrooms = this.buildRoomFromParser(buildedrooms,p);
            buildedrooms+=builedrooms.size();
            
            rooms.addAll(builedrooms);
            
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
        System.out.println("Description alentoure:");
        int i = 0;
        while(i!=Entrances.length()){
            
            if(Entrances.charAt(i) == 'N' && isRoomAvailable(x-1,y)){
                System.out.print("La porte du Nord mene a :");
                System.out.println(rooms.get(x-1*width+y).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'S' && isRoomAvailable(x+1,y)){
                System.out.print("La porte du Sud mene a :");
                System.out.println(rooms.get(x+1*width+y).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'E' && isRoomAvailable(x,y+1)){
                System.out.print("La porte de l'Est mene a :");
                System.out.println(rooms.get(x*width+y+1).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'W' && isRoomAvailable(x,y-1)){
                System.out.print("La porte de l'Ouest mene a :");
                System.out.println(rooms.get(x-1*width+y-1).getName());
                i++;
            }
        }
        System.out.println("\n");
    }
    public Room moveCharacter(){
        int actualRoom = hero.getRoom().getX()+hero.getRoom().getY()*width;
        char nextRoom = '\0';
        boolean isUncorrect = true;
        while(isUncorrect){
            nextRoom = hero.moveWhere();
            for(int i=0;i<hero.getRoom().getEntrances().length();i++){
                if(hero.getRoom().getEntrances().charAt(i) == nextRoom){
                    isUncorrect = false;
                    break;
                }
            }
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
        return rooms.get(nextRoomIs);
    }
    private boolean isRoomAvailable(int x,int y){
        if(x<0 || y<0 || x>width-1 || y>height-1){
            return false;
        }
        return true;
    }
    
    public void enterNewRoom(Room r){
        hero.setRoom(r);
        System.out.println(r.toString());
        descriptionAlentoure(r.getX(),r.getY());
        hero.inventaire();
        
    }
    
    public boolean takeTalismans(int t){
        if(t<0 || t>hero.getRoom().getTalismans().size()){
            hero.getTalisman().add(hero.getRoom().getTalismans().get(t));
            hero.getRoom().getTalismans().remove(t);
            return true;
        }else{
            return false;
        }
    }
    public boolean putTalismans(int t){
        if(t<0 || t>hero.getTalisman().size()){
            hero.getRoom().getTalismans().add(hero.getTalisman().get(t));
            hero.getTalisman().remove(t);
            return true;
        }else{
            return false;
        }
    }
    
    public char choiceChar(){
        while(true){
            System.out.println("Vous pouvez:");
            if(hero.getRoom().getTalismans().size()>0){
                System.out.println("Prendre un talismant dans la salle (P)");
            }
            if(hero.getTalisman().size()>0){
                System.out.println("Jeter un talismant dans la salle (J)");
            }
            System.out.println("Voir votre inventaire (V)");
            if(hero.getRoom().getTalismansLock().size()>0){
                System.out.println("Utiliser un talismant sur un monstre (U)");
            }
            System.out.println("Deplacer votre hero dans une autre salle (D)");

            Scanner sc = new Scanner(System.in);
            String choise = sc.next();
            choise = choise.toUpperCase();
            char choiseLetter = choise.charAt(0);
            if(choiseLetter=='P' || choiseLetter=='J' || choiseLetter=='V' || choiseLetter=='U' || choiseLetter=='D'){
                return choiseLetter;
            }
        }
    }
    
    /*public void Choix(char c){
        if(c == 'P'){
            
        }
        if(c == 'J'){
            
        }
        if(c == 'V'){
            
        }
        if(c == 'U'){
            
        }
        if(c == 'D'){
            
        }
    }*/
}
