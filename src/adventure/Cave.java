/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author User
 */
public class Cave {
    
    // get the logger
    private static final Logger LOGGER = Logger.getLogger(Builder.class.getPackage().getName());

    // configure the logger
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] {%2$s} %5$s%6$s%n");
        try {
            FileHandler fileHandler = new FileHandler("game.log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Cannot read configuration file", exception);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // on créé une map vide
        try {
            Cave m =  new Cave.Builder()
                    .loadRoomsFromStream(new Stream(Cave.class.getResourceAsStream("Rooms2.dat")))
                    .loadTalismansFromStream(new Stream(Cave.class.getResourceAsStream("Talismans.dat")))
                    .loadCharactersFromStream(new Stream(Cave.class.getResourceAsStream("Characters.dat")))
                    .build();

            
            /*Parser p = new Parser(new Stream(System.in));
            p.setCharSeparator(' ');
            p.setAcceptEmptyToken(false);
            
            List<Token> tokens = p.getNextObject();
            for (Token t : tokens) {
                System.out.println(t);
            }*/
            
            // on créé le héro et on le met dans la 1ère salle de la liste
            Character Hero = new Character("Hero",m.getRooms().get(0),null);

            // on ajoute le héro dans la map
            m.setHero(Hero);

            // on fait entrer le héro dans la salle
            m.enterNewRoom(m.getRooms().get(0));
            
            while (true) {
                m.quefaire();
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
    
    /**
     * A ParseException is thrown when data is missing when parsing
     */
    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
        public ParseException(Exception e) {
            super(e);
        }
    }
    
    /**
     * Builder for the Cave class
     */
    public static class Builder {
        
        // contains all rooms added to this builder
        private List<Room> rooms;
        
        // contains all characters added to this builder
        private List<Character> characters;
        
        // contains all talismans added to this builder
        private List<Talisman> talismans;
        
        /**
         * contains unsolved characters
         * (when we extract "# Room name, Character, Talisman" from file, we need then to
         * associate the room name and the talisman name to actual object (Room Object and Talisman Object))
         */
        private List<List<Token>> unsolvedCharacters;
        
        // contains the hero
        private Character hero;
        
        // a callback for the method loadObjectFromStream (see below)
        private interface Callback {
            void call(List<Token> tokens) throws Cave.ParseException;
        }
        
        // map size
        private int width;
        private int height;
        
        private boolean isCaveSizeValid() {
            return this.width > 0 && this.height > 0;
        }
        
        Builder() {
            this.rooms = new ArrayList<>();
            this.talismans = new ArrayList<>();
            this.hero = null;
            this.width = 0;
            this.height = 0;
            this.unsolvedCharacters = new ArrayList<>();
            this.characters = new ArrayList<>();
        }
        
        Cave build() throws Cave.ParseException {
            
            // before build, we solve all the informations together
            this.solve();
            
            return new Cave(this.rooms,this.hero,this.width,this.height);
        }
        
        Builder setWidth(int width) throws Cave.ParseException {
            if (this.isCaveSizeValid()) throw new Cave.ParseException("Cave size already defined");
            this.width = width;
            LOGGER.log(Level.INFO, "Width = {0}", width);
            return this;
        }
        Builder setHeight(int height) throws Cave.ParseException {
            if (this.isCaveSizeValid()) throw new Cave.ParseException("Cave size already defined");
            this.height = height;
            LOGGER.log(Level.INFO, "Height = {0}", height);
            return this;
        }
        
        
        Builder loadRoomsFromStream(Stream stream) throws Cave.ParseException {
            
            LOGGER.log(Level.INFO, "Load Room from a stream");
            
            int x = 0;
            int y = 0;
            
            // create a parser over the stream
            Parser p = new Parser(stream);
            List<Token> tokens = p.getNextObject(); // get the next object
            
            // if cave size not set or invalid, try to extract it
            if (!this.isCaveSizeValid()) {
                this.extractCaveSize(tokens); // if fail, a ParseException is thrown
                LOGGER.log(Level.INFO, "[CaveSize] Width = {0}, Height = {1}", new Object[]{this.width, this.height});
            }
            
            // extract all rooms
            while (true) {
                tokens = p.getNextObject(); // get the next object
                
                // we need at least 3 tokens : the room name, the directions and a token that tell if the room is the start room
                if (tokens.size() < 3) throw new Cave.ParseException("data is missing when loading room");
                
                // build the room
                String roomName = tokens.get(0).getData();
                String roomDirections = tokens.get(1).getData();
                String roomIsStart = tokens.get(2).getData();
                Room room = new Room(x, y, roomName, roomDirections, roomIsStart.toLowerCase().equals("true"));
                
                this.rooms.add(room); // add the room
                
                LOGGER.log(Level.INFO, "[Room] {0},{1} : {2} - {3} - {4}", new Object[]{x, y, roomName, roomDirections, roomIsStart});
                
                // compute positions
                x++;
                if (x > this.width-1) {
                    x = 0;
                    y++;
                    if (y > this.height-1) break;
                }
            }
            
            return this;
        }
        
        Builder loadTalismansFromStream(Stream stream) throws Cave.ParseException {
            this.loadObjectFromStream(stream, (List<Token> tokens) -> {
                
                // check if enough data
                if (tokens.size() < 2) throw new Cave.ParseException("data is missing when loading talisman");
                
                // extract data
                String roomName = tokens.get(0).getData();
                String talismanName = tokens.get(1).getData();
                
                // build talisman
                Talisman talisman = new Talisman(talismanName,roomName);
                
                LOGGER.log(Level.INFO, "[Talisman] Room={0}, Name={1}", new Object[]{roomName, talismanName});
                
                // add talisman
                this.talismans.add(talisman);
                
            });
            return this;
        }
        
        Builder loadCharactersFromStream(Stream stream) throws Cave.ParseException {
            this.loadObjectFromStream(stream, (List<Token> tokens) -> {
                
                // check if enough data
                if (tokens.size() < 3) throw new Cave.ParseException("data is missing when loading character");
                
                this.unsolvedCharacters.add(tokens); // with solve the data of characters in the solve() method
                
            });
            return this;
        }
        
        
        /**
         * This method extract from the stream each list of tokens (which represent an object) and pass this list to the callback
         * 
         * @param stream
         * @param callback this callback is call for every list of tokens found in the stream
         * @return
         * @throws adventure.Cave.ParseException 
         */
        private Builder loadObjectFromStream(Stream stream, Callback callback) throws Cave.ParseException {
            // create a parser over the stream
            Parser p = new Parser(stream);
            
            // extract all "objects"
            while (true) {
                List<Token> tokens = p.getNextObject(); // get the next object
                
                // if no object to load, break the loop
                if (tokens.isEmpty()) break;
                
                // build the object by calling the callback
                callback.call(tokens);
                
            }
            
            return this;
        }
        
        /**
         * This method check if the list of tokens passed in arguments represent an "objet" that set the map size
         * 
         * @param tokens
         * @throws adventure.Cave.ParseException throws an exception if the tokens dont represent an "objet" that set the cave size
         */
        private void extractCaveSize(List<Token> tokens) throws Cave.ParseException {
            
            // check if there are enough data
            if (tokens == null || tokens.size() < 3) {
                throw new Cave.ParseException("unable to extract width and height");
            }

            // first token must be "!" (to specify the size)
            if (tokens.get(0).getData().equals("!")) {
                try {
                    this.width = Integer.parseInt(tokens.get(1).getData());
                    this.height = Integer.parseInt(tokens.get(2).getData());
                } catch (Exception e) {
                    throw new Cave.ParseException(e); // chain exception to preserve log trace
                }
                return;
            }
            
            throw new Cave.ParseException("unable to extract width and height");
            
        }

        /**
        * This method build a Hashmap<List<T>> from an List<T> by using the name's item as key
        * 
        * @param list
        * @return return the hashmap
        */
        private <T extends Item> HashMap<String,List<T>> listToHashmap(List<T> list) {
            HashMap<String,List<T>> hashmap = new HashMap<>();
            
            // fill hashmap with item
            for (T r : list) {
                
                String key = r.getName();
                
                // if two items have same name, send a warning
                if (hashmap.containsKey(key)) {
                    LOGGER.log(Level.WARNING, "[Warning] Two items have the name [{0}]", key);
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
        private void solve() throws Cave.ParseException {
            
            // use intermediate hash map (to find room in O(1))
            HashMap<String,List<Room>> hashmap = this.<Room>listToHashmap(this.rooms);
            HashMap<String,List<Talisman>> hashmaptalisman = this.<Talisman>listToHashmap(this.talismans);
            
            // try to put all the talismans in rooms
            for (Talisman t : this.talismans) {
                
                // get the name of the room
                String key = t.getOriginRoomName();
                
                // try to find the room
                if (!hashmap.containsKey(key)) {
                    LOGGER.log(Level.WARNING, "[Warning] unable to find the room [{0}] for the talisman [{1}]", new Object[]{key, t.getName()});
                    continue;
                }
                
                // if we found the room, we add the talisman
                List<Room> roomslist = hashmap.get(key);
                for (Room r : roomslist) {
                    r.addTalisman((Talisman)t.copy()); // a copy of the talisman to avoid issues
                }
                
            }
            
            // try to solve characters data
            for (List<Token> tokens : this.unsolvedCharacters) {
                
                String key = tokens.get(0).getData(); // get the name of the room
                String name = tokens.get(1).getData(); // name of the character
                String talismanName = tokens.get(2).getData(); // name of the talisman
                List<Talisman> talismanscharacter = new ArrayList<>();
                
                // try to find the room
                if (!hashmap.containsKey(key)) {
                    LOGGER.log(Level.WARNING, "[Warning] unable to find the room [{0}] for the character [{1}]", new Object[]{key, name});
                    continue;
                }
                
                // try to find the talisman
                if (!hashmaptalisman.containsKey(talismanName)) {
                    LOGGER.log(Level.WARNING, "[Warning] unable to find the talisman [{0}] for the character [{1}]", new Object[]{talismanName, name});
                } else {
                    talismanscharacter = new ArrayList<>(hashmaptalisman.get(talismanName));
                }
                
                Character character = new Character(name,hashmap.get(key).get(0),talismanscharacter);
                LOGGER.log(Level.INFO, "[Character] Room={0}, Name={1}, Talismans={2}", new Object[]{hashmap.get(key).get(0).getName(), name, talismanscharacter.size()});
                this.characters.add(character);
            }
            
        }
    }
    
    private Character hero;
    private List<Room> rooms;
    private int width;
    private int height;
    
    private Cave() {
        this.rooms = new ArrayList<>();
        this.hero = null;
        this.width = 0;
        this.height = 0;
    }

    private Cave(List<Room> rooms, Character hero, int width, int height) {
        this.rooms = rooms;
        this.hero = hero;
        this.width = width;
        this.height = height;
    }
    
    public List<Room> getRooms() {
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
     *Cette fonction décrit les salles qui sont connecter a la salle entrer
     * @param x position x dans le labyrinthe 
     * @param y position y dans le labyrinthe 
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
     *demande la destination (NSEW) a l'utilisateur
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
    
    /**
     * execute tout les fonction utile lors de la rentrer dans une nouvelle salle
     * @param r la nouvelle salle dans lequel le joueur va rentrer
     */
    public void enterNewRoom(Room r){
        hero.setRoom(r);
        System.out.println(r.toString());
        descriptionAlentoure(r.getX(),r.getY());
        hero.inventaire();        
    }
    
    /**
     * permet a l'utilisateur de prendre un talisement dans une salle
     * @param t le numero du talisement que l'utilisateur va prendre dans la salle
     * @return retourne si l'action a bien etait effectuer
     */
    public boolean takeTalismans(int t){
        if(t>=0 && t<hero.getRoom().getTalismans().size()){
            hero.addTalisman((Talisman)hero.getRoom().getTalismans().get(t));
            hero.getRoom().removeTalisman(t);
            return true;
        }else{
            return false;
        }
    }

    /**
     * permet a l'utilisateur de deposer un talisement dans une salle
     * @param t le numero du talisement que l'utilisateur va deposer dans la salle
     * @return retourne si l'action a bien etait effectuer
     */
    public boolean putTalismans(int t){
        if(t>=0 && t<hero.getTalisman().size()){
            hero.getRoom().getTalismans().add(hero.getTalisman().get(t));
            hero.getTalisman().remove(t);
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * demande a l'utilisateur cfe qui veux faire en fonction de ses possibilites et l'oblige a donner un choix possible
     * @return choix de l'utilisateur pour la suite de ses actions
     */
    private char choiceChar(){
        while(true){
            System.out.println("Vous pouvez:");
            if(hero.getRoom().getTalismans().size()>0){
                System.out.println("Prendre un talismant dans la salle (P)");
            }
            if(hero.getTalisman()!=null && hero.getTalisman().size()>0){
                System.out.println("Jeter un talismant dans la salle (J)");
            }
            System.out.println("Voir votre inventaire (V)");
            if(hero.getRoom().getTalismansLock()!=null && hero.getRoom().getTalismansLock().size()>0){
                System.out.println("Utiliser un talismant sur un monstre (U)");
            }
            System.out.println("Deplacer votre hero dans une autre salle (D)");

            Scanner sc = new Scanner(System.in);
            String choise = sc.next();
            choise = choise.toUpperCase();
            char choiseLetter = choise.charAt(0);
            if(choiseLetter=='P' && hero.getRoom().getTalismans().size()>0 ||
                choiseLetter=='J' && hero.getTalisman()!=null || 
                choiseLetter=='V' || 
                choiseLetter=='U' && hero.getRoom().getTalismansLock()!=null || 
                choiseLetter=='D'){
                return choiseLetter;
            }
        }
    }
    
    /**
     *  la fonction execute l'action en fonction de se qui est demander en entrer 
     * @param c le choix de l'utilisateur (PJVUD)
     */
    private void choix(char c){
        Scanner sc = new Scanner(System.in);
        
        if(c == 'P'){
            System.out.println("Quelle talisement prenez vous?");
            System.out.println(hero.getRoom().getTalismansToString());
            int choise;
            do{
                choise = sc.nextInt();
            }while(!takeTalismans(choise));
        }
        if(c == 'J'){
            System.out.println("Quelle talisement jetez vous?");
            System.out.println(hero.getTalismansToString());
            int choise;
            do{
                choise = sc.nextInt();
            }while(!putTalismans(choise));
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
    
    /**
     *fusionne la fonction Choix et choiceChar qui fonctionne de paire
     */
    public void quefaire(){
        choix(choiceChar());
    }
}
