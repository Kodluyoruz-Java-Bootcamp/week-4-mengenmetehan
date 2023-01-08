package com.example.emlakcepteloggerfile.listener;

import com.example.emlakcepteloggerfile.model.LoggerEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.logging.*;

@Component
public class LoggerFileListener {

    @RabbitListener(queues = "${emlakcepte.rabbitmq.queue}")
    public void realtyListener(LoggerEntity loggerEntity) throws IOException
    {
        Logger logger = Logger.getLogger(LoggerEntity.class.getName());

            try
            {
                FileHandler fh = new FileHandler("logger_file.log",true);

                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // the following statement is used to log any messages
                logger.info(loggerEntity.toString());
            }
            catch (SecurityException | IOException e) {
                e.printStackTrace();
            }



        /*
           LogManager lgMan = LogManager.getLogManager();

        String LoggerName = Logger.GLOBAL_LOGGER_NAME;
        Logger Logr = lgMan.getLogger(LoggerName);
        Logr.setLevel(Level.ALL);

        //Create Handler and Set Formatter
        FileHandler fh = new FileHandler("logger_file.log",
                true);

        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.FINE);

        Logr.addHandler(fh);


        Logr.log(Level.INFO, loggerEntity.toString());
         */

    }
}
