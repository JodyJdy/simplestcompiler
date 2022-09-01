package com.example.compiler.lexer;

public class Token {

    private final String token;
    private final int line;
    public Token(String token,int line){
        this.token = token;
        this.line = line;
    }
    public int getLine(){
        return line;
    }
    public String getToken(){
        return token;
    }
    public static class IdToken extends Token {
        public IdToken(String token,int line) {
            super(token,line);
        }
    }
    public static class StringToken extends Token {
        public StringToken(String token,int line) {
            super(token,line);
        }
    }
    public static class NumToken extends Token {
        public NumToken(String token,int line) {
            super(token,line);
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", line=" + line +
                '}';
    }
}
