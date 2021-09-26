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
public class Character {
    
    protected Room room;
    protected String name;
    protected ArrayList<Talisman> talisman;

    public Character(Room room, String name, ArrayList<Talisman> talisman) {
        this.room = room;
        this.name = name;
        this.talisman = talisman;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Talisman> getTalisman() {
        return talisman;
    }

    public void setTalisman(ArrayList<Talisman> talisman) {
        this.talisman = talisman;
    }
    
    
    
    
    
}
