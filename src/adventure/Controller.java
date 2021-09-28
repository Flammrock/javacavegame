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
    
    // contains the tokens extracted by the parser
    private List<Token> tokens;
    
    // the parser
    private Parser parser;

    public Controller() {
        this.tokens = new ArrayList<>();
        this.parser = new Parser(new Stream(System.in)); // connect the System.in to the parser
        this.parser.setCharSeparator(' ');
    }
    
    /**
     * This method add new tokens to the tokens list if the tokens list is empty
     */
    private void populate() {
        if (this.tokens.isEmpty()) {
            
            // retrieve all the tokens extracted by the parser and add these tokens to the list
            this.tokens.addAll(this.parser.getNextObject());
            
        }
    }
    
    /**
     * This method consume a token and return the data of the consumed token
     * @return
     */
    public String getInput() {
        this.populate();
        if (this.tokens.isEmpty()) return "";
        String input = this.tokens.remove(0).getData();
        return input;
    }
    
    /**
     * This method return all extracted token (and merge the token together)
     * @return
     */
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
    
    /**
     * This method clear the tokens
     */
    public void abort() {
        this.tokens.clear();
    }
    
}
