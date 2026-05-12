package wrap_and_drive.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wrap_and_drive.Model.Order;
import wrap_and_drive.Repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> createOrder(Order order) {

        order.setTotalAmount(new BigDecimal("299.98"));
        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "success",
                "message", "Zamówienie zostało złożone pomyślnie!",
                "orderId", order.getId()
        ));
    }
}