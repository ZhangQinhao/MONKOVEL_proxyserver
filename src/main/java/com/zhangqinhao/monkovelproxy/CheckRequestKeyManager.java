package com.zhangqinhao.monkovelproxy;

import com.zhangqinhao.monkovelproxy.util.LRULinkedHashMap;

public class CheckRequestKeyManager {
    private static CheckRequestKeyManager instance = null;
    private static LRULinkedHashMap<String,Integer> data;
    private CheckRequestKeyManager(){
        data = new LRULinkedHashMap<String, Integer>(100000);
    }
    public static CheckRequestKeyManager getInstance(){
        if(instance==null){
            synchronized (CheckRequestKeyManager.class){
                if(instance==null){
                    instance = new CheckRequestKeyManager();
                }
            }
        }
        return instance;
    }

    public boolean isExist(String key){
        if(data.get(key)==null){
            return false;
        }else{
            return true;
        }
    }

    public void setKey(String key){
        data.put(key,1);
    }
}
