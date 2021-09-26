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
    protected ArrayList<Talisman> talismans;

    public Room(int x, int y, String name, String entrances, ArrayList<Talisman> talismans) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.entrances = entrances;
        this.talismans = talismans;
    }
    
    public static Room buildFromFile(String filename) {
        // TODO
        return null;
    }

    public ArrayList<Talisman> getCharacters() {
        return talismans;
    }

    public void setCharacters(ArrayList<Talisman> talismans) {
        this.talismans = talismans;
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
    
    @Override
    public String toString(){
        String phrasesDescriptif = "Vous entrez dans : " + name;
        if(talismans.size()==1){
            phrasesDescriptif += "\nil y a 1 talisman dans cette salle :";
        }else if(talismans.size()>0){
            phrasesDescriptif += "\nil y a "+talismans.size()+" talismans dans cette salle :";
        }
        for(Talisman t:talismans){
            phrasesDescriptif += "\n-"+t.getName();   
        }
        return phrasesDescriptif;
    }
    
}
