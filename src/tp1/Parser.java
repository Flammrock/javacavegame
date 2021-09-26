/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Parser {
    
    protected Scanner scanner;

    public Parser(InputStream stream) {
        this.scanner = new Scanner(stream);
    }
    
    public boolean hasNextToken() {
        return this.scanner.hasNext();
    }
    
    protected String getUntilMeetChar(char specialchar) {
        String result = "";
        while (true) {
            if (!this.scanner.hasNext()) return result;
            char c = this.scanner.next().charAt(0);
            if (c==specialchar) return result;
            if (c=='\n') return result;
            result += c;
        }
    }
    
    public Token getNextToken() {
        
        // if no next token, abort
        if (!this.hasNextToken()) return null;
        
        // consume a character
        char c = this.scanner.next().charAt(0);
            
        // comment
        if (c=='#') {
            // read the line and return the token
            return new Token(this.getUntilMeetChar('\n'));
        }
        
        // !
        if (c=='!') return new Token("!");
            
        // else
        return new Token(this.getUntilMeetChar(','));
        
    }
    
}
