package com.zhangqinhao.monkovelproxy.interceptor;

import com.zhangqinhao.monkovelproxy.ApplicationListenerImpl;
import com.zhangqinhao.monkovelproxy.util.StringUtil;
import com.zhangqinhao.monkovelproxy.util.aes.AESUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求验证
 */
public class RequestCheckInterceptor extends BaseInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(ApplicationListenerImpl.getConfigBoolean("checkState",false)){
            String AESkey = ApplicationListenerImpl.getConfigStr("AESkey");
            if(StringUtil.isEmpty(AESkey)){
                httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.getWriter().write("已经开启请求验证，服务器未配置AESkey");
                return false;
            }
            String packageName = ApplicationListenerImpl.getConfigStr("packageName");
            if(StringUtil.isEmpty(packageName)){
                httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.getWriter().write("已经开启请求验证，服务器未配置packageName");
                return false;
            }
            String proxyPackagename = getQueryString(request,"proxyPackagename");
            if(StringUtil.isEmpty(proxyPackagename)){
                httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.getWriter().write("已经开启请求验证，缺失proxyPackagename");
                return false;
            }
            try{
                if(!packageName.equals(AESUtil.aesDecode(proxyPackagename,AESkey))){
                    httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.getWriter().write("已经开启请求验证，校验未通过");
                    return false;
                }
            }catch (Exception e){
                httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.getWriter().write("已经开启请求验证，校验未通过");
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
