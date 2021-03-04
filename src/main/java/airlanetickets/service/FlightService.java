package airlanetickets.service;

import airlanetickets.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {

    List<Flight> listAll();

    Flight findById(Long id);

    Flight create(String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agency,
                         Long airplane, String duration, int price, int seats);

    Flight update(Long id,String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agency,
                         Long airplaneId, String duration, int price, int seats);

    Flight delete(Long id);

    Page<Flight> listByFromAndToAndDeptTime(String fromSearch, String toSearch, String deptSearch, Pageable pageable);

     Flight updateSeats(Long id,int seats);

     void deleteExpDateAndNoAvbSeats(List<Flight> flights);
     Page<Flight> findPaginated(int pageNo, int pageSize, String fromSearch, String toSearch, String deptTime);


    }
