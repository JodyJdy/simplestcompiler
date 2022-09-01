package com.example.compiler.grammer;

import java.util.HashMap;
import java.util.Map;

import static com.example.compiler.grammer.Executor.LocalVar;
/**
 * 存储变量
 */
public class Env {
    private final Map<String, LocalVar> map = new HashMap<>();
    Env last;

    /**
     *变量定义时使用
     */
    public void putNewVar(String id,LocalVar localVar){
        if(map.containsKey(id)){
            throw new RuntimeException("变量重定义:" + id);
        }
        map.put(id,localVar);
    }
    /**
     *变量赋值时使用
     */
    public void put(String id, LocalVar localVar){
        if(map.containsKey(id)){
            map.put(id,localVar);
            return;
        }
        if(last != null){
            last.put(id,localVar);
            return;
        }
        throw new RuntimeException("变量不存在："+id);
    }
    public LocalVar get(String id){
        if(map.containsKey(id)){
            return map.get(id);
        }
        if(last != null){
            return last.get(id);
        }
        return null;
    }

    public void setLast(Env last) {
        this.last = last;
    }
}
