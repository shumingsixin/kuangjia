package com.example.demo.controller;

import com.example.demo.dao.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")//使得下面的映射都在/user下面
public class UserController {
    //创建一个线程安全的Map
    static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<Integer, User>());

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Integer createUser(@ModelAttribute User user) {
        //将映射注入好的对象加入Map
        users.put(user.getId(), user);
        return user.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Integer id) {
        return users.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable Integer id, @ModelAttribute User user) {
        User u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.replace(id, u);
        return "success";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Integer id) {
        users.remove(id);
        return "success";
    }


}
