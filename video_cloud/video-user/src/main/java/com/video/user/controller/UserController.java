package com.video.user.controller;

import com.video.user.pojo.Account;
import com.video.user.service.UserService;
import com.video.user.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //用户登录
    @RequestMapping(value = "/user/front/login")
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password
                        , HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse
                        ){

        Account aa = userService.getAccount(username);
        if(aa==null){
            return Result.fail("用户名不存在");
        }
        if(!password.equals(aa.getPassword())){
            return Result.fail("密码不正确");
        }

        //redis记录用户登录状态
        String token = UUID.randomUUID().toString();
        //key为token，value为用户信息
        redisTemplate.opsForValue().set("token:"+token, aa.getAccess()+"",1, TimeUnit.DAYS);

        //用户名密码正确
        Map<String,Object> map = new HashMap<>();
        map.put("user",aa);
        map.put("token","token:"+token);
        return Result.succ(map);
    }

    //获取所有用户
    @GetMapping("/user/back/all/{page}")
    public Result getAllUsers(@PathVariable("page") int page){
        Map<String,Object> map = new HashMap<>();
        map = userService.getAllUsers(page);
        return Result.succ(map);
    }

    //用户搜索
    @RequestMapping("/user/back/search")
    public Result search(@RequestParam("username") String username){
        List<Account> list = new ArrayList<>();
        Account aa = userService.getAccount(username);
        list.add(aa);
        if(aa==null){
            return Result.fail("用户不存在");
        }
        return Result.succ(list);
    }

    //用户删除
    @RequestMapping("/user/back/delete/{username}")
    public Result delete(@PathVariable("username") String username){
        int count = userService.delete(username);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("用户删除失败");
    }

    //用户信息修改
    @RequestMapping("/user/back/update")
    public Result update(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("access") int access){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setAccess(access);
        int count = userService.update(account);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("用户信息修改失败");
    }
    //增加用户
    @RequestMapping("/user/back/add")
    public Result adduser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("access") int access){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setAccess(access);
        int count = userService.adduser(account);
        if(count>0)
            return Result.succ(null);
        else
            return Result.fail("增加用户失败");
    }



}
