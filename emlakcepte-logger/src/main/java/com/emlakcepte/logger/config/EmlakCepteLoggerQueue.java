package com.emlakcepte.logger.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class EmlakCepteLoggerQueue {


    private String queueName="emlakcepte.logger";

    private String exchange = "emlakcepte.logger";

    @Bean
    public Queue loggerQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    public DirectExchange loggerExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue loggerQueue, DirectExchange loggerExchange) {
        return BindingBuilder.bind(loggerQueue).to(loggerExchange).with("");
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

}


