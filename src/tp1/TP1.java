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
public class TP1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Parser p = new Parser(new Stream(System.in));
        
        
        /*while (true) {
            Token t = p.getNextToken();
            if (t!=null) {
                System.out.println(t);
            }
        }*/
        
        // test
        //blocked?
        Talisman medaillon1 = new Talisman("medaillon en Argent");
        Talisman medaillon2 = new Talisman("medaillon en Or");
        ArrayList<Talisman> t = new ArrayList();
        t.add(medaillon1);
        t.add(medaillon2);
        Room salle = new Room(0,0,"salle de coffre","NSE",t);
        Character Hero = new Character(salle,"Hero",null);
        ArrayList<Room> r = new ArrayList();
        r.add(salle);
        Map m =  new Map(r,Hero,1,1);
        m.enterNewRoom(salle);
        
    }
}
