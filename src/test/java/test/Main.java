package test;

import com.example.compiler.Starter;

import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("input.txt");
        Starter.run(fileInputStream);
    }

}
