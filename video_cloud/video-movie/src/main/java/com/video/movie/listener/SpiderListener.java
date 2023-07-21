package com.video.movie.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.video.movie.pojo.Spider;
import com.video.movie.service.KindService;
import com.video.movie.service.MovieService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = "spider_movie")
public class SpiderListener {

    @Autowired
    private MovieService movieService;

    @Autowired
    private KindService kindService;

    //保存爬虫数据
    @RabbitHandler
    @Transactional
    public void handleMessage(String msg,Message message, Channel channel) throws IOException {
       try {
           System.out.println("接收爬虫传来的数据");
           Spider data = new ObjectMapper().readValue(msg, Spider.class);
           if(data.getMovies().size()>0){
               movieService.batchAddMovies(data.getMovies());
               kindService.batchAddKinds(data.getMovie_kinds());
           }
           System.out.println("保存完成");
           channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
       } catch (IOException e) {
           e.printStackTrace();
           // 两个布尔值，若第二个设为false，则丢弃该消息；若设为true，则返回给队列
           channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
           System.out.println("消费失败 我此次将返回给队列");
       }

    }

}
