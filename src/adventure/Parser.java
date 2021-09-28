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
    
    /**
     * when a token is extracted from the stream,
     *  - if this.acceptEmptyToken is true then :
     *         if the token is empty, the token is still sent to upper layer
     *  - else if this.acceptEmptyToken is false :
     *         if the token is empty, the token is NOT sent to upper layer
     */
    protected boolean acceptEmptyToken = true;

    public Parser(Stream stream) {
        this.stream = stream;
        this.current = null;
        this.currentline = 0;
    }

    public boolean isAcceptEmptyToken() {
        return acceptEmptyToken;
    }

    public void setAcceptEmptyToken(boolean acceptEmptyToken) {
        this.acceptEmptyToken = acceptEmptyToken;
    }
    
    

    public char getCharComment() {
        return charComment;
    }

    public void setCharComment(char charComment) {
        this.charComment = charComment;
    }

    public char getCharSize() {
        return charSize;
    }

    public void setCharSize(char charSize) {
        this.charSize = charSize;
    }

    public char getCharSeparator() {
        return charSeparator;
    }

    public void setCharSeparator(char charSeparator) {
        this.charSeparator = charSeparator;
    }
    
    /**
     * This method check if we can extract one more token from the stream
     * @return true  => there are still tokens
     *         false => no tokens to extract
     */
    public boolean hasNextToken() {
        return this.stream.hasNext(); // based on the method hasNext() of the stream
    }
    
    /**
     * This method extract all characters from the stream until a line feed is met or a specialchar (passed as argument)
     * @param specialchar
     * @return return the extracted characters
     */
    public String getUntilMeetChar(char specialchar) {
        String result = ""; // store the extracted characters here
        
        // while we can extract characters
        while (true) {
            
            // if no next character return the built string
            if (!this.stream.hasNext()) return result;
            
            // consume a character from the stream
            char c = this.stream.next();
            
            // stop read if we meet a special char
            if (c==specialchar) return result;
            
            // unix line feed \n
            if (c=='\n') {
                this.currentline++; // track line
                return result;
            }
            
            // windows line feed \r\n
            if (c=='\r' && this.stream.peek()=='\n') {
                this.currentline++; // track line
                this.stream.next(); // consume the '\n'
                return result;
            }
            
            // concat the char to the result string
            result += c;
        }
    }
    
    /**
     * This method return the last token extracted from the stream
     * @return last token extracted (can be null)
     */
    public Token peekToken() {
        return this.current;
    }
    
    /**
     * This method extract a token from the stream
     * @return a token (can be null)
     */
    public Token getNextToken() {
        
        // if no next token, abort
        if (!this.hasNextToken()) return null;
        
        // get the current character
        char c = this.stream.peek();
            
        // comment
        if (c==this.charComment) {
            // read the line and return the token
            this.current = new Token(this.getUntilMeetChar('\n'));
            return this.current;
        }
        
        // !
        if (c==this.charSize) {
            this.stream.next(); // consume the character
            this.current = new Token(String.valueOf(this.charSize)); // and build the token
            return this.current;
        }
            
        // else
        this.current = new Token(this.getUntilMeetChar(this.charSeparator)); // extract until a charSeparator is met
        return this.current;
        
    }
    
    public List<Token> getTokenList() {
        List<Token> tokens = new ArrayList<>();
        while (this.hasNextToken()) {
            tokens.add(this.getNextToken());
        }
        return tokens;
    }
    
    /**
     * This method is a shortcut to ignore all lines which are comments
     */
    public void skipComments() {
        this.ignoreLinesStartWith(this.charComment);
    }
    
    /**
     * This method return a list of tokens that represent an "object"
     * @return a list of token (never null)
     */
    public List<Token> getNextObject() {
        List<Token> tokens = new ArrayList<>(); // first create the list
        
        // skip all comments
        while (true) {
            this.skipComments();
            break;
        }
        int line = this.currentline;
        while (line==this.currentline) {
            if (!this.hasNextToken()) return tokens;
            if (this.isAcceptEmptyToken()) {
                tokens.add(this.getNextToken());
            } else {
                Token token = this.getNextToken();
                String data = token.getData();
                if (!data.isEmpty()) tokens.add(token);
            }
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
