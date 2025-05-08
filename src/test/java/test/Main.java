package test;

import com.example.compiler.Starter;
import com.example.compiler.grammer.Env;
import com.example.compiler.grammer.Executor;
import com.example.compiler.grammer.Parser;
import com.example.compiler.grammer.Statement;
import com.example.compiler.lexer.Token;
import com.example.compiler.lexer.TokenGenerator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/input.txt");
        Starter.run(fileInputStream);
    }

}
