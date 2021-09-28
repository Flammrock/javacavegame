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
    private Parser parser;

    public Controller() {
        this.tokens = new ArrayList<>();
        this.parser = new Parser(new Stream(System.in));
        this.parser.setCharSeparator(' ');
    }
    
    private void populate() {
        if (this.tokens.isEmpty()) {
            this.tokens.addAll(this.parser.getNextObject());
        }
    }
    
    public String getInput() {
        this.populate();
        if (this.tokens.isEmpty()) return "";
        String input = this.tokens.remove(0).getData();
        return input;
    }
    
    
    public String getData() {
        this.populate();
        String result = "";
        for (int i = 0; i < this.tokens.size(); i++) {
            Token t = this.tokens.get(i);
            if (i==this.tokens.size()-1) {
                result += t.getData();
            } else {
                result += t.getData() + " ";
            }
        }
        this.tokens.clear();
        return result;
    }
    
    public void abort() {
        this.tokens.clear();
    }
    
}
