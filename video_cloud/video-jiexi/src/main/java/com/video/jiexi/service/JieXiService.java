package com.video.jiexi.service;


import com.video.jiexi.mapper.JieXiMapper;
import com.video.jiexi.pojo.Jiexi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JieXiService {

    @Autowired
    private JieXiMapper mapper;

    //获取所有解析接口
    public List<Jiexi> getAllJieXi(){
        return mapper.getAllJieXi();
    }

    //增加解析接口
    public int addJieXi(Jiexi jiexi){
        return mapper.addJieXi(jiexi);
    }

    //修改解析接口
    public int updateJieXi(Jiexi jiexi){
        return mapper.updateJieXi(jiexi);
    }

    //删除解析接口
    public int deleteJieXi(int id){
        return mapper.deleteJieXi(id);
    }

}
