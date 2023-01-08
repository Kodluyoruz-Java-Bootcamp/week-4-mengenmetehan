package emlakcepte.controller;

import emlakcepte.model.Payment;
import emlakcepte.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{cardNo}/{amount}")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment, @PathVariable String cardNo, @PathVariable Integer amount) {
        paymentService.processPayment(payment,cardNo,amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
