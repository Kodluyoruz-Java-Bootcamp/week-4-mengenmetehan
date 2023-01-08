package com.emlakcepte.logger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class RabbitMQLoggerConfiguration {

	private String queueName = "emlakcepte.logger";
	private String exchange = "emlakcepte.logger-exchange";

	@Bean
	public Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	@Bean
	public Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("");
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
				.modules(new JavaTimeModule())
				.dateFormat(new StdDateFormat())
				.build();
		Jackson2JsonMessageConverter jackson2JsonMessageConverter
				= new Jackson2JsonMessageConverter(objectMapper);
		return jackson2JsonMessageConverter;
		//return new Jackson2JsonMessageConverter();
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
