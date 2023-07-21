package com.video.spider.clients;

import com.video.spider.pojo.Kinds;
import com.video.spider.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "movie-service")
public interface MovieClient {

    //电影查重：供爬虫服务使用
    @PostMapping("/movie/name/exist")
    public Result movieExists2(@RequestParam("name") String name);

    //获取电影分类
    @PostMapping("/movie/getKinds")
    public Result getMovieKinds();

    ////获取电影表主键的下一个id
    @PostMapping("/movie/getNextId")
    public Result getMovieNextId();

}
