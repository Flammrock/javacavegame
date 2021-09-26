/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        Room salle = new Room(0,0,"salle de coffre","NSE",null);
        Character Hero = new Character(salle,"Hero",null);
        Hero.moveWhere();
    }
    
}
