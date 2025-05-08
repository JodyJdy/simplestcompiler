package com.example.compiler.grammer;

import java.util.Arrays;
import java.util.List;

import static com.example.compiler.grammer.Executor.LocalVar;
class NativeCall {

    static LocalVar execute(String funcName, List<LocalVar> args) {
        switch (funcName){
            case "print":return print(args);
            case "len":return len(args);
            case "append": return append(args);
            default:throw new RuntimeException("function doesn't exist");
        }
    }

    private static LocalVar print(List<LocalVar> args){
        StringBuilder sb = new StringBuilder();
        for(LocalVar arg : args){
            if(arg.type == VarType.INT){
                sb.append(arg.i).append(" ");
            } else if(arg.type == VarType.STRING){
                sb.append(arg.str).append(" ");
            } else if(arg.type == VarType.INT_ARRAY){
                sb.append(Arrays.toString(arg.ints)).append(" ");
            } else {
                sb.append(Arrays.toString(arg.strs)).append(" ");
            }
        }
        System.out.println(sb);
        return null;
    }

    /**
     *返回数组长度
     */
    private static LocalVar len(List<LocalVar> args){
        if(args.size() > 1){
            System.err.println("len函数的参数数量不符");
            System.exit(1);
        }
        LocalVar array = args.get(0);
        LocalVar result = new LocalVar();
        result.type = VarType.INT;
        if(array.type == VarType.STRING_ARRAY){
            result.i = array.strs.length;
        } else{
            result.i = array.ints.length;
        }
        return result;
    }
    private static LocalVar append(List<LocalVar> args){
        if(args.size() < 2){
            System.err.println("append函数的参数数量不符");
            System.exit(1);
        }
        LocalVar array = args.get(0);
        if(array.type != VarType.STRING_ARRAY && array.type != VarType.INT_ARRAY){
            System.err.println("append函数的首个参数不是数组类型");
            System.exit(1);
        }
        int size = array.type == VarType.INT_ARRAY ? array.ints.length : array.strs.length;
        int subType = array.type == VarType.INT_ARRAY ? VarType.INT : VarType.STRING;
        for(int i=1;i<args.size();i++){
            LocalVar arg = args.get(i);
            if (arg.type != subType) {
                System.err.println("向数组添加元素的类型和数组类型不一致");
                System.exit(1);
            }
        }
        int appendedSize = size + args.size() - 1;
        if(array.type == VarType.INT_ARRAY){
            int[] appendedInts = Arrays.copyOf(array.ints, appendedSize);
            for(int i=1;i<args.size();i++){
               appendedInts[size + i - 1] = args.get(i).i;
            }
            array.ints = appendedInts;
        } else{
            String[] appendedStrs = Arrays.copyOf(array.strs, appendedSize);
            for(int i=1;i<args.size();i++){
                appendedStrs[size + i - 1] = args.get(i).str;
            }
            array.strs = appendedStrs;
        }
        return  array;

    }
}
