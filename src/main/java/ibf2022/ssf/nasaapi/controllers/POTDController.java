package ibf2022.ssf.nasaapi.controllers;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.ssf.nasaapi.models.POTD;
import ibf2022.ssf.nasaapi.services.POTDService;

@Controller
@RequestMapping(path = "/apod")
public class POTDController {

    @Autowired
    private POTDService potdSvc;

    @GetMapping
    public String getPOTD(Model model) throws ParseException {
        Optional<POTD> opt = potdSvc.getPOTD();
        POTD potd = opt.get();
        model.addAttribute("potd", potd);
        return "photo-of-the-day";
    }

}
