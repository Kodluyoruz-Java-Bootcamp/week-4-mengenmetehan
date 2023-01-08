package emlakcepte.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

public class EmlakCepteRealtyQueue {
    @Value("${emlakcepte.rabbitmq.realtyQueue}")
    private String queueName;

    @Bean
    public Queue queue()
    {
        return new Queue(queueName,false);
    }
    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {

        return new MappingJackson2MessageConverter();
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


    @Bean
    public Binding binding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with("");
    }
}
