package emlakcepte.client.payment;


import emlakcepte.model.Payment;
import emlakcepte.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceClient {
    private final UserRepository m_userRepository;

    public PaymentServiceClient(UserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    public Payment processPayment(Integer id, String no, Integer amount)
    {
        var user = m_userRepository.getReferenceById(id);

        Payment payment = new Payment(user.getId(), no);

        String url = "http://localhost:8095/payment/" + no + "/" + amount;
        RestTemplate template = new RestTemplate();

        HttpEntity<Payment> request = new HttpEntity<>(payment);
        return template.postForObject(url, request, Payment.class);
    }
}
