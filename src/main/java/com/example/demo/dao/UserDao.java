package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据持久层
 */
@Repository
public class UserDao extends BaseDao<User, Long> {

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    public Integer insert(User user) {
        return super.insert(user, true);
    }

    /**
     * 获取用户列表
     *
     * @param user
     * @return
     */
    public List<User> getUser(User user) {
        return super.findByExample(user);
    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    public User getUser(Long id) {
        return super.findById(id);
    }

    /**
     * 根据id更新用户
     *
     * @param id
     * @param user
     * @return
     */
    public Integer updateUser(Long id, User user) {
        return super.updateById(user, id, true);
    }

    /**
     * 根据用户条件批量更新用户
     *
     * @param param
     * @param value
     * @return
     */
    public Integer updateUser(User param, User value) {
        return super.updateByExample(param, value, true);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    public Integer deleteUser(Long id) {
        return super.deleteById(id);
    }

    /**
     * 根据用户条件删除用户
     *
     * @param user
     * @return
     */
    public Integer deleteUser(User user) {
        return super.deleteByExample(user);
    }
}
