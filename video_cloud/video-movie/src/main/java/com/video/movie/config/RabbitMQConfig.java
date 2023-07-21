package com.video.movie.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 创建队列
    @Bean
    public Queue queue(){
        return new Queue("spider_movie");
    }

    // 创建Direct交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("directExchange");
    }

    // 绑定队列到交换机，并指定routingKey为"movie_key"
    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("movie_key");
    }
}
