/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Character extends Item {
    
    private Room room;
    private List<Talisman> talisman;
    private boolean mort = false;

    public Character(String name, Room room, List<Talisman> talismans) {
        super(name);
        this.room = room;
        this.talisman = talismans==null?new ArrayList<>():talismans;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public List<Talisman> getTalisman() {
        return talisman;
    }

    public void setTalisman(ArrayList<Talisman> talisman) {
        this.talisman = talisman;
    }
    public void addTalisman(Talisman talisman){
        this.talisman.add(talisman);
    }
    public void removeTalisman(Talisman talisman){
        this.talisman.remove(talisman);
    }

    public boolean isMort() {
        return mort;
    }

    public void setMort(boolean mort) {
        this.mort = mort;
    }
            

    /**
     *
     * @param entrances
     * @return retourne la sortie choisie par l'utilisateur
     */
    public char moveWhere(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Quel est votre prochaine destination? ("+this.room.getEntrances()+")");
            String nextRoom = sc.next();
            nextRoom = nextRoom.toUpperCase();
            char nextPorte = nextRoom.charAt(0);
            if(nextPorte=='N' || nextPorte=='S' || nextPorte=='E' || nextPorte=='W'){
                return nextPorte;
            }
        }
    }
    
    /**
     * affiche l'inventaire de l'utilisateur
     */
    public void inventaire(){
        String inv = "[Votre inventaire]:\n";
        if(talisman!=null){
            for(Talisman t:talisman){
                inv += "-"+t.getName()+"\n";
            }
        }else{
            inv += "vide\n";
        }
        System.out.println(inv);
    }
    
    /**
     *
     * @return retourne la liste de talisement de l'utilisateur numeroté dans le but d'en deposer un
     */
    public String getTalismansToString(){
        String description = "Talismens:\n";
        int i=0;
        if(talisman != null){
            for(Talisman t:talisman){
                description += "- "+t.getName()+"\n";
                i++;
            }
        }
        return description;
    }
}
