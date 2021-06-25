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
    @GetMapping(value = {"","/","/index"})
    public ModelAndView index(HttpServletRequest request){
        ModelAndView view=new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if(ObjectUtil.isNull(user)){
            view.setViewName("redirect:/user/login");
        }else{
            view.setViewName("page/index");
            view.addObject(user);
        }
        return view;
    }

    @PostMapping("/user/login")
    public ModelAndView login(@ModelAttribute User user, HttpServletRequest request){
        ModelAndView view=new ModelAndView();
        view.addObject(user);
        view.setViewName("redirect:/index");
        request.getSession().setAttribute("user",user);
        return view;
    }

    @GetMapping("/user/login")
    public ModelAndView login(){
        return new ModelAndView("page/login");
    }
}
