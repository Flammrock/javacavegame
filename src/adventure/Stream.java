/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Stream {
    
    protected String data;
    protected Scanner scanner;

    public Stream(InputStream istream) {
        this.data = "";
        this.scanner = new Scanner(istream);
        this.scanner.useDelimiter("");
    }
    
    public Stream(String str) {
        this.data = "";
        this.scanner = new Scanner(str);
        this.scanner.useDelimiter("");
    }
    
    /**
     * This method insert the next data from the scanner if this.data is empty
     */
    protected void populate() {
        // if empty repopulate
        if (this.data.isEmpty()) this.data = this.scanner.next();
    }
    
    /**
     * This method check if there are still characters to consume in the scanner/this.data
     * @return true  => there are still characters
     *         false => no character can be consumed
     */
    public boolean hasNext() {
        return this.data.length()!=0 || this.scanner.hasNext();
    }
    
    /**
     * This method return the current character
     * @return the first character ready to be consumed
     */
    public char peek() {
        // if this.data is empty, the exception will be sent in the upper layer
        // (bad use of this class)
        this.populate();
        return this.data.charAt(0);
    }
    
    /**
     * This method consume a character in the scanner/this.data and return it
     * @return the consumed character
     */
    public char next() {
        
        this.populate(); // repopulate this.data
        
        // get first char
        char c = this.data.charAt(0);
        
        // remove first character
        this.data = this.data.substring(1);
        
        // return the char
        return c;
        
    }
    
}
