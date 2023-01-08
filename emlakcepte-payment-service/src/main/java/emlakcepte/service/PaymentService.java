package emlakcepte.service;

import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.model.Payment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfiguration rabbitMQConfiguration;

    public PaymentService(RabbitTemplate rabbitTemplate, RabbitMQConfiguration rabbitMQConfiguration)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfiguration = rabbitMQConfiguration;
    }

    public void processPayment(Payment payment, String cardNo, Integer amount)
    {
        int i = 0;
        while (i < amount) {
            rabbitTemplate.convertAndSend(rabbitMQConfiguration.getQueueName(), payment);
            i++;
        }


    }

}