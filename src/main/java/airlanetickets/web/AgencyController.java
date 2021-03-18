package airlanetickets.web;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.service.AgencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AgencyController {

    private final AgencyService agencyService;

    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @GetMapping("/agencies")
    public String getAgency(Model model) {
        List<Agency> agencies = this.agencyService.listAll();

        model.addAttribute("agencies", agencies);
        model.addAttribute("title","Agencies");
        model.addAttribute("bodyContent","agencies");

        return "master-template";
    }

    @GetMapping("/agencies/add")
    public String addAgencies(Model model) {
        model.addAttribute("title","Add agency");
        model.addAttribute("bodyContent","add-agency");

        return "master-template";
    }

    @PostMapping("/agencies")
    public String create(@RequestParam String nameOfAgency,
                         @RequestParam String cityOfAgency,
                         @RequestParam String countryOfAgency,
                         @RequestParam int yearOfCreated) {
        this.agencyService.create(nameOfAgency, cityOfAgency, countryOfAgency, yearOfCreated);
        return "redirect:/agencies";

    }


    @PostMapping("/agencies/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String nameOfAgency,
                         @RequestParam String cityOfAgency,
                         @RequestParam String countryOfAgency,
                         @RequestParam int yearOfCreated) {
        this.agencyService.update(id, nameOfAgency, cityOfAgency, countryOfAgency, yearOfCreated);
        return "redirect:/agencies";
    }

    @GetMapping("/agencies/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {
        Agency agency = this.agencyService.findById(id);

        model.addAttribute("agency",agency);
        model.addAttribute("title","Add agency");
        model.addAttribute("bodyContent","add-agency");


        return "master-template";
    }

    @PostMapping("/agencies/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.agencyService.delete(id);
        return "redirect:/agencies";
    }
}
