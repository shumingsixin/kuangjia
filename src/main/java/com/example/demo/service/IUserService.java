package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

/**
 * 用户服务接口层
 */
public interface IUserService {

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    Boolean save(User user);

    /**
     * 获取用户列表
     *
     * @param user
     * @return
     */
    List<User> select(User user);

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    User getById(Long id);

    /**
     * 更新用户
     *
     * @param id
     * @param user
     * @return
     */
    Boolean update(Long id, User user);

    /**
     * 更新用户组
     *
     * @param param
     * @param value
     * @return
     */
    Boolean Update(User param, User value);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 删除用户组
     *
     * @param param
     * @return
     */
    Boolean delete(User param);

}
