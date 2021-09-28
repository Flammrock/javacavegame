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

    private Controller controller;
    private boolean waitForCommand;
    
    public Player(String name, Room room, List<Talisman> talismans) {
        super(name, room, talismans);
        this.waitForCommand = true;
        this.controller = new Controller();
    }
    
    public String getInput() {
        if (this.waitForCommand) {
            this.waitForCommand = !this.waitForCommand;
            return this.controller.getInput();
        } else {
            this.waitForCommand = !this.waitForCommand;
            return this.controller.getData();
        }
    }
    
    public void abortInput() {
        this.waitForCommand = true;
        this.controller.abort();
    }
    
}
