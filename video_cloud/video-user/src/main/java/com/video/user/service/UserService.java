package com.video.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.video.user.mapper.UserMapper;
import com.video.user.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //用户登录
    public Account accountLogin(String username, String password){
        return userMapper.accountLogin(username,password);
    }

    //分页获取所有用户
    public Map<String,Object> getAllUsers(int page){
        PageHelper.startPage(page,30);
        List<Account> accounts = userMapper.getAllUsers();
        PageInfo<Account> pageInfo = new PageInfo<>(accounts);
        Map<String,Object> map = new HashMap<>();
        map.put("users",accounts);
        map.put("count",pageInfo.getTotal());
        return map;
    }

    //搜索用户
    public Account getAccount(String username){
        return userMapper.getAccount(username);
    }

    //用户删除
    public int delete(String username){
        return userMapper.delete(username);
    }

    //用户信息修改
    public int update(Account account){
        return userMapper.update(account);
    }

    //增加用户
    public int adduser(Account account){
        return userMapper.adduser(account);
    }

}
