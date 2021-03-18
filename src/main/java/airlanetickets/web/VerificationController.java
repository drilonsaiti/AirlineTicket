package airlanetickets.web;

import airlanetickets.model.Order;
import airlanetickets.model.Ticket;
import airlanetickets.service.OrderService;

import airlanetickets.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class VerificationController {

    private final TicketService ticketService;

    private final OrderService orderService;

    public VerificationController(TicketService ticketService, OrderService orderService) {
        this.ticketService = ticketService;
        this.orderService = orderService;
    }


    @GetMapping("/verification")
    public String getSearch(Model model){
        model.addAttribute("title","Verification");
        model.addAttribute("bodyContent","verificationSearch");

        return "master-template";

    }

    @GetMapping("/verification/search")
    public String getDownloadPage(@RequestParam(required = false) Long idOrder, HttpServletRequest req, Model model){
        String username = req.getRemoteUser();
        Ticket ticket = this.ticketService.getActiveTicketCart(username);

        if (idOrder != null ) {
            Order order = this.orderService.findById(idOrder);

            model.addAttribute("order", order);
            model.addAttribute("ticket", ticket);
        }else{
            Order order = new Order();

            model.addAttribute("order", order);
            model.addAttribute("ticket", ticket);
        }

        return "/verification";
    }
}
