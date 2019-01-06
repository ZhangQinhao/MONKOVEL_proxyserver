package com.zhangqinhao.monkovelproxy.controller.web;

import com.zhangqinhao.monkovelproxy.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制器
 */
@Controller
public class HomeController extends BaseController {

    /**
     * 根路径重定向为主页
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/")
    public ModelAndView toPageHome(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse){
        ModelAndView mav = new ModelAndView("redirect:/home");
        return mav;
    }

    /**
     * 主页页面
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/home")
    public ModelAndView pageHome(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("home/index");
        return mav;
    }
}
