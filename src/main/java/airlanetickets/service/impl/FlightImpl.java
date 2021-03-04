package airlanetickets.service.impl;

import airlanetickets.model.*;
import airlanetickets.model.exceptions.InvalidFlightIdException;
import airlanetickets.repository.*;
import airlanetickets.service.FlightService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FlightImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final AgencyRepository agencyRepository;

    private final AirplaneRepository airplaneRepository;

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;

    public FlightImpl(FlightRepository flightRepository, AgencyRepository agencyRepository, AirplaneRepository airplaneRepository, OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.flightRepository = flightRepository;
        this.agencyRepository = agencyRepository;
        this.airplaneRepository = airplaneRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Page<Flight> findPaginated(int pageNo, int pageSize, String fromSearch, String toSearch, String deptTime) {
        this.deleteExpDateAndNoAvbSeats(this.flightRepository.findAll());

        Pageable pageable = PageRequest.of(pageNo-1,pageSize,Sort.by(Sort.Direction.ASC, "deparatureTime"));
        List<Flight> flightList = new ArrayList<>();

        if (fromSearch != null || toSearch != null || deptTime != null){
            System.out.println("IN IF");

            flightList =  this.listByFromAndToAndDeptTime(fromSearch,toSearch,deptTime,pageable);
            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > flightList.size() ? flightList.size() : (start+pageable.getPageSize());
            return new PageImpl<Flight>(flightList.subList(start,end),pageable,flightList.size());

        }

        flightList = this.flightRepository.findAll().stream().filter(f -> f.cantShow()).collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > flightList.size() ? flightList.size() : (start+pageable.getPageSize());

        return new PageImpl<Flight>(flightList.subList(start,end),pageable,flightList.size());
    }

    @Override
    public Flight findById(Long id){

        Flight flight = this.flightRepository.findById(id).orElseThrow(InvalidFlightIdException::new);

        return flight;
    }

    @Override
    public List<Flight> listAll() {
        this.deleteExpDateAndNoAvbSeats(this.flightRepository.findAll());
        return this.flightRepository.findAll();
    }

    @Override
    public Flight create(String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agencyId,
                         Long airplaneId, String duration, int price, int seats) {
        Agency agency = this.agencyRepository.findById(agencyId).orElseThrow();
        Airplane airplane = this.airplaneRepository.findById(airplaneId).orElseThrow();

        Flight flight = new Flight(departureFrom,departureTo,departureTime,arrivalTime,duration,price,seats,agency,airplane);

        return this.flightRepository.save(flight);
    }

    @Override
    public Flight update(Long id, String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agencyId,
                         Long airplaneId, String duration, int price, int seats) {
        Flight flight = this.findById(id);
        Agency agency = this.agencyRepository.findById(agencyId).orElseThrow();
        Airplane airplane = this.airplaneRepository.findById(airplaneId).orElseThrow();

        flight.setFromLocation(departureFrom);
        flight.setToLocation(departureTo);
        flight.setDeparatureTime(departureTime);
        flight.setArrival_time(arrivalTime);
        flight.setAgency(agency);
        flight.setAirplane(airplane);
        flight.setDuration(duration);
        flight.setPrice(price);
        flight.setTotal_seats(seats);

        return this.flightRepository.save(flight);

    }

    @Override
    public Flight updateSeats(Long id,int seats){
        Flight flight = this.findById(id);
        flight.setTotalSeats(1);

        return this.flightRepository.save(flight);
    }

    @Override
    public void deleteExpDateAndNoAvbSeats(List<Flight> flights) {
        List<Flight> flightFilter = flights.stream()
                .filter(f -> !f.cantShow() || f.getTotal_seats() == 0).collect(Collectors.toList());
        for (Flight flight : flightFilter){
            Order order = this.orderRepository.findByFlightId(flight.getId());
            Ticket ticket = this.ticketRepository.findByOrders(order);
            if (ticket != null && order != null) {
                this.ticketRepository.delete(ticket);
                this.orderRepository.delete(order);
                this.flightRepository.delete(flight);
            }
        }
    }

    @Override
    public Flight delete(Long id) {
        Flight flight = this.findById(id);
        Order order = this.orderRepository.findByFlightId(id);
        Ticket ticket = this.ticketRepository.findByOrders(order);
        if (ticket != null && order != null) {
            this.ticketRepository.delete(ticket);
            this.orderRepository.delete(order);
            this.flightRepository.delete(flight);
        }

        return flight;
    }

    @Override
    public List<Flight> listByFromAndToAndDeptTime(String fromSearch, String toSearch, String deptSearch,Pageable pageable) {
        this.deleteExpDateAndNoAvbSeats(this.flightRepository.findAll());

        String fromLike = "%" + fromSearch + "%";
        String toLike = "%" + toSearch + "%";
        String deptTime = "";

        if (!deptSearch.isEmpty()) {
            String[] parts = deptSearch.split("-");
            String day = parts[2];
            String month = parts[1];
            String year = parts[0];
            deptTime = day+"-"+month+"-"+year;
        }

        String deptLike = "%" + deptTime + "%";

       if (fromSearch != null && toSearch != null && deptSearch != null){
            return this.flightRepository.findAllByFromLocationLikeAndToLocationLikeAndDeparatureTimeLike(fromLike,toLike,deptLike);

        }else if(fromSearch != null && toSearch != null){
            return this.flightRepository.findAllByFromLocationLikeAndToLocationLike(fromLike,toLike);

        }else if(fromSearch != null && deptSearch != null){
            return this.flightRepository.findAllByFromLocationLikeAndDeparatureTimeLike(fromLike,deptLike);

        }else if(toSearch != null && deptSearch != null){
            return  this.flightRepository.findAllByToLocationLikeAndDeparatureTimeLike(fromLike,deptLike);

        }else if(fromSearch != null){
            return  this.flightRepository.findAllByFromLocationLike(fromLike);

        }else if(toSearch != null){
            return  this.flightRepository.findAllByToLocationLike(toLike);

        }else if(deptSearch != null){
            return this.flightRepository.findAllByDeparatureTimeLike(deptLike);
        }else{
            return  this.listAll();
        }
    }
}
