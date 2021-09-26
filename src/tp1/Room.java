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
public class Room {
    
    protected int x;
    protected int y;
    protected String name;
    protected String entrances;
    protected ArrayList<Character> characters;

    public Room(int x, int y, String name, String entrances, ArrayList<Character> characters) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.entrances = entrances;
        this.characters = characters;
    }
    
    public static Room buildFromFile(String filename) {
        // TODO
        return null;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntrances() {
        return entrances;
    }

    public void setEntrances(String entrances) {
        this.entrances = entrances;
    }
    
    
    
}
