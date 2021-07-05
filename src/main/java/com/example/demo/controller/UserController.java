package com.example.demo.controller;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")//使得下面的映射都在/user下面
public class UserController {
    //创建一个线程安全的Map
    //static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<Integer, User>());
    private final IUserService userService;

    @Autowired //自动注入匹配
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    //必须localhost:8080/user/访问
    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public ApiResponse createUser(@ModelAttribute User user) {
        Boolean result = userService.save(user);
        if (!result) {
            return ApiResponse.ofMessage("保存失败!");
        }
        return ApiResponse.ofSuccess(result);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ApiResponse getUser(@ModelAttribute User user) {
        return ApiResponse.ofSuccess(userService.select(user));

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse getUser(@PathVariable Long id) {
        return ApiResponse.ofSuccess(userService.getById(id));

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse updateUser(@PathVariable Long id, @ModelAttribute User user) {
        Boolean result = userService.update(id, user);
        if (!result) {
            return ApiResponse.ofMessage("更新失败!");
        }
        return ApiResponse.ofSuccess(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse deleteUser(@PathVariable Long id) {
        Boolean result = userService.delete(id);
        if (!result) {
            return ApiResponse.ofMessage("删除失败!");
        }
        return ApiResponse.ofSuccess(result);
    }

    @DeleteMapping("/user")//也是在json里面传入
    public ApiResponse deleteUser(@ModelAttribute User user) {
        Boolean result = userService.delete(user);
        if (!result) {
            return ApiResponse.ofMessage("删除失败!");
        }
        return ApiResponse.ofSuccess(result);
    }


}
