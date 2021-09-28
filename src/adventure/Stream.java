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
    
    private void populate() {
        // if empty repopulate
        if (this.data.isEmpty()) this.data = this.scanner.next();
    }
    
    public boolean hasNext() {
        return this.data.length()!=0 || this.scanner.hasNext();
    }
    
    public char peek() {
        this.populate();
        return this.data.charAt(0);
    }
    
    public char next() {
        
        this.populate();
        
        // get first char
        char c = this.data.charAt(0);
        
        // remove first character
        this.data = this.data.substring(1);
        
        // return the char
        return c;
        
    }
    
}
