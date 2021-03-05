package airlanetickets.repository;

import airlanetickets.model.Order;
import airlanetickets.model.Ticket;
import airlanetickets.model.enumerations.TicketStatus;
import airlanetickets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    Optional<Ticket> findByUserAndStatus(User user, TicketStatus status);

    void deleteById(Long id);

    Ticket findTicketByOrders(Order order);

    void deleteByOrders(Order order);
}
