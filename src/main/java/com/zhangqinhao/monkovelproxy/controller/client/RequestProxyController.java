package com.zhangqinhao.monkovelproxy.controller.client;

import com.zhangqinhao.monkovelproxy.ApplicationListenerImpl;
import com.zhangqinhao.monkovelproxy.CheckRequestKeyManager;
import com.zhangqinhao.monkovelproxy.annot.Clear;
import com.zhangqinhao.monkovelproxy.bean.BaseResponse;
import com.zhangqinhao.monkovelproxy.controller.BaseController;
import com.zhangqinhao.monkovelproxy.interceptor.RequestCheckInterceptor;
import com.zhangqinhao.monkovelproxy.util.HttpKit;
import com.zhangqinhao.monkovelproxy.util.StringUtil;
import com.zhangqinhao.monkovelproxy.util.aes.AESUtil;
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
            return e.getMessage();
        }
    }

    @RequestMapping(value = "aesencode" ,produces = "text/html; charset=utf-8")
    @Clear(RequestCheckInterceptor.class)
    @ResponseBody
    public String aesEncode(@RequestParam(value = "key") String key,@RequestParam(value = "content") String content) {
        try {
            try{
                content = URLDecoder.decode(content,"utf-8");
            }catch (Exception e){
            }
            return AESUtil.aesEncode(content,key);
        }catch (Exception e){
            return "加密失败  "+e.toString();
        }
    }

    @RequestMapping(value = "aesdecode" ,produces = "text/html; charset=utf-8")
    @Clear(RequestCheckInterceptor.class)
    @ResponseBody
    public String aesDecode(@RequestParam(value = "key") String key,@RequestParam(value = "content") String content) {
        try {
            try{
                content = URLDecoder.decode(content,"utf-8");
            }catch (Exception e){
            }
            return AESUtil.aesDecode(content,key);
        }catch (Exception e){
            return "解密失败  "+e.toString();
        }
    }

    /**
     * 在线修改是否开启代理请求校验
     * @param key
     * @param check
     * @return
     */
    @RequestMapping(value = "request_state" ,produces = "text/html; charset=utf-8")
    @Clear(RequestCheckInterceptor.class)
    @ResponseBody
    public String changeRequestCheckState(@RequestParam(value = "aesKey") String key,@RequestParam(value = "check") boolean check){
        if(!ApplicationListenerImpl.getConfigStr("AESkey").equals(key)){
            return "操作验证不通过";
        }
        ApplicationListenerImpl.setConfigBoolean("checkState",check);
        return "状态修改成功";
    }

    /**
     * 清除历史校验key
     * @param key
     * @return
     */
    @RequestMapping(value = "clean_historykey" ,produces = "text/html; charset=utf-8")
    @Clear(RequestCheckInterceptor.class)
    @ResponseBody
    public String cleanHistoryKey(@RequestParam(value = "aesKey") String key){
        if(!ApplicationListenerImpl.getConfigStr("AESkey").equals(key)){
            return "操作验证不通过";
        }
        CheckRequestKeyManager.getInstance().clean();
        return "清除成功";
    }

    /**
     * 获取历史校验key缓存数量
     * @param key
     * @return
     */
    @RequestMapping(value = "count_historykey" ,produces = "text/html; charset=utf-8")
    @Clear(RequestCheckInterceptor.class)
    @ResponseBody
    public String countHistoryKey(@RequestParam(value = "aesKey") String key){
        if(!ApplicationListenerImpl.getConfigStr("AESkey").equals(key)){
            return "操作验证不通过";
        }
        return "目前缓存共有 "+CheckRequestKeyManager.getInstance().getSize()+" 条历史校验key";
    }
}
