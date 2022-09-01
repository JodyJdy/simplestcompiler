package com.example.compiler.lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenGenerator {

    private static final Pattern NUM = Pattern.compile("[1-9]\\d*|0");
    private static final Pattern ID = Pattern.compile("[A-Za-z][a-zA-Z_0-9]*");
    private static final List<String> KEY_WORDS = Arrays.asList( "do", "while", "for", "break", "continue", "if", "main", "int", "return",
            "string","else", "void");
    private static final String[] OPES = new String[]{
            "(", ")", "{", "}", "[", "]", ">=", "<=", "==",">","<","!=","&&","||","!","^","|","~","&",
            "=","-", "+", "/", "%", "*", ":",";",","
    };
    public static List<Token> generateToken(File file) {
        try {
            return generateToken(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
    public static List<Token> generateToken(InputStream in) {
        Scanner sc;
        sc = new Scanner(in);
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        return generateToken(sb.toString());
    }


    private static List<Token> generateToken(String input) {
        int lineNum = 1;
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char ch = input.charAt(i);
            if(ch == '/'){
                int j = i + 2;
                if(i + 1 < input.length()){
                    //单行注释
                    if(input.charAt(i+1) == '/') {
                        while (j < input.length() && input.charAt(j) != '\n') {
                            j++;
                        }
                        j++;
                        lineNum++;
                        i = j;
                        continue;
                    //多行注释
                    } else if(input.charAt(i+1) == '*'){
                        while(j < input.length()){
                            if(input.charAt(j) == '\n'){
                                lineNum++;
                            }
                            if(input.charAt(j) == '*' && j + 1 < input.length() && input.charAt(j+1) =='/'){
                                j+=2;
                                break;
                            }
                            j++;
                        }
                        i = j;
                        continue;
                    }
                }
            }
            if(ch == '\n'){
                lineNum++;
                i++;
                continue;
            }
            //num
            if(Character.isDigit(ch)) {
                Matcher num = NUM.matcher(input);
                if (num.find(i)) {
                    tokens.add(new Token.NumToken(input.substring(num.start(), num.end()),lineNum));
                    i = num.end();
                    continue;
                }
            }
            //key_word  and id
            if(Character.isAlphabetic(ch) || ch == '_') {
                Matcher id = ID.matcher(input);
                if (id.find(i)) {
                    i = id.end();
                    String str = input.substring(id.start(), id.end());
                    if(KEY_WORDS.contains(str)){
                        tokens.add(new Token(str,lineNum));
                        continue;
                    }
                    tokens.add(new Token.IdToken(str,lineNum));
                    continue;
                }
            }
            //提取字符串
            if (ch == '"') {
                int j = i + 1;
                while (input.charAt(j) != '"' || input.charAt(j - 1) == '\\'){
                    if(input.charAt(j) == '\n'){
                        lineNum++;
                    }
                    j++;
                }
                j++;
                tokens.add(new Token.StringToken(input.substring(i+1, j-1),lineNum));
                i = j;
                continue;
            }
            boolean isFind = false;
            //提取运算符
            for (String op : OPES) {
                if (i + op.length() <= input.length() && op.equals(input.substring(i, i + op.length()))) {
                    tokens.add(new Token(op,lineNum));
                    i += op.length();
                    isFind = true;
                    break;
                }
            }
            if (isFind) {
                continue;
            }
            i++;
        }
        return tokens;
    }
}
