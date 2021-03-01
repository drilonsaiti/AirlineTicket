package airlanetickets.web;


import airlanetickets.model.*;
import airlanetickets.model.enumerations.ClassesType;
import airlanetickets.service.FlightService;
import airlanetickets.service.ReservationService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    private final FlightService flightService;

    Long idFlight;

    public ReservationController(ReservationService reservationService, FlightService flightService) {
        this.reservationService = reservationService;
        this.flightService = flightService;
    }

    @PostMapping("/reservation/setId")
    public String setId(@Param("idFlight") String idFlight,HttpServletRequest req){
        String ides = req.getParameter("idFlight");
        req.getSession().setAttribute("idFlight",ides);

        this.idFlight = Long.valueOf(idFlight);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation")
    public String getReservation(Model model,HttpServletRequest req){
        Flight flight = this.flightService.findById(this.idFlight);
        List<ClassesType> classesTypes = Arrays.stream(ClassesType.values()).sequential().collect(Collectors.toList());
        Reservation reservation = new Reservation();

        model.addAttribute("reservation",reservation);
        model.addAttribute("flight",flight);
        model.addAttribute("classesTypes",classesTypes);

        return "reservation";
    }

    @PostMapping("/reservation")
    public String createReservation(HttpServletRequest req,
                                    @RequestParam String nameOfCust,
                                    @RequestParam String surOfCust,
                                    @RequestParam String numofPass,
                                    @RequestParam String countryCode,
                                    @RequestParam String numberOfPhone,
                                    @RequestParam ClassesType type,
                                    @RequestParam(required = false) String bagging){

        Long id  = this.reservationService.create(nameOfCust,surOfCust,numofPass,countryCode+numberOfPhone,type).getId();
        req.getSession().setAttribute("idReservation",id);
        req.getSession().setAttribute("bagging",bagging);
        return "redirect:/payment";
    }


}
