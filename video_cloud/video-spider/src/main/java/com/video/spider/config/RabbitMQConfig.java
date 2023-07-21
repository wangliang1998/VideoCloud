package com.video.spider.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    // 创建队列
    @Bean
    public Queue queue(){
        return new Queue("spider_movie",true);   // 将队列设置为持久化
    }

    // 创建Direct交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("directExchange",true,false);// 将交换机设置为持久化
    }

    // 绑定队列到交换机，并指定routingKey为"movie_key"
    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("movie_key");
    }


    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(confirmCallback());
    }

    private RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            if (ack) {
                // 消息发送成功
                System.out.println("Message successfully sent with correlationId: " + correlationData.getId());
            } else {
                // 消息发送失败，可以进行重试操作
                System.out.println("Failed to send message with correlationId: " + correlationData.getId() + ", cause: " + cause);
            }
        };
    }
}
