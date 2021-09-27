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
    
    private String name;
    private String originRoomName;
    private ArrayList<Talisman> talisman;

    public Character(String originRoomName, String name, ArrayList<Talisman> talisman) {
        this.originRoomName = originRoomName;
        this.name = name;
        this.talisman = talisman;
    }

    public String getOriginRoomName() {
        return originRoomName;
    }

    public void setOriginRoomName(String originRoomName) {
        this.originRoomName = originRoomName;
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
     * @param entrances
     * @return retourne la sortie choisie par l'utilisateur
     */
    public char moveWhere(String entrances){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Quel est votre prochaine destination? ("+entrances+")");
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
    
    /**
     *
     * @return retourne la liste de talisement de l'utilisateur numeroté dans le but d'en deposer un
     */
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
