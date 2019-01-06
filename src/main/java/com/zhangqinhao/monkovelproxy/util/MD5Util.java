package com.zhangqinhao.monkovelproxy.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;

public class MD5Util {
    private final static Logger logger = LogManager.getLogger(MD5Util.class.getName());
    /**
     * 字符串MD5加密  大写+数字
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("需要MD5加密字符串:"+s+"    错误原因:"+e.getMessage());
            return null;
        }
    }

    public static void main(String[] arg){
        String a = "zQh19931118";
        System.out.println(MD5(a));
    }
}
