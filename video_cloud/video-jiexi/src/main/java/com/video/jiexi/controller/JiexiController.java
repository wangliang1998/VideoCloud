package com.video.jiexi.controller;

import com.video.jiexi.pojo.Jiexi;
import com.video.jiexi.service.JieXiService;
import com.video.jiexi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class JiexiController {

    @Autowired
    private JieXiService jieXiService;

    //获取所有解析接口
    @GetMapping("/jx/front/getAllJieKou")
    public Result getAllJieKou(){
        return Result.succ(jieXiService.getAllJieXi());
    }

    //接口类别信息
    @GetMapping("/jx/back/select")
    public Result getJieKous(){
        List<Jiexi> list = jieXiService.getAllJieXi();
        return Result.succ(list);
    }

    //接口增加
    @RequestMapping("/jx/back/add")
    public Result addJieKou(@RequestParam("name") String name, @RequestParam("url") String url, @RequestParam("rank") int rank){
        Jiexi jiexi = new Jiexi();
        jiexi.setName(name);
        jiexi.setRank(rank);
        jiexi.setUrl(url);
        jiexi.setId(-1);
        jieXiService.addJieXi(jiexi);
        return Result.succ(null);
    }

    //接口删除
    @GetMapping("/jx/back/delete/{id}")
    public Result deleteJieKou(@PathVariable("id") int id){
        jieXiService.deleteJieXi(id);
        return Result.succ(null);
    }

    //接口信息修改
    @RequestMapping("/jx/back/update")
    public Result updateJieKou(@RequestParam("id") int id,@RequestParam("name") String name,
                               @RequestParam("url") String url,@RequestParam("rank") int rank
    ){
        Jiexi jiexi = new Jiexi();
        jiexi.setName(name);
        jiexi.setRank(rank);
        jiexi.setUrl(url);
        jiexi.setId(id);
        jieXiService.updateJieXi(jiexi);
        return Result.succ(null);
    }




}
