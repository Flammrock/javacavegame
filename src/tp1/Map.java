/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
        
        
        // contains all rooms added to this builder
        private ArrayList<Room> rooms;
        
        // contains all talismans added to this builder
        private ArrayList<Talisman> talismans;
        
        // contains the hero
        private Character hero;
        
        // map size
        private int width;
        private int height;
        
        private boolean isMapSizeValid() {
            return this.width > 0 && this.height > 0;
        }
        
        Builder() {
            this.rooms = new ArrayList<>();
            this.talismans = new ArrayList<>();
            this.hero = null;
            this.width = 0;
            this.height = 0;
        }
        
        Map build() throws Map.ParseException {
            
            // before build, we solve all the informations together
            this.solve();
            
            return new Map(this.rooms,this.hero,this.width,this.height);
        }
        
        Builder setWidth(int width) throws Map.ParseException {
            if (this.isMapSizeValid()) throw new Map.ParseException("Map size already defined");
            this.width = width;
            return this;
        }
        Builder setHeight(int height) throws Map.ParseException {
            if (this.isMapSizeValid()) throw new Map.ParseException("Map size already defined");
            this.height = height;
            return this;
        }
        
        /**
        * This method try to extract all rooms from a Stream Object
        * 
        * Syntax of the stream :
        * !3,3
        * # 0,0
        * Room name 1,SE,true
        * # 1,0
        * Room name 2,SEW,false
        * ...
        * 
        * @param stream the stream
        */
        Builder roomsFromStream(Stream stream) throws Map.ParseException {
            
            try {
                
                // build a parser
                Parser p = new Parser(stream);
                
                // variable which contains the number of built rooms
                int buildedrooms = 0;
                
                // while we can get rooms
                while (true) {
                    
                    // get the next token
                    Token token = p.getNextToken();

                    // if token is "!"  (try to extract map size)
                    if (token.getData().equals("!")) {
                        if (this.isMapSizeValid()) throw new Map.ParseException("Map size already defined");
                        try {
                            this.width = Integer.parseInt(p.getNextToken().getData());
                            this.height = Integer.parseInt(p.getNextToken().getData());
                        } catch (Exception e) {
                            throw new Map.ParseException(e.getMessage());
                        }
                        System.out.println("Map Size : width="+this.width+", height="+this.height);
                        continue;
                    }

                    // if comment '#' and map size not defined then ignore
                    if (!this.isMapSizeValid() && token.getData().charAt(0)=='#') continue;

                    // else, map size must be defined
                    if (!this.isMapSizeValid()) throw new Map.ParseException("Map size must be defined at first");

                    List<Room> builedrooms = this.buildRoomFromParser(buildedrooms,p);
                    buildedrooms+=builedrooms.size();

                    this.rooms.addAll(builedrooms);
                    
                    // according to the size of the map, we check if we have build all the rooms
                    if (buildedrooms >= this.width*this.height) return this;
                    
                    // else continue to build rooms
                }
                
            } catch (Exception e) {
                throw new Map.ParseException(e.getMessage());
            }
            
        }
    
        /**
        * This method try to extract all talismans from a Stream Object
        * 
        * Syntax of the stream (line which start with '#' are comments):
        * Room name 1, Talisman 1
        * Room name 2, Talisman 2
        * ...
        * 
        * @param stream the stream
        */
        Builder talismansFromStream(Stream stream) throws Map.ParseException {
            try {
                Parser p = new Parser(stream);
                while (true) {
                    if (!p.hasNextToken()) break;
                    p.skipComments();
                    String roomName = p.getNextToken().getData();
                    String talismanName = p.getNextToken().getData();
                    System.out.println("load Talisman : "+roomName+" - "+talismanName);
                    this.talismans.add(new Talisman(roomName,talismanName));
                    p.skipComments();
                }
                return this;
            } catch (Exception e) {
                throw new Map.ParseException(e.getMessage());
            }
        }
        
        /**
        * This method try to build rooms from a Parser Object
        * 
        * @param totalbuiledrooms the number of rooms already built, this information can be use to compute positions of rooms
        * @param p the parser which contains the stream, with this parser we can get the next token easly
        * @return return the list of rooms extracted from the parser
        */
        private List<Room> buildRoomFromParser(int totalbuiledrooms, Parser p) throws Map.ParseException {
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

        /**
        * This method build a Hashmap<ArrayList<Room>> from an ArrayList<Room> by using the name's room as key
        * 
        * @param roomslist
        * @return return the hashmap
        */
        private HashMap<String,ArrayList<Room>> roomstoHashmap(ArrayList<Room> roomslist) {
            HashMap<String,ArrayList<Room>> hashmap = new HashMap<>();
            
            // fill hashmap with room
            for (Room r : roomslist) {
                
                String key = r.getName();
                
                // if two room have same name, send a warning
                if (hashmap.containsKey(key)) {
                    System.out.println("[Warning] Two rooms have the name ["+key+"]");
                    
                }
                
                // if no key, create the array first
                else {
                    hashmap.put(key, new ArrayList<>());
                }
                
                // then add
                hashmap.get(key).add(r);
            }
            
            return hashmap;
        }
        
        /**
        * This method try to insert all Talismans in Rooms
        */
        private void solve() throws Map.ParseException {
            
            // use intermediate hash map (to find room in O(1))
            HashMap<String,ArrayList<Room>> hashmap = this.roomstoHashmap(this.rooms);
            
            // try to put all the talismans in rooms
            for (Talisman t : this.talismans) {
                
                // get the name of the room
                String key = t.getOriginRoomName();
                
                // try to find the room
                if (!hashmap.containsKey(key)) {
                    System.out.println("[Warning] unable to find the room ["+key+"] for the talisman ["+t.getName()+"]");
                    continue;
                }
                
                // if we found the room, we add the talisman
                ArrayList<Room> roomslist = hashmap.get(key);
                for (Room r : roomslist) {
                    r.addTalisman((Talisman)t.copy()); // a copy of the talisman to avoid issues
                }
                
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
    
    /**
     *
     * @param x
     * @param y
     */
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
                continue;
            }
            System.out.println("Vous avez trouvez la sortie!!!!\nElle est au "+googleTrad(Entrances.charAt(i)));
            i++;
        }
        System.out.println("\n");
    }
    
    private String googleTrad(char t){
        switch(t){
            case('N'):
                return "Nord";
            case('S'):
                return "Sud";
            case('E'):
                return "Est";
            case('W'):
                return "Ouest";
            default:
                return "Ha bah non...";
        }
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
                if(isRoomAvailable(actualRoom%width-1,actualRoom/width)){
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
