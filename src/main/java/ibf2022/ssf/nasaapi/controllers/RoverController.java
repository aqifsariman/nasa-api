package ibf2022.ssf.nasaapi.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.ssf.nasaapi.models.Rover;
import ibf2022.ssf.nasaapi.services.RoverService;

@Controller
@RequestMapping("/mars-rovers")
public class RoverController {

    @Autowired
    private RoverService roverSvc;

    @GetMapping
    public String getRover(Model model) {
        Optional<List<Rover>> opt = roverSvc.getRoverPhoto();
        List<Rover> rover = opt.get();
        model.addAttribute("rover", rover);
        return "mars-rovers";
    }

}
