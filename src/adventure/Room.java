/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Room extends Item {
    
    private int x;
    private int y;
    private String entrances;
    private ArrayList<Talisman> talismans;
    private ArrayList<Talisman> talismansLock;
    private boolean isStart;
    
    public Room(int x, int y, String name, String entrances, boolean isStart) {
        super(name);
        this.x = x;
        this.y = y;
        this.entrances = entrances;
        this.talismans = new ArrayList<>();
        this.isStart = isStart;
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
    
    public void removeTalisman(Talisman t){
        this.talismans.remove(t);
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
        String phrasesDescriptif = "Vous entrez dans : " + this.getName() + "\n";
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
        String description = "Talismans :\n";
        int i=0;
        if(talismans != null){
            for(Talisman t:talismans){
                description += "- "+t.getName()+"\n";
                i++;
            }
        }
        return description;
    }
}
