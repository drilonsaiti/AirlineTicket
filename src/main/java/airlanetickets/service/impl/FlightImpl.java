package airlanetickets.service.impl;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.model.exceptions.InvalidFlightIdException;
import airlanetickets.repository.AgencyRepository;
import airlanetickets.repository.AirplaneRepository;
import airlanetickets.repository.FlightRepository;
import airlanetickets.service.FlightService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FlightImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final AgencyRepository agencyRepository;

    private final AirplaneRepository airplaneRepository;

    public FlightImpl(FlightRepository flightRepository, AgencyRepository agencyRepository, AirplaneRepository airplaneRepository) {
        this.flightRepository = flightRepository;
        this.agencyRepository = agencyRepository;
        this.airplaneRepository = airplaneRepository;
    }

    @Override
    public Flight findById(Long id){

        Flight flight = this.flightRepository.findById(id).orElseThrow(InvalidFlightIdException::new);

        return flight;
    }

    @Override
    public List<Flight> listAll() {
        return this.flightRepository.findAll().stream().filter(f -> f.cantShow())
                .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
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
    public Flight delete(Long id) {
        Flight flight = this.findById(id);

        this.flightRepository.delete(flight);

        return flight;
    }

    @Override
    public List<Flight> listByFromAndToAndDeptTime(String fromSearch, String toSearch, String deptSearch) {
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
            return this.flightRepository.findAllByFromLocationLikeAndToLocationLike(fromLike,toLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else if(fromSearch != null && deptSearch != null){
            return this.flightRepository.findAllByFromLocationLikeAndDeparatureTimeLike(fromLike,deptLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else if(toSearch != null && deptSearch != null){
            return this.flightRepository.findAllByToLocationLikeAndDeparatureTimeLike(fromLike,deptLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else if(fromSearch != null){
            return this.flightRepository.findAllByFromLocationLike(fromLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else if(toSearch != null){
            return this.flightRepository.findAllByToLocationLike(toLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else if(deptSearch != null){
            return this.flightRepository.findAllByDeparatureTimeLike(deptLike)
                    .stream().filter(f -> f.cantShow())
                    .sorted(Comparator.comparing(Flight::getDeparatureTime)).collect(Collectors.toList());
        }else{
            return this.flightRepository.findAll();
        }
    }
}
