package airlanetickets.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/","/home"})
public class HomeController {

    @GetMapping("home")
    public String getHomePage(Model model) {
        return "home";

    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(Model model){
        return "access_denied";

    }
}
