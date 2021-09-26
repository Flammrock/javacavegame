/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.ArrayList;
import java.util.Scanner;

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
    public void addTalisman(Talisman talisman){
        this.talisman.add(talisman);
    }

    /**
     *
     * @return retourne la sortie choisie par l'utilisateur
     */
    public char moveWhere(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Quel est votre prochaine destination? (N/S/E/W) ");
            String nextRoom = sc.next();
            nextRoom = nextRoom.toUpperCase();
            char nextPorte = nextRoom.charAt(0);
            if(nextPorte=='N' || nextPorte=='S' || nextPorte=='E' || nextPorte=='W'){
                return nextPorte;
            }
        }
    }
    
    public void inventaire(){
        String inv = "Votre inventaire :\n";
        if(talisman!=null){
            for(Talisman t:talisman){
                inv += "-"+t.getName()+"\n";
            }
        }else{
            inv += "vide\n";
        }
        System.out.println(inv);
    }
    
    public String getTalismansToString(){
        String description = "";
        int i=0;
        if(talisman != null){
            for(Talisman t:talisman){
                description += "(" +i+ ")"+t.getName()+"\n";
                i++;
            }
        }
        return description;
    }
}
