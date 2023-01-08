package com.emlakcepte.logger.controller;

import com.emlakcepte.logger.model.LoggerEntity;
import com.emlakcepte.logger.service.LoggerService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/logger/")
public class Controller {

    private final LoggerService m_loggerService;

    public Controller(LoggerService loggerService)
    {
        m_loggerService = loggerService;
    }

    @PostMapping("save")
    public LoggerEntity save (@RequestBody LoggerEntity logger)
    {
        return m_loggerService.create(logger);
    }
}
