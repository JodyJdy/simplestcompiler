package com.example.compiler.grammer;

import com.example.compiler.lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.compiler.grammer.Expr.*;
import static com.example.compiler.grammer.Statement.*;
import static com.example.compiler.grammer.Statement.DefineStatement.DefineSingleStatement;

public class Parser {
    private static final Token END = new Token("END OF TOKEN",-1);
    private final List<Token> tokens;
    private int cur = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }
    private void next(int n){
        cur+=n;
    }
    private void next(){
        cur++;
    }
    private boolean nextTokenIs(String v){
        return nextToken().getToken().equals(v);
    }
    private boolean curTokenIs(String v){
        return currToken().getToken().equals(v);
    }
    private boolean curTokenIn(String ...v){
        for(String t : v){
            if(currToken().getToken().equals(t)){
                return true;
            }
        }
        return false;
    }
    private boolean checkAndNext(String v){
        if(curTokenIs(v)){
            next();
            return true;
        }
        return false;
    }
    private void match(String v){
        if(curTokenIs(v)){
            next();
            return;
        }
        throw new RuntimeException("expect: '"+ v +"' find :'" + currToken().getToken()+"'  at line :"+ currToken().getLine());
    }
    private Token currToken(){
        if(cur >= tokens.size()){
            return END;
        }
        return tokens.get(cur);
    }
    private Token nextToken(){
        if(cur+1 >= tokens.size()){
            return END;
        }
        return tokens.get(cur+1);
    }

    public Map<String,FuncStatement> funcs() throws Exception {
        Map<String,FuncStatement> funcStatements = new HashMap<>();
        FuncStatement func = funcStatement();
        funcStatements.put(func.funcName,func);
        while(currToken() != END){
            FuncStatement f = funcStatement();
            funcStatements.put(f.funcName,f);
        }
        return funcStatements;
    }
    private static class TypeAndSize{
        int type;
        int size;
        TypeAndSize(int type, int size) {
            this.type = type;
            this.size = size;
        }
    }
    private TypeAndSize getVarType(){
        int type = Integer.MIN_VALUE;
        int size = Integer.MIN_VALUE;
        if(curTokenIn("void","int","string")) {
            if (curTokenIs("void")) {
                type = VarType.VOID;
                next();
            }
            if (curTokenIs("int")) {
                next();
                type = VarType.INT;
            }
            if (curTokenIs("string")) {
                next();
                type = VarType.STRING;
            }
        } else{
            throw new RuntimeException("error type :'" + currToken().getToken() + "'" + "at line :" + currToken().getLine());
        }
        if(curTokenIs("[")){
            next();
            if(!curTokenIs("]")){
                size = Integer.parseInt(currToken().getToken());
                next();
            }
            match("]");
            type+=1;
        }
        if(type == Integer.MIN_VALUE){
            throw new RuntimeException("expect var type, find : '"+currToken().getToken()+"' at line"+currToken().getLine());
        }
        return new TypeAndSize(type,size);
    }
    private FuncStatement funcStatement() throws Exception {
      int returnType = getVarType().type;
      String funcName = currToken().getToken();
      next();
      match("(");
      List<ArgExpr> args = new ArrayList<>();
      while (!curTokenIs(")")){
          int varType =getVarType().type;
          String varName = currToken().getToken();
          args.add(new ArgExpr(varName,varType));
          next();
          if(curTokenIs(",")){
              next();
          }
      }
      match(")");
      BlockStatement statements = block();
      return new FuncStatement(returnType,funcName,args,statements);
    }
    private BlockStatement block() throws Exception {
        match("{");
        List<Statement> statements = new ArrayList<>();
        while (true){
            checkAndNext(";");
            if(curTokenIs("}")||currToken() == END){
                break;
            }
            statements.add(statement());
        }
        match("}");
        return new BlockStatement(statements);
    }
    private Statement statement() throws Exception {
        String val = currToken().getToken();
        for(;;) {
            switch (val) {
                case "for":return forStat();
                case "if": return ifStat();
                case "do": return doStat();
                case "while": return whileStat();
                case "int": case "string": return defineStat();
                case "break": next();return new BreakStatement();
                case "continue": next();return new ContinueStatement();
                case "{": return block();
                case "return":
                    next();
                    if (!curTokenIs(";")) {
                        return new ReturnStatement(expr());
                    } else {
                        return new ReturnStatement(null);
                    }
                case ";": next();continue;
                default: return assignStat();
            }
        }
    }

    private Statement forStat() throws Exception {
        match("for");
        match("(");
        Statement first = null; Expr second = null;Statement third = null;
        if(!curTokenIs(";")){
            first =statement();
        }
        checkAndNext(";");
        if(!curTokenIs(";")){
            second =expr();
        }
       checkAndNext(";");
        if(!curTokenIs(")")){
            third =statement();
        }
        match(")");
        BlockStatement block = block();
        return new ForStatement(first,second,third,block);
    }
    private Statement ifStat() throws Exception {
        match("if");
        match("(");
        Expr ifCond = expr();
        match(")");
        BlockStatement ifBlock = block();
        List<Expr> elseIfConds = new ArrayList<>();
        List<BlockStatement> elseIfBlocks = new ArrayList<>();
        while(curTokenIs("else") && nextTokenIs("if")){
            next(2);
            match("(");
            elseIfConds.add(expr());
            match(")");
            elseIfBlocks.add(block());
        }
        BlockStatement elseBlock = null;
        if(curTokenIs("else")){
            next();
            elseBlock = block();
        }
        return new IfStatement(ifCond,ifBlock,elseIfConds,elseIfBlocks,elseBlock);
    }
    private Statement doStat() throws Exception {
        match("do");
        BlockStatement block = block();
        checkAndNext("while");
        match("(");
        Expr cond = expr();
        match(")");
        return new DoWhileStatement(block,cond);
    }
    private Statement whileStat() throws Exception {
        match("while");
        match("(");
        Expr cond = expr();
        match(")");
        BlockStatement block = block();
        return new WhileStatement(cond,block);
    }
    private Statement defineStat() throws Exception {
        TypeAndSize typeAndSize = getVarType();
        List<DefineSingleStatement> defineSingleStatements = new ArrayList<>();
        DefineStatement defineStatement = new DefineStatement(defineSingleStatements);
        while (true){
            if(!(currToken() instanceof Token.IdToken)){
                throw new RuntimeException("expect identifier but find :'"+currToken().getToken()+"'  at line :"+currToken().getLine());
            }
            String id = currToken().getToken();
            next(1);
            Expr expr = null;
            if(checkAndNext("=")) {
                expr = expr();
                if (expr instanceof ArrayInit) {
                    typeAndSize.size = ((ArrayInit) expr).exprs.size();
                }
            }
            defineSingleStatements.add(new DefineSingleStatement(typeAndSize.type,id,typeAndSize.size,expr));
            if(curTokenIs(",")){
                next();
            } else if(curTokenIs(";")){
                next();
                break;
            } else{
                throw new RuntimeException("expect ',' or ';' but find :'"+currToken().getToken()+"' at line :"+currToken().getLine());
            }
        }
        return  defineStatement;
    }
    private Statement assignStat() throws Exception {
        Expr left = expr();
        if(curTokenIs(";") && left instanceof FuncCallExpr){
            next();
            return new FuncCallStatement((FuncCallExpr)left);
        }
        match("=");
        Expr right = expr();
        return new AssignStatement(left,right);
    }
    private List<Expr> exprList() throws Exception {
        List<Expr> exprs = new ArrayList<>();
        while(!curTokenIn(")","}")){
            exprs.add(expr());
            if(curTokenIs(",")){
                next();
            } else{
                break;
            }
        }
        return exprs;
    }
    private Expr expr() throws Exception {
        return orExpr();
    }
    private Expr orExpr() throws Exception {
        Expr left = andExpr();
        while(curTokenIs("||")){
            next();
            left = new BoolExpr(left,andExpr(),"||");
        }
        return left;
    }
    private Expr andExpr() throws Exception {
        Expr left = relExpr();
        while(curTokenIs("&&")){
            next();
            left = new BoolExpr(left,relExpr(),"&&");
        }
        return left;
    }
    private Expr relExpr() throws Exception {
        Expr left = bitAnd();
        while(curTokenIn(">" ,">=","<","<=","==","!=")){
            String op = currToken().getToken();
            next();
            left = new RelExpr(left,bitAnd(),op);
        }
        return left;
    }
    private Expr bitAnd() throws Exception {
        Expr left = bitOr();
        while(curTokenIs("&")){
            next();
            left = new CalExpr(left,bitOr(),"&");
        }
        return left;
    }
    private Expr bitOr() throws Exception {
        Expr left = addSub();
        while(curTokenIs("|")){
            next();
            left = new CalExpr(left,addSub(),"|");
        }
        return left;
    }
    private Expr addSub() throws Exception {
        Expr left = mulDivMod();
        while(curTokenIn("+","-")){
            String op = currToken().getToken();
            next();
            left = new CalExpr(left,mulDivMod(),op);
        }
        return left;
    }
    private Expr mulDivMod() throws Exception {
        Expr left = single();
        while(curTokenIn("*","/","%")){
            String op = currToken().getToken();
            next();
            left = new CalExpr(left,single(),op);
        }
        return left;
    }
    private Expr single() throws Exception {
        if(currToken() instanceof Token.IdToken){
            String id = currToken().getToken();
            next();
            //数组访问
            if(curTokenIs("[")){
                next();
                Expr index = expr();
                next();
                return new ArrayUse(id,index);
            }
            //函数调用
            if(curTokenIs("(")){
                next();
                List<Expr> exprs = exprList();
                next();
                return new FuncCallExpr(id,exprs);
            }
            return new IdExpr(id);
        }
        if(currToken() instanceof Token.NumToken){
            int i = Integer.parseInt(currToken().getToken());
            next();
            return new IntExpr(i);
        }
        if(currToken() instanceof Token.StringToken){
            String str = currToken().getToken();
            next();
            return new StringExpr(str);
        }
        //数组初始化 int[xx] x = {1,2,3,4,5}
        if(curTokenIs("{")){
            next();
            List<Expr> exprs = exprList();
            next();
            return new ArrayInit(exprs);
        }
        // !(expr)
        if(curTokenIs("!")){
            match("!");
            match("(");
            Expr expr = expr();
            match(")");
            return new NotExpr(expr);
        }
        throw new Exception("error expr type, line :" + currToken().getLine() + " token: " + currToken().getToken());
    }
}
