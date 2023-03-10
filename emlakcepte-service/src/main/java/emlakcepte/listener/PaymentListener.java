package emlakcepte.listener;

import emlakcepte.model.Payment;
import emlakcepte.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PaymentListener {

    @Autowired
    private UserService userService;

    @RabbitListener(queues = "emlakcepte.payment")
    public void paymentListener(Payment payment) {
        System.out.println("User Extension something happened here");
        userService.extendPackageExpiration(payment);
        System.out.printf("%s extension done!!!", payment.getUserId() );
    }

}
