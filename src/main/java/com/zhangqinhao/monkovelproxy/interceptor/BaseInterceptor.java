package com.zhangqinhao.monkovelproxy.interceptor;

import com.zhangqinhao.monkovelproxy.controller.BaseController;
import com.zhangqinhao.monkovelproxy.util.StringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseInterceptor {
    public Cookie[] getCookieObjects(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getCookies();
    }

    public Cookie getCookieObject(HttpServletRequest httpServletRequest, String key) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public String getCookie(HttpServletRequest httpServletRequest, String key) {
        return getCookie(httpServletRequest, key, null);
    }

    public String getCookie(HttpServletRequest httpServletRequest, String key, String def) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    return cookies[i].getValue() == null ? def : cookies[i].getValue();
                }
            }
        }
        return def;
    }

    public Integer getCookieInt(HttpServletRequest httpServletRequest, String key) {
        return getCookieInt(httpServletRequest, key, null);
    }

    public Integer getCookieInt(HttpServletRequest httpServletRequest, String key, Integer def) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    try {
                        return StringUtil.isEmpty(cookies[i].getValue()) ? def : Integer.parseInt(cookies[i].getValue());
                    }catch (Exception e){
                        return def;
                    }
                }
            }
        }
        return def;
    }

    public Long getCookieLong(HttpServletRequest httpServletRequest, String key) {
        return getCookieLong(httpServletRequest, key, null);
    }

    public Long getCookieLong(HttpServletRequest httpServletRequest, String key, Long def) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    try{
                        return StringUtil.isEmpty(cookies[i].getValue()) ? def : Long.parseLong(cookies[i].getValue());
                    }catch (Exception e){
                        return def;
                    }
                }
            }
        }
        return def;
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, String value) {
        setCookie(httpServletRequest,httpServletResponse, key, value, BaseController.WEB_KEY_COOKIE_DEFAULT);
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, String value, int maxAge) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies!=null && cookies.length>0){
            for(int i=0;i<cookies.length;i++){
                if(cookies[i].getName().equals(key)){
                    cookies[i].setValue(value);
                    cookies[i].setMaxAge(maxAge);
                    httpServletResponse.addCookie(cookies[i]);
                    return ;
                }
            }
        }else{
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, int value) {
        setCookie(httpServletRequest,httpServletResponse, key, String.valueOf(value), BaseController.WEB_KEY_COOKIE_DEFAULT);
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, int value, int maxAge) {
        setCookie(httpServletRequest,httpServletResponse, key, String.valueOf(value), maxAge);
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, long value) {
        setCookie(httpServletRequest,httpServletResponse, key, String.valueOf(value), BaseController.WEB_KEY_COOKIE_DEFAULT);
    }

    public void setCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key, long value, int maxAge) {
        setCookie(httpServletRequest,httpServletResponse, key, String.valueOf(value), maxAge);
    }

    public void removeCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key){
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies!=null && cookies.length>0){
            for(int i=0;i<cookies.length;i++){
                if(cookies[i].getName().equals(key)){
                    cookies[i].setValue(null);
                    cookies[i].setMaxAge(0);
                    httpServletResponse.addCookie(cookies[i]);
                    return ;
                }
            }
        }
    }

    public String getQueryString(HttpServletRequest request,String key){
        return getQueryString(request,key,null);
    }
    public String getQueryString(HttpServletRequest request,String key,String def){
        if(request==null){
            return def;
        }
        if(StringUtil.isEmpty(key)){
            return def;
        }
        String queryString = request.getQueryString();
        if(StringUtil.isEmpty(queryString)){
            return def;
        }
        String[] querys = queryString.split("&");
        if(querys==null ||querys.length==0){
            return def;
        }
        for(String item:querys){
            item = item.trim();
            if(item.startsWith(key+"=")){
                return item.replace(key+"=","");
            }
        }
        return def;
    }
}
