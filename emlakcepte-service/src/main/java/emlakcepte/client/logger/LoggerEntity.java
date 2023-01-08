package emlakcepte.client.logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LoggerEntity {

    private Long id;
    private String priority;
    private String message;

    public LoggerEntity()
    {

    }
    public LoggerEntity(Long id, String priority, String message)
    {
        this.id = id;
        this.priority = priority;
        this.message = message;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}


