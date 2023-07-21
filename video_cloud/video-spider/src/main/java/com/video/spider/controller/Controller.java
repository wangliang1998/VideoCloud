package com.video.spider.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.video.spider.service.catch_aqiyi;
import com.video.spider.task.CatchSchedule;
import com.video.spider.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
@CrossOrigin
public class Controller {

    @Autowired
    private CatchSchedule service;

    //获取所有解析接口
    @GetMapping("/spider/test")
    public Result getAllJieKou() throws SQLException, InterruptedException, JsonProcessingException {
        service.runAqiyi();
        return Result.succ(null);
    }

}
