package com.zhangqinhao.monkovelproxy.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringUtil {
    private final static Logger logger = LogManager.getLogger(StringUtil.class.getName());

    public static boolean isEmpty(String data){
        if(data==null || data.length()==0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNotEmpty(String data){
        return !isEmpty(data);
    }
}
