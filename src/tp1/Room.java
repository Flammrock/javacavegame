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
    protected ArrayList<Talisman> talismansLock;
    
    public Room(int x, int y, String name, String entrances) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.entrances = entrances;
        this.talismans = new ArrayList<>();
    }

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

    public ArrayList<Talisman> getTalismans() {
        return talismans;
    }

    public void setTalismans(ArrayList<Talisman> talismans) {
        this.talismans = talismans;
    }

    public ArrayList<Talisman> getTalismansLock() {
        return talismansLock;
    }

    public void setTalismansLock(ArrayList<Talisman> talismansLock) {
        this.talismansLock = talismansLock;
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
        String phrasesDescriptif = "Vous entrez dans : " + name + "\n";
        if(talismans.size()==1){
            phrasesDescriptif += "il y a 1 talisman dans cette salle :\n";
        }else if(talismans.size()>0){
            phrasesDescriptif += "il y a "+talismans.size()+" talismans dans cette salle :\n";
        }
        for(Talisman t:talismans){
            phrasesDescriptif += "-"+t.getName()+"\n";   
        }
        if(talismans.size()==1){
            phrasesDescriptif += "Vous mettez le talisement dans votre poche\n";
        }else if(talismans.size()>0){
            phrasesDescriptif += "Vous mettez les talisements dans votre poche\n";
        }
        return phrasesDescriptif;
    }
    
}
