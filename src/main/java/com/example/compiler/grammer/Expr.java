package com.example.compiler.grammer;

import java.util.List;

 abstract class Expr {
    static class ArgExpr extends Expr {
        String id;
        int type;
        ArgExpr(String id, int type) {
            this.id = id;
            this.type = type;
        }
    }
    static class ArrayInit extends Expr{
        List<Expr> exprs;
        ArrayInit(List<Expr> exprs) {
           this.exprs = exprs;
        }
    }

    static class ArrayUse extends Expr{
        String arrayId;
        Expr index;
        ArrayUse(String arrayId, Expr index) {
            this.arrayId = arrayId;
            this.index = index;
        }
    }
    static class BoolExpr extends Expr {
        Expr left,right;
        String op;
         BoolExpr(Expr left, Expr right, String op) {
            this.left = left;
            this.right = right;
            this.op = op;
        }
    }
     static class CalExpr extends BoolExpr {
         CalExpr(Expr left, Expr right, String op) {
             super(left, right, op);
         }
     }
     static class FuncCallExpr extends Expr {
        String funcName;
        List<Expr> args;
         FuncCallExpr(String funcName, List<Expr> args) {
            this.funcName = funcName;
            this.args = args;
        }
    }
     static class IdExpr extends Expr{
        String id;
         IdExpr(String id) {
            this.id = id;
        }
    }
     static class IntExpr extends Expr {
        int i;
         IntExpr(int i) {
            this.i = i;
        }
    }
     static class RelExpr extends BoolExpr{
         RelExpr(Expr left, Expr right, String op) {
             super(left, right, op);
         }
     }
     static class StringExpr extends Expr {
        String str;
         StringExpr(String str) {
            this.str = str;
        }
    }
     static class NotExpr extends Expr{
        Expr expr;
         NotExpr(Expr expr){
            this.expr = expr;
        }
    }
}
