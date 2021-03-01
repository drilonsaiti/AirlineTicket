package airlanetickets.service;

import airlanetickets.model.Flight;

import java.util.List;

public interface FlightService {

    List<Flight> listAll();

    Flight findById(Long id);

    Flight create(String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agency,
                         Long airplane, String duration, int price, int seats);

    Flight update(Long id,String departureFrom, String departureTo, String departureTime, String arrivalTime, Long agency,
                         Long airplaneId, String duration, int price, int seats);

    Flight delete(Long id);

    List<Flight> listByFromAndToAndDeptTime(String fromSearch,String toSearch,String deptSearch);

     Flight updateSeats(Long id,int seats);


    }
