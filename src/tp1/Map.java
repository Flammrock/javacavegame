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
    
    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
    
    public static class Builder {
    
        ArrayList<Room> rooms;
        
        ArrayList<ArrayList<String>> unResolvedTalismans;
        
        Character hero;
        int width;
        int height;
        
        // true if width and height are defined
        boolean mapsize;
        
        void updateMapSize() {
            this.mapsize = this.width > 0 && this.height > 0;
        }
        
        Builder() {
            this.rooms = new ArrayList<>();
            this.unResolvedTalismans = new ArrayList<>();
            this.hero = null;
            this.width = 0;
            this.height = 0;
            this.mapsize = false;
        }
        
        Map build() {
            return new Map(this.rooms,this.hero,this.width,this.height);
        }
        
        Builder setWidth(int width) throws Map.ParseException {
            if (mapsize) throw new Map.ParseException("Map size already defined");
            this.width = width;
            this.updateMapSize();
            return this;
        }
        Builder setHeight(int height) throws Map.ParseException {
            if (mapsize) throw new Map.ParseException("Map size already defined");
            this.height = height;
            this.updateMapSize();
            return this;
        }
        
        Builder roomsFromStream(Stream stream) throws Map.ParseException {
            
            try {
                Parser p = new Parser(stream);
                
                int buildedrooms = 0;

                while (true) {
                    Token token = p.getNextToken();

                    // if token is "!"
                    if (token.getData().equals("!")) {
                        if (this.mapsize) throw new Map.ParseException("Map size already defined");
                        try {
                            this.width = Integer.parseInt(p.getNextToken().getData());
                            this.height = Integer.parseInt(p.getNextToken().getData());
                        } catch (Exception e) {
                            throw new Map.ParseException(e.getMessage());
                        }
                        this.updateMapSize();
                        this.mapsize = true;
                        System.out.println("Map Size : width="+this.width+", height="+this.height);
                        continue;
                    }

                    // if comment '#' and mapsize not defined then ignore
                    if (!this.mapsize && token.getData().charAt(0)=='#') continue;

                    // else, map size must be defined
                    if (!this.mapsize) throw new Map.ParseException("Map size must be defined at first");

                    List<Room> builedrooms = this.buildRoomFromParser(buildedrooms,p);
                    buildedrooms+=builedrooms.size();

                    this.rooms.addAll(builedrooms);

                    if (buildedrooms >= this.width*this.height) return this;
                }
            } catch (Exception e) {
                throw new Map.ParseException(e.getMessage());
            }
            
        }
    
        Builder talismansFromStream(Stream stream) throws Map.ParseException {
            try {
                Parser p = new Parser(stream);
                while (true) {
                    if (!p.hasNextToken()) break;
                    p.skipComments();
                    String roomName = p.getNextToken().getData();
                    String talismanName = p.getNextToken().getData();
                    ArrayList<String> unresolvedtalisman = new ArrayList<>();
                    unresolvedtalisman.add(roomName);
                    unresolvedtalisman.add(talismanName);
                    this.unResolvedTalismans.add(unresolvedtalisman);
                    p.skipComments();
                }
                return this;
            } catch (Exception e) {
                throw new Map.ParseException(e.getMessage());
            }
        }
        
        List<Room> buildRoomFromParser(int totalbuiledrooms, Parser p) throws Map.ParseException {
            // ignore useless comment '#'
            
            try {

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

                    throw new Map.ParseException("a line which start by '#' with position or row informations was expected");

                }

            } catch (Exception e) {
                throw new Map.ParseException(e.getMessage());
            }
        }

    }
    
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
        Room actualRoom = rooms.get(x+y*width);
        String Entrances = actualRoom.getEntrances();
        System.out.println("Description alentoure:");
        int i = 0;
        while(i!=Entrances.length()){
            
            if(Entrances.charAt(i) == 'N' && isRoomAvailable(x,y-1)){
                System.out.print("La porte du (N)ord mene a :");
                System.out.println(rooms.get(x+width*(y-1)).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'S' && isRoomAvailable(x,y+1)){
                System.out.print("La porte du (S)ud mene a :");
                System.out.println(rooms.get(x+width*(y+1)).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'E' && isRoomAvailable(x+1,y)){
                System.out.print("La porte de l'(E)st mene a :");
                System.out.println(rooms.get(x+1+y*width).getName());
                i++;
                continue;
            }
            if(Entrances.charAt(i) == 'W' && isRoomAvailable(x-1,y)){
                System.out.print("La porte de l'(O)uest mene a :");
                System.out.println(rooms.get(x-1+width*y).getName());
                i++;
            }
        }
        System.out.println("\n");
    }

    /**
     *
     * @return retourn la salle choisie par l'utilisateur
     */
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
    
    /**
     *
     * @return choix de l'utilisateur pour la suite de ses actions
     */
    public char choiceChar(){
        while(true){
            System.out.println("Vous pouvez:");
            if(hero.getRoom().getTalismans().size()>0){
                System.out.println("Prendre un talismant dans la salle (P)");
            }
            if(hero.getTalisman()!=null){
                System.out.println("Jeter un talismant dans la salle (J)");
            }
            System.out.println("Voir votre inventaire (V)");
            if(hero.getRoom().getTalismansLock()!=null){
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
    
    /**
     *  la fonction execute l'action en fonction de se qui est demander en entrer
     * @param c le choix de l'utilisateur
     */
    public void choix(char c){
        Scanner sc = new Scanner(System.in);
        
        if(c == 'P'){
            System.out.println("Quelle talisement prenez vous?");
            System.out.println(hero.getRoom().getTalismansToString());
            int choise;
            do{
                choise = sc.nextInt();
            }while(takeTalismans(choise));
        }
        if(c == 'J'){
            System.out.println("Quelle talisement jetez vous?");
            System.out.println(hero.getTalismansToString());
            int choise;
            do{
                choise = sc.nextInt();
            }while(putTalismans(choise));
        }
        if(c == 'V'){
            hero.inventaire();
        }
        if(c == 'U'){
            //TODO
        }
        if(c == 'D'){
            System.out.println("Quelle porte prenez vous?");
            descriptionAlentoure(hero.getRoom().getX(),hero.getRoom().getY());
            enterNewRoom(moveCharacter());
        }
    }
    
    public void quefaire(){
        choix(choiceChar());
    }
}
