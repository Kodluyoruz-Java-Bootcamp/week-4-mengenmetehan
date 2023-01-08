package emlakcepte.client.logger;


import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoggerServiceClient {

    public LoggerEntity create(Long id, String priority, String message)
    {
        LoggerEntity logger = new LoggerEntity(id, priority, message);

        String url = "http://localhost:50500/api/logger/save";
        RestTemplate template = new RestTemplate();

        HttpEntity<LoggerEntity> request = new HttpEntity<>(logger);
        return template.postForObject(url, request, LoggerEntity.class);
    }
}
