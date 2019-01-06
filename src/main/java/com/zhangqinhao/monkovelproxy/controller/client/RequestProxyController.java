package com.zhangqinhao.monkovelproxy.controller.client;

import com.zhangqinhao.monkovelproxy.bean.BaseResponse;
import com.zhangqinhao.monkovelproxy.controller.BaseController;
import com.zhangqinhao.monkovelproxy.util.HttpKit;
import com.zhangqinhao.monkovelproxy.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求代理
 */
@Controller
public class RequestProxyController extends BaseController {

    /**
     * 为了拦截xxx/client请求
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/")
    public ModelAndView toClient(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse){
        ModelAndView mav = new ModelAndView("redirect:/client");
        return mav;
    }

    @RequestMapping(value = "/client" ,produces = "text/html; charset=utf-8")
    @ResponseBody
    public String request(@RequestParam(value = "proxyUrl", required = false) String proxyUrl, javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) {
        if (StringUtil.isEmpty(proxyUrl)) {
            return new BaseResponse(-1, "缺少proxyUrl参数").toJson();
        }
        try {
            proxyUrl = URLDecoder.decode(proxyUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result;
        Enumeration<String> headNames = httpServletRequest.getHeaderNames();
        Map<String, String> header = new HashMap<String, String>();
        if (headNames != null) {
            while (headNames.hasMoreElements()) {
                String headName = headNames.nextElement();
                header.put(headName, httpServletRequest.getHeader(headName));
            }
        }
        header.remove("host");
        header.remove("accept-encoding");
        try {
            if (httpServletRequest.getMethod().equalsIgnoreCase("get")) {
                result = HttpKit.getByAutoDecode(proxyUrl, new HashMap<String, String>(), header);
            } else {
                StringBuilder bodyBuilder = new StringBuilder();
                try {
                    BufferedReader br = httpServletRequest.getReader();
                    String str;
                    while ((str = br.readLine()) != null) {
                        bodyBuilder.append(str);
                    }
                }catch(Exception ex){
                }
                result = HttpKit.postByAutoDecode(proxyUrl, new HashMap<String, String>(), bodyBuilder.toString(),header);
            }
            return result;
        }catch (Exception e){
            return null;
        }
    }
}
