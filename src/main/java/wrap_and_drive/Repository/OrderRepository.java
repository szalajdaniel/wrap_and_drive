package wrap_and_drive.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wrap_and_drive.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}