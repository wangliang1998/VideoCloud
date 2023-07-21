package com.video.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.movie.pojo.Kinds;
import com.video.movie.pojo.Movie;
import com.video.movie.pojo.movieKind;
import com.video.movie.service.KindService;
import com.video.movie.service.MovieService;
import com.video.movie.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class movieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private KindService kindService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /***
     * 电影首页数据显示
     * @param page
     * @return
     */
    @GetMapping("/movie/front/{type}/{page}")
    public Result PageSplit(@PathVariable("page") int page, @PathVariable("type") String type) throws JsonProcessingException {
        String keys = "select:movie:"+type+":"+page;
        String result = redisTemplate.opsForValue().get(keys);
        if(result!=null){
            Map<String, Object> map = new ObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {});
            return Result.succ(map);
        }
        Map<String, Object> map = null;
        if ("default".equals(type)) {
            map = movieService.getAll_Movies_Date(page);
        } else if ("hot".equals(type)) {
            map = movieService.getAll_Movies_Score(page);
        } else if ("new".equals(type)) {
            map = movieService.getAll_Movies_Date(page);
        } else if (type.contains("kind")) {
            int kind = Integer.parseInt(type.split("_")[1]);
           map = movieService.get_kind_Movies(kind,page);
        } else if (type.contains("area")) {
            int aid = Integer.parseInt(type.split("_")[1]);
            map = movieService.get_Area_Movies(aid,page);
        } else if (type.contains("language")) {
            int lid = Integer.parseInt(type.split("_")[1]);
            map = movieService.get_Language_Movies(lid,page);
        }
        String json = new ObjectMapper().writeValueAsString(map);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);
        return Result.succ(map);
    }

    //获取电影分类
    @PostMapping("/movie/getKinds")
    public Result getMovieKinds(){
        List<Kinds> kinds = null;
        kinds = kindService.getAllKinds();
        return Result.succ(kinds);
    }

    //获取电影表主键的下一个id
    @PostMapping("/movie/getNextId")
    public Result getMovieNextId(){
        int id = movieService.get_auto_increase()+1;
        return Result.succ(id);
    }

    //电影搜索
    @RequestMapping ("/movie/front/search/{page}")
    public Result search(@RequestParam("search_name") String name, @PathVariable("page") int page) throws JsonProcessingException {

        String keys = "select:movie:search:" + name + ":" + page;
        String result = redisTemplate.opsForValue().get(keys);
        if (result != null) {
            Map<String, Object> map = new ObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {
            });
            return Result.succ(map);
        }

        Map<String,Object> map = movieService.search_Movies(name,page);

        String json = new ObjectMapper().writeValueAsString(map);
        redisTemplate.opsForValue().set(keys,json,30, TimeUnit.MINUTES);
        return Result.succ(map);
    }

    //电影详情
    @GetMapping("/movie/front/information/{id}")
    public Result getInformation(@PathVariable("id") int id) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        Movie movie = movieService.getMoviesById(id);
        map.put("movie",movie);
        List<String> kinds= movieService.getMovieKinds(id);
        String ss="";
        for(int i=0;i<kinds.size();i++)
            ss=ss + kinds.get(i).toString();
        map.put("kind",ss);

        return Result.succ(map);
    }

    //电影类别信息
    @GetMapping("/movie/back/kind/select")
    public Result getKinds(){
        Map<String,Object> map = new HashMap<>();
        List<Kinds> list = kindService.getAllKinds();
        map.put("kinds",list);
        map.put("count",list.size());
        return Result.succ(map);
    }

    //电影类别增加
    @RequestMapping("/movie/back/kind/add")
    public Result addKind(@RequestParam("id") int id,@RequestParam("name") String name
    ){
        kindService.addKind(id,name);
        return Result.succ(null);
    }

    //电影类别删除
    @GetMapping("/movie/back/kind/delete/{id}")
    public Result deleteKind(@PathVariable("id") int id){
        kindService.deleteKind(id);
        return Result.succ(null);
    }

    //电影类别信息修改
    @GetMapping("/movie/back/kind/update")
    public Result updateKind(@RequestParam("id") int id,@RequestParam("name") String name){
        kindService.updateKind(id,name);
        return Result.succ(null);
    }

    //电影查重
    @RequestMapping("/movie/back/upload/find/")
    public Result movieExists(@RequestParam("name") String name){
        boolean result = movieService.isNameExist(name);
        return Result.succ(result);
    }

    //电影查重：供爬虫服务使用
    @PostMapping("/movie/name/exist")
    public Result movieExists2(@RequestParam("name") String name){
        boolean result = movieService.isNameExist(name);
        return Result.succ(result);
    }

    //电影上传
    @RequestMapping("/movie/back/upload/do")
    @Transactional
    public Result upload(
            @RequestParam("name") String name,@RequestParam(value = "kind",required = false) String kind,
            @RequestParam(value = "actors",required = false,defaultValue = "") String actors,@RequestParam(value = "director",required = false) String director,
            @RequestParam("picture_url") String picture_url,@RequestParam(value = "movie_url",required = false) String movie_url,
            @RequestParam(value = "jinx_url",required = false) String jinx_url,@RequestParam(value = "area",required = false) String area,
            @RequestParam(value = "language",required = false) String language,@RequestParam(value = "score",required = false,defaultValue = "0") String score,
            @RequestParam(value = "time",required = false) String time,@RequestParam(value = "information",required = false) String information
    )  {
        int id = movieService.get_auto_increase();
        Movie movie =  new Movie(id, name, actors, information, picture_url, movie_url,
                jinx_url, area, Float.parseFloat(score), (int) Float.parseFloat(score) * 100, language, time, director);

        //同时添加到最新电影和原始电影
        movieService.addMovieSource(movie);
        //添加电影分类
        List<movieKind> movieKinds = new ArrayList<>();
        for (String ss:kind.split(",")) {
            movieKinds.add(new movieKind(id, Integer.parseInt(ss)));
        }
        kindService.batchAddKinds(movieKinds);
        return Result.succ(null);
    }

    /**
     * 电影模糊查询
     */
    @RequestMapping("/movie/back/search")
    public Result searchMovie(@RequestParam("name") String name,@RequestParam("page") int page){
        Map<String,Object> map = new HashMap<>();
        map =  movieService.search_Movies(name,page);
        return Result.succ(map);
    }

    /**
     * 根据电影id删除电影
     * 流程：
     *   设置了触发器，只要删除movies表即可，其他表会一并删除
     *   1.删除movies表
     *   2.删除movie_kind表
     *
     */
    @GetMapping("/movie/back/delete")
    @Transactional
    public Result deleteMovie(@RequestParam("id") int id){
        //删除电影表
        movieService.deleteMovieById(id);
        //批量删除删除movie_kind表
        movieService.deleteMovieKindById(id);
        return Result.succ(null);
    }

    /**
     * 电影信息更改
     */
    @RequestMapping("/movie/back/update")
    public Result updateMovie(@RequestParam("id") int id,
                              @RequestParam("name") String name,
                              @RequestParam("picture_url") String picture_url,
                              @RequestParam("movie_url") String movie_url,
                              @RequestParam("jiexi_url") String jinx_url,
                              @RequestParam("score") String score,
                              @RequestParam("popular") String popular) throws JsonProcessingException {
        Movie movie = movieService.getMoviesById(id);
        movie.setName(name);
        movie.setPicture_url(picture_url);
        movie.setMovie_url(movie_url);
        movie.setJiexi_url(jinx_url);
        movie.setScore(Float.parseFloat(score));
        movie.setPopular(Integer.parseInt(popular));
        movieService.updateMovie(movie);
        return Result.succ(null);
    }



}
