package com.video.movie.service;


import com.video.movie.mapper.KindMapper;
import com.video.movie.pojo.Kinds;
import com.video.movie.pojo.movieKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KindService {

    @Autowired
    private KindMapper mapper;

    /**
     * 得到所有分类电影
     */
    public List<Kinds> getAllKinds(){
        return mapper.getAllKinds();
    }

    /***
     * 增加电影分类
     */
    public int addKind(int id,String name){
        return mapper.addKind(id,name);
    }

    /**
     * 电影类别删除
     */
    public int deleteKind(int id){
        return mapper.deleteKind(id);
    }

    /**
     * 电影类别修改
     */
    public int updateKind(int id,String name){
        return mapper.updateKind(id,name);
    }

    /**
     * 根据id删除电影类别
     * @param id
     */
    public int deleteById(int id){
        return mapper.deleteById(id);
    }

    /**
     * 增加电影时，批量添加类别
     * 将电影类别添加到movie_kind表，用于分类查看
     */
    public int batchAddKinds(List<movieKind> lists){
        return mapper.batchAddKinds(lists);
    }


}
