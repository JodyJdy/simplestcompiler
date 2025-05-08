package com.example.compiler.grammer;

import java.util.ArrayList;
import java.util.List;

import static com.example.compiler.grammer.Expr.*;
public abstract class Statement {

    static class WhileStatement extends Statement {
        Expr condition;
        BlockStatement block;
        WhileStatement(Expr conditon, BlockStatement block) {
            this.condition = conditon;
            this.block = block;
        }
    }
    static class AssignStatement extends Statement {
        Expr left,right;
        AssignStatement(Expr left, Expr right) {
            this.left = left;
            this.right = right;
        }
    }
    static class BreakStatement extends Statement {}
    static class ContinueStatement extends Statement {}
    static class DefineStatement extends Statement {
        List<DefineSingleStatement> singleStatements;
        DefineStatement(List<DefineSingleStatement> singleStatements) {
            this.singleStatements = singleStatements;
        }

        static class DefineSingleStatement extends Statement{
            int type;
            String id;
            int size;
            Expr right;
            DefineSingleStatement(int type, String id, int size, Expr right) {
                this.type = type;
                this.id = id;
                this.size = size;
                this.right = right;
            }
        }
    }
    static class BlockStatement extends Statement{
        List<Statement> block;
        BlockStatement(List<Statement> block){
            this.block = block;
        }
        public static BlockStatement emptyBlock(){
            return new BlockStatement(new ArrayList<>());
        }
    }
    static class DoWhileStatement extends Statement {
        BlockStatement block;
        Expr cond;
        DoWhileStatement(BlockStatement block, Expr cond) {
            this.block = block;
            this.cond = cond;
        }
    }
    static class ForStatement extends Statement{
        Statement statement;
        Expr condition1Expr;
        Statement statement2;
        BlockStatement block;
        ForStatement(Statement statement, Expr condition1Expr, Statement statement2, BlockStatement block) {
            this.statement = statement;
            this.condition1Expr = condition1Expr;
            this.statement2 = statement2;
            this.block = block;
        }
    }
    static class FuncCallStatement extends Statement {
        FuncCallExpr funcCallExpr;
        FuncCallStatement(FuncCallExpr funcCallExpr) {
            this.funcCallExpr = funcCallExpr;
        }
    }
    public static class FuncStatement {
        int returnType;
        String funcName;
        List<ArgExpr> args;
        BlockStatement block;
        FuncStatement(int returnType, String funcName, List<ArgExpr> args, BlockStatement block) {
            this.returnType = returnType;
            this.funcName = funcName;
            this.args = args;
            this.block = block;
        }
        public static FuncStatement emptyFunc(){
            return new FuncStatement(VarType.VOID, "main", new ArrayList<>(), BlockStatement.emptyBlock());
        }
    }
    static class IfStatement extends Statement {
        Expr ifCond;
        BlockStatement ifBlock;
        List<Expr> elseIfConds;
        List<BlockStatement> elseIfBlocks;
        BlockStatement elseBlock;
        IfStatement(Expr ifCond, BlockStatement ifBlock, List<Expr> elseIfConds, List<BlockStatement> elseIfBlocks, BlockStatement elseBlock) {
            this.ifCond = ifCond;
            this.ifBlock = ifBlock;
            this.elseIfConds = elseIfConds;
            this.elseIfBlocks = elseIfBlocks;
            this.elseBlock = elseBlock;
        }
    }
    static class ReturnStatement extends Statement {
        Expr expr;
        ReturnStatement(Expr expr) {
            this.expr = expr;
        }
    }

}
