package com.example.compiler.grammer;

import java.util.Arrays;
import java.util.List;

import static com.example.compiler.grammer.Executor.LocalVar;
class NativeCall {

    static LocalVar execute(String funcName, List<LocalVar> args) throws Exception {
        switch (funcName){
            case "print":return print(args);
            case "len":return len(args);
            default:throw new Exception("function doesn't exist");
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
        System.out.println(sb.toString());
        return null;
    }
    private static LocalVar len(List<LocalVar> args){
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
}
