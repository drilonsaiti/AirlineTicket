package airlanetickets.web;


import airlanetickets.model.Flight;
import airlanetickets.model.Order;
import airlanetickets.model.Reservation;
import airlanetickets.model.Ticket;
import airlanetickets.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TicketController {

    private final TicketService ticketService;

    private final FlightService flightService;

    private final ReservationService reservationService;

    private final PaymentSercvice paymentSercvice;

    private final OrderService orderService;


    public TicketController(TicketService ticketService, FlightService flightService,
                            ReservationService reservationService, PaymentSercvice paymentSercvice, OrderService orderService) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.paymentSercvice = paymentSercvice;
        this.orderService = orderService;
    }

    @GetMapping("/ticket-cart")
    public String getTickerPage(HttpServletRequest req, Model model) throws Exception {
        String username = req.getRemoteUser();

        Ticket ticket = this.ticketService.getActiveTicketCart(username);

        String str = (String) req.getSession().getAttribute("idFlight") == null ? "NULL" : "NOT NULL";


        if(str.equals("NOT NULL")) {
            Long idFlight = Long.valueOf((String) req.getSession().getAttribute("idFlight"));


            Long idReservation = (Long) req.getSession().getAttribute("idReservation") != null ? (Long) req.getSession().getAttribute("idReservation") : -1;
            Reservation reservation = new Reservation();
            Flight flight = this.flightService.findById(idFlight);
            if (idReservation != -1) {
                reservation = this.reservationService.findById(idReservation);
            }


            double price = flight.getFinalPrice(reservation.getClassesType());
            //model.addAttribute("price", price);
        }else{
            //model.addAttribute("price", 0);
            System.out.println("ITS NULL");
        }

        model.addAttribute("orders",this.ticketService.listAllOrderInShoppingCart(ticket.getId()));
        model.addAttribute("ticket",ticket);

        return "ticket-cart";

    }



    @GetMapping("/ticket-cart/{id}/download")
    public String getDownloadPage(@PathVariable Long id,HttpServletRequest req,Model model){
        String username = req.getRemoteUser();
        Ticket ticket = this.ticketService.getActiveTicketCart(username);
        int ides = Math.toIntExact(id);

        Order order = this.orderService.findById(id);

        model.addAttribute("order",order);
        model.addAttribute("ticket",ticket);

        return "download";
    }

    @PostMapping("/ticket-cart/{id}/canceled")
    public String canceled(@PathVariable Long id,HttpServletRequest req){

        this.orderService.delete(id);
        return "redirect:/ticket-cart";
    }

}
