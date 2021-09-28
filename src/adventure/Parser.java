/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

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
    
    // the stream
    protected Stream stream;
    
    // contains the last token extracted from the stream
    protected Token current;
    
    // track the line when parsing the stream
    protected int currentline;
    
    
    protected char charComment   = '#';
    protected char charSize      = '!';
    protected char charSeparator = ',';
    

    public Parser(Stream stream) {
        this.stream = stream;
        this.current = null;
        this.currentline = 0;
    }
    
    public boolean hasNextToken() {
        return this.stream.hasNext();
    }
    
    public String getUntilMeetChar(char specialchar) {
        String result = "";
        while (true) {
            
            // if no next character return the built string
            if (!this.stream.hasNext()) return result;
            
            // consume a character from the stream
            char c = this.stream.next();
            
            // stop read
            if (c==specialchar) return result;
            
            // unix line feed \n
            if (c=='\n') {
                this.currentline++; // track line
                return result;
            }
            
            // windows line feed \r\n
            if (c=='\r' && this.stream.peek()=='\n') {
                this.currentline++; // track line
                this.stream.next();
                return result;
            }
            
            // concat the char to the result string
            result += c;
        }
    }
    
    public Token peekToken() {
        return this.current;
    }
    
    public Token getNextToken() {
        
        // if no next token, abort
        if (!this.hasNextToken()) return null;
        
        // consume a character
        char c = this.stream.peek();
            
        // comment
        if (c==this.charComment) {
            // read the line and return the token
            this.current = new Token(this.getUntilMeetChar('\n'));
            return this.current;
        }
        
        // !
        if (c==this.charSize) {
            this.stream.next();
            this.current = new Token(String.valueOf(this.charSize));
            return this.current;
        }
            
        // else
        this.current = new Token(this.getUntilMeetChar(this.charSeparator));
        return this.current;
        
    }
    
    public List<Token> getTokenList() {
        List<Token> tokens = new ArrayList<>();
        while (this.hasNextToken()) {
            tokens.add(this.getNextToken());
        }
        return tokens;
    }
    
    public void skipComments() {
        this.ignoreLinesStartWith(this.charComment);
    }
    
    public List<Token> getNextObject() {
        List<Token> tokens = new ArrayList<>();
        while (true) {
            this.skipComments();
            break;
        }
        int line = this.currentline;
        while (line==this.currentline) {
            if (!this.hasNextToken()) return tokens;
            tokens.add(this.getNextToken());
        }
        return tokens;
    }
    
    
    
    private boolean ignoreLinesStartWith(char a) {
        if (!this.hasNextToken()) return false;
        boolean b = false;
        while (true) {
            char c = this.stream.peek();
            if (c==a) {
                this.getUntilMeetChar('\n');
                b = true;
                continue;
            }
            return b;
        }
    }
    
}
