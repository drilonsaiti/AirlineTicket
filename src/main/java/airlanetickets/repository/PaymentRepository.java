package airlanetickets.repository;

import airlanetickets.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
