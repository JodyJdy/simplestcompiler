package com.example.compiler;

import com.example.compiler.grammer.*;
import com.example.compiler.lexer.Token;
import com.example.compiler.lexer.TokenGenerator;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 用于执行脚本
 * @author  jdy
 */
public class Starter {
    /**
     * 运行输入的文件
     */
    public static void run(InputStream inputStream) {
        List<Token> tokenList = TokenGenerator.generateToken(inputStream);
        execute(tokenList);
    }

    private static void execute(List<Token> tokenList){
        Parser parser = new Parser(tokenList);
        //解析出所有函数
        Map<String, Statement.FuncStatement> funcStatementMap = parser.funcs();
        Executor.execute(funcStatementMap);
    }


    public static void run(File file) {
        try {
            run(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runFileName(String fileName) {
        try {
            run(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(String string) {
        List<Token> tokenList = TokenGenerator.generateToken(string);
        execute(tokenList);
    }

    /**
     * 交互式命令行
     */
    public static void commandLine(){
        //函数表
        Map<String, Statement.FuncStatement> funcStatementMap = new HashMap<>(2) {
            {
                put("main", Statement.FuncStatement.emptyFunc());
            }
        };
        //初始化执行器
        Executor executor = new Executor(funcStatementMap);
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------输入 bye  结束执行,多行使用 \\结尾 ");
        System.out.print(">>");
        StringBuilder command = new StringBuilder();
        while (scanner.hasNext()) {
            //读取一行命令
            String line = scanner.nextLine();
            if ("bye".equals(line)) {
                break;
            }
            //多行
            if(line.charAt(line.length()-1)=='\\'){
               line = line.substring(0,line.length()-1);
               command.append(line);
               continue;
            } else{
                command.append(line);
            }
            List<Token> tokens = TokenGenerator.generateToken(command.toString());
            Parser parser = new Parser(tokens);
            CommandLine commandLine = parser.commandLine();
            if(commandLine.getExpr() != null){
                Executor.LocalVar localVar = executor.executeExpr(commandLine.getExpr());
                System.out.println(localVar);
            } else{
               executor.executeStatement(commandLine.getStatement());
            }
            //清空
            System.out.print(">>");
            command = new StringBuilder();
        }
    }
}
