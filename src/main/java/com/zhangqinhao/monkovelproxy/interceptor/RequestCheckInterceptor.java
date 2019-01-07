package com.zhangqinhao.monkovelproxy.interceptor;

import com.zhangqinhao.monkovelproxy.ApplicationListenerImpl;
import com.zhangqinhao.monkovelproxy.CheckRequestKeyManager;
import com.zhangqinhao.monkovelproxy.util.StringUtil;
import com.zhangqinhao.monkovelproxy.util.aes.AESUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求验证
 */
public class RequestCheckInterceptor extends BaseInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(ApplicationListenerImpl.getConfigBoolean("checkState",false)){
            String AESkey = ApplicationListenerImpl.getConfigStr("AESkey");
            if(StringUtil.isEmpty(AESkey)){
                reponse(httpServletResponse,"已经开启请求验证，服务器未配置AESkey");
                return false;
            }
            String packageName = ApplicationListenerImpl.getConfigStr("packageName");
            if(StringUtil.isEmpty(packageName)){
                reponse(httpServletResponse,"已经开启请求验证，服务器未配置packageName");
                return false;
            }
            String proxyPackagename = getQueryString(request,"proxyPackagename");
            if(StringUtil.isEmpty(proxyPackagename)){
                reponse(httpServletResponse,"已经开启请求验证，缺失proxyPackagename");
                return false;
            }
            try{
                String decodeContent = AESUtil.aesDecode(proxyPackagename,AESkey);
                //解码后根据*分割，将*号后部分存储，校验遍历，使用过一次的proxyPackagename则不允许再使用，这里使用Hashmap今后可以使用数据库存储
                if(decodeContent==null|| decodeContent.length()==0 || !decodeContent.startsWith(packageName)){
                    reponse(httpServletResponse,"已经开启请求验证，校验未通过");
                    return false;
                }else{
                    if(CheckRequestKeyManager.getInstance().isExist(proxyPackagename)){
                        reponse(httpServletResponse,"已经开启请求验证，该key使用过");
                        return false;
                    }else{
                        CheckRequestKeyManager.getInstance().setKey(proxyPackagename);
                    }
                }
            }catch (Exception e){
                reponse(httpServletResponse,"已经开启请求验证，校验未通过");
                return false;
            }
        }
        return true;
    }

    private void reponse(HttpServletResponse httpServletResponse,String content) throws IOException {
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(content);
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
