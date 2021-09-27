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
    
    
    public void addTalisman(Talisman t) {
        this.talismans.add(t);
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
    
    /**
     *
     * @return retourne la description de la salle avec son contenu
     */
    @Override
    public String toString(){
        String phrasesDescriptif = "Vous entrez dans : " + name + "\n";
        if(talismans.size()==1){
            phrasesDescriptif += "il y a 1 talisman dans cette salle :\n";
        }else if(talismans.size()>0){
            phrasesDescriptif += "il y a "+talismans.size()+" talismans dans cette salle :\n";
        }else{
            phrasesDescriptif += "il n'y a pas de talismans dans cette salle :\n";
        }
        for(Talisman t:talismans){
            phrasesDescriptif += "-"+t.getName()+"\n";   
        }
        return phrasesDescriptif;
    }
    
    /**
     *
     * @return retourn la liste de talisment dans la salle
     */
    public String getTalismansToString(){
        String description = "";
        int i=0;
        if(talismans != null){
            for(Talisman t:talismans){
                description += "(" +i+ ")"+t.getName()+"\n";
                i++;
            }
        }
        return description;
    }
}
