package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.demo.constant.Const;
import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务逻辑层
 */
@Service
public class UserServiceImpl implements IUserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Boolean save(User user) {
        String rawPass = user.getPassword();
        String salt = IdUtil.simpleUUID();
        String pwd = SecureUtil.md5(rawPass + Const.SALT_PREFIX + salt);
        user.setPassword(pwd);
        user.setSalt(salt);
        return userDao.insert(user) > 0;
    }

    @Override
    public List<User> select(User user) {
        return userDao.getUser(user);
    }

    @Override
    public User getById(Long id) {
        return userDao.getUser(id);
    }

    @Override
    public Boolean update(Long id, User user) {
        User exist = getById(id);
        if (StrUtil.isNotBlank(user.getPassword())) {
            String rawPass = user.getPassword();
            String salt = IdUtil.simpleUUID();
            String pwd = SecureUtil.md5(rawPass + Const.SALT_PREFIX + salt);
            user.setPassword(pwd);
            user.setSalt(salt);
        }
        //复制对象
        BeanUtil.copyProperties(user, exist, CopyOptions.create().setIgnoreNullValue(true));
        return userDao.updateUser(id, exist) > 0;
    }

    @Override
    public Boolean Update(User param, User value) {
        return userDao.updateUser(param, value) > 0;
    }

    @Override
    public Boolean delete(Long id) {
        return userDao.deleteUser(id) > 0;
    }

    @Override
    public Boolean delete(User param) {
        return userDao.deleteUser(param) > 0;
    }
}
