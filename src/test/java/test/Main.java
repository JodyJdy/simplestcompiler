package test;

import com.example.compiler.grammer.Env;
import com.example.compiler.grammer.Executor;
import com.example.compiler.grammer.Parser;
import com.example.compiler.grammer.Statement;
import com.example.compiler.lexer.Token;
import com.example.compiler.lexer.TokenGenerator;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream ins = Main.class.getClassLoader().getResourceAsStream("input.txt");
        List<Token> tokenList = TokenGenerator.generateToken(ins);
        Parser parser = new Parser(tokenList);
        //解析出所有函数
        Map<String, Statement.FuncStatement> funcStatementMap = parser.funcs();
        Executor.setUserFuncs(funcStatementMap);
        // 找到main函数 执行
        Statement.FuncStatement funcStatement  = funcStatementMap.get("main");
        new Executor(new Env(),funcStatement).execute();
    }

}
