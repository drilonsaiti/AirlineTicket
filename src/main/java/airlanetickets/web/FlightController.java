package airlanetickets.web;


import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.service.AgencyService;
import airlanetickets.service.AirplaneService;
import airlanetickets.service.FlightService;
import airlanetickets.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;

    private final AgencyService agencyService;

    private final AirplaneService airplaneService;

    private final ReservationService reservationService;


    public FlightController(FlightService flightService, AgencyService agencyService, AirplaneService airplaneService, ReservationService reservationService) {
        this.flightService = flightService;
        this.agencyService = agencyService;
        this.airplaneService = airplaneService;
        this.reservationService = reservationService;
    }

    @GetMapping("/flights")
    public String getFlights(@RequestParam(required = false) String fromSearch,
                             @RequestParam(required = false) String toSearch,
                             @RequestParam(required = false) String deptSearch,
                             Model model){

        List<Flight> flights;

        if (fromSearch == null && toSearch == null && deptSearch == null){
            flights = this.flightService.listAll();
        }else{
            flights = this.flightService.listByFromAndToAndDeptTime(fromSearch, toSearch, deptSearch);
        }

        model.addAttribute("flights",flights);

        return "flights";
    }

    @GetMapping("/flights/add")
    public String addFlight(Model model ){
        List<Agency> agencies = this.agencyService.listAll();
        List<Airplane> airplanes = this.airplaneService.listAll();


        model.addAttribute("agencies",agencies);
        model.addAttribute("airplanes",airplanes);

        return "add-flights";
    }

    @PostMapping("/flights")
    public String create(@RequestParam String departureFrom,
                         @RequestParam String departureTo,
                         @RequestParam String departureTime,
                         @RequestParam String arrivalTime,
                         @RequestParam Long agency,
                         @RequestParam Long airplane,
                         @RequestParam String duration,
                         @RequestParam int price,
                         @RequestParam int seats){
        this.flightService.create(departureFrom, departureTo, departureTime, arrivalTime, agency, airplane, duration, price, seats);
        return "redirect:/flights";

    }
    @PostMapping("/flights/reservation")
    public String reservation(){
        return "redirect:/reservation";
    }
    @PostMapping("/flights/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam String departureFrom,
                         @RequestParam String departureTo,
                         @RequestParam String departureTime,
                         @RequestParam String arrivalTime,
                         @RequestParam Long agency,
                         @RequestParam Long airplane,
                         @RequestParam String duration,
                         @RequestParam int price,
                         @RequestParam int seats){
        this.flightService.update(id,departureFrom, departureTo, departureTime, arrivalTime, agency, airplane, duration, price, seats);
        return "redirect:/flights";

    }

    @GetMapping("/flights/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {
        Flight flight = this.flightService.findById(id);

        List<Agency> agencies = this.agencyService.listAll();
        List<Airplane> airplanes = this.airplaneService.listAll();

        model.addAttribute("flight",flight);
        model.addAttribute("agencies",agencies);
        model.addAttribute("airplanes",airplanes);
        return "add-flights";
    }

    @PostMapping("/flights/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.flightService.delete(id);
        return "redirect:/flights";
    }


}
