package com.example.demo.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    //表示可以用 localhost:8080/ 或者是localhost:8080直接访问
    @GetMapping(value = {"", "/", "/index"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (ObjectUtil.isNull(user)) {
            //重定向到后端连接
            view.setViewName("redirect:/user/login");
        } else {
            view.setViewName("page/index");
            view.addObject(user);
        }
        return view;
    }

    @PostMapping("/user/login")
    public ModelAndView login(@ModelAttribute User user, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject(user);
        view.setViewName("redirect:/index");
        request.getSession().setAttribute("user", user);
        return view;
    }

    @GetMapping("/user/login")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }

    /***freemarker模板区域***/
    @GetMapping(value = {"/fm", "/fmindex"})
    public ModelAndView fmIndex(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        User user = (User) request.getSession().getAttribute("fmUser");
        if (ObjectUtil.isNull(user)) {
            view.setViewName("redirect:/fm/login");
        } else {
            view.setViewName("page/fm_index");
            view.addObject("fmUser", user);
        }
        return view;
    }

    @PostMapping("/fm/login")
    public ModelAndView fmLogin(@ModelAttribute User user, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject(user);
        view.setViewName("redirect:/fm");
        request.getSession().setAttribute("fmUser", user);
        return view;
    }

    @GetMapping("/fm/login")
    public ModelAndView fmLogin() {
        return new ModelAndView("page/fm_login");
    }


    /*enjoy区域*/
    @GetMapping(value = {"/enjoy", "/enjoy/index"})
    public ModelAndView enjoyIndex(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        User user = (User) request.getSession().getAttribute("enjoyUser");
        if (ObjectUtil.isNull(user)) {
            //若是空 则重定向到登录
            view.setViewName("redirect:/enjoy/login");
        } else {
            //若有值 这跳转到主页
            view.setViewName("enjoy_index");
            view.addObject("enjoyUser", user);
        }
        return view;
    }

    @GetMapping("/enjoy/login")
    public ModelAndView enjoyLogin() {
        return new ModelAndView("enjoy_login");
    }


    @PostMapping("/enjoy/userLogin")
    public ModelAndView enjoyLogin(@ModelAttribute User user, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject(user);
        view.setViewName("redirect:/enjoy/index");
        request.getSession().setAttribute("enjoyUser", user);
        return view;
    }


}
