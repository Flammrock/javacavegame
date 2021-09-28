/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Controller {
    
    private List<Token> tokens;

    public Controller() {
        this.tokens = new ArrayList<>();
    }
    
    private void populate() {
        if (this.tokens.isEmpty()) {
            Parser p = new Parser(new Stream(System.in));
            p.setCharSeparator(' ');
            this.tokens.addAll(p.getNextObject());
        }
    }
    
    public String getInput() {
        this.populate();
        String input = this.tokens.remove(0).getData();
        return input;
    }
    
    
    public String getData() {
        this.populate();
        String result = "";
        for (Token t : this.tokens) {
            result += t.getData() + " ";
        }
        if (!result.isEmpty()) result = result.substring(0, result.length()-2);
        this.tokens.clear();
        return result;
    }
    
    public void abort() {
        this.tokens.clear();
    }
    
}
