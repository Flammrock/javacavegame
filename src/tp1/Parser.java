/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Parser {
    
    protected Stream stream;

    public Parser(Stream stream) {
        this.stream = stream;
    }
    
    public boolean hasNextToken() {
        return this.stream.hasNext();
    }
    
    public String getUntilMeetChar(char specialchar) {
        String result = "";
        while (true) {
            if (!this.stream.hasNext()) return result;
            char c = this.stream.next();
            if (c==specialchar) return result;
            if (c=='\n') return result;
            if (c=='\r' && this.stream.peek()=='\n') {
                this.stream.next();
                return result;
            }
            result += c;
        }
    }
    
    public Token getNextToken() {
        
        // if no next token, abort
        if (!this.hasNextToken()) return null;
        
        // consume a character
        char c = this.stream.peek();
            
        // comment
        if (c=='#') {
            // read the line and return the token
            return new Token(this.getUntilMeetChar('\n'));
        }
        
        // !
        if (c=='!') {
            this.stream.next();
            return new Token("!");
        }
            
        // else
        return new Token(this.getUntilMeetChar(','));
        
    }
    
    public List<Token> getTokenList() {
        List<Token> tokens = new ArrayList<>();
        while (!this.hasNextToken()) {
            tokens.add(this.getNextToken());
        }
        return tokens;
    }
    
}
