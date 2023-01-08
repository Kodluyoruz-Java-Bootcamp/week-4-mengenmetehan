package com.emlakcepte.logger.repository;


import com.emlakcepte.logger.model.LoggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILogRepository extends JpaRepository<LoggerEntity, Long> {
}
