package com.zhangqinhao.monkovelproxy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 服务启动后执行
 * @author zhangqinhao
 * @date 2-18-08-20
 */
public class ApplicationListenerImpl implements ServletContextListener {
    public static HashMap<String ,Object> configMap = new HashMap<String, Object>();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(ApplicationListenerImpl.class.getResourceAsStream("/config.properties"),"utf-8"));
            Set<Map.Entry<Object,Object>> entrySet = properties.entrySet();
            for (Map.Entry<Object, Object> entry : entrySet) {
                configMap.put(String.valueOf(entry.getKey()),entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static String getConfigStr(String key){
        return getConfigStr(key,null);
    }
    public static String getConfigStr(String key,String def){
        String value = (String) configMap.get(key);
        if(value == null){
            return def;
        }else{
            return value;
        }
    }
    public static void setConfigStr(String key, String value){
        configMap.put(key,value);
    }

    public static Boolean getConfigBoolean(String key){
        return getConfigBoolean(key,false);
    }
    public static Boolean getConfigBoolean(String key,boolean def){
        Boolean value = Boolean.parseBoolean((String) configMap.get(key));
        if(value == null){
            return def;
        }else{
            return value;
        }
    }
    public static void setConfigBoolean(String key,boolean value){
        configMap.put(key,Boolean.toString(value));
    }

    public static Integer getConfigInt(String key){
        return getConfigInt(key,null);
    }
    public static Integer getConfigInt(String key,Integer def){
        Integer value = Integer.parseInt((String) configMap.get(key));
        if(value == null){
            return def;
        }else{
            return value;
        }
    }
    public static void setConfigInt(String key,Integer value){
        configMap.put(key,value==null?null:Integer.toString(value));
    }
}
