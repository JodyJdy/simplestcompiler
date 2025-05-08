package com.example.compiler.grammer;

/**
 * 解析命令行
 * 命令行可能是一个 statement 也可能是一个 表达式
 */
public class CommandLine {
    public Statement getStatement() {
        return statement;
    }

    public Expr getExpr() {
        return expr;
    }

    Statement statement;
    Expr expr;

    public CommandLine(Statement statement) {
        this.statement = statement;
    }

    public CommandLine(Expr expr) {
        this.expr = expr;
    }
}
