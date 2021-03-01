package airlanetickets.service.impl;

import airlanetickets.model.Flight;
import airlanetickets.model.Order;
import airlanetickets.repository.OrderRepository;
import airlanetickets.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional
    public Order save(Order order) {
        System.out.println("ORDER SIZE " + this.orderRepository.findAll().stream().filter(i -> i.getFlight().getFromLocation().equals(order.getFlight().getFromLocation()) && i.getFlight()
                .getDeparatureTime().equals(order.getFlight().getDeparatureTime()) &&
                i.getReservation().getNumberOfPassport().equals(order.getReservation().getNumberOfPassport()))
                .collect(Collectors.toList()).size());


        if (this.orderRepository.findAll().stream().filter(i -> i.getFlight().getFromLocation().equals(order.getFlight().getFromLocation()) && i.getFlight()
        .getDeparatureTime().equals(order.getFlight().getDeparatureTime()) &&
                i.getReservation().getNumberOfPassport().equals(order.getReservation().getNumberOfPassport()))
                .collect(Collectors.toList()).size() > 0){
            this.orderRepository.delete(order);
        }
        return this.orderRepository.save(order);
    }

    @Override

    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElseThrow();
    }


    @Override
    @Transactional
    public Order delete(Long id) {
        Order order = this.findById(id);

        this.orderRepository.delete(order);

        return order;
    }

}
