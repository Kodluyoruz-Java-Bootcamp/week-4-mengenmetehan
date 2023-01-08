package com.emlakcepte.logger.service;

import com.emlakcepte.logger.config.RabbitMQLoggerConfiguration;
import com.emlakcepte.logger.model.LoggerEntity;
import com.emlakcepte.logger.repository.ILogRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
    private ILogRepository m_logRepository;
    private final RabbitTemplate m_rabbitTemplate;
    private final RabbitMQLoggerConfiguration m_rabbitMQConfiguration;

    public LoggerService(ILogRepository logRepository, RabbitTemplate rabbitTemplate, RabbitMQLoggerConfiguration rabbitMQConfiguration)
    {
        m_logRepository = logRepository;
        m_rabbitTemplate = rabbitTemplate;
        m_rabbitMQConfiguration = rabbitMQConfiguration;
    }

    public LoggerEntity create (LoggerEntity logger)
    {
        System.out.println(logger.getId() + logger.getMessage() + logger.getPriority());
        m_rabbitTemplate.convertAndSend(m_rabbitMQConfiguration.getQueueName(), logger);
        return m_logRepository.save(logger);
    }
}
