/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.util.List;

/**
 *
 * @author User
 */
public class Player extends Character {

    // the controller which get the input of user
    private Controller controller;
    
    // if true  => wait for a "T" or "M"
    //    false => wait for the name of talisman or the direction NSEW
    private boolean waitForCommand;
    
    public Player(String name, Room room, List<Talisman> talismans) {
        super(name, room, talismans);
        this.waitForCommand = true; // by default, wait a command
        this.controller = new Controller();
    }
    
    public String getInput() {
        if (this.waitForCommand) {
            this.waitForCommand = !this.waitForCommand;
            return this.controller.getInput(); // just get a token data
        } else {
            this.waitForCommand = !this.waitForCommand;
            return this.controller.getData();  // get all data
        }
    }
    
    /**
     * This method reset the input
     */
    public void abortInput() {
        this.waitForCommand = true;
        this.controller.abort();
    }
    
}
