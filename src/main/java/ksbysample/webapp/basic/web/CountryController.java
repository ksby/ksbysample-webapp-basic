package ksbysample.webapp.basic.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/country")
public class CountryController {

    @RequestMapping("/input")
    public String input() {
        return "country/input";
    }

    @RequestMapping("/confirm")
    public String confirm() {
        return "country/confirm";
    }

    @RequestMapping("/update")
    public String update() {
        return "redirect:/country/complete";
    }

    @RequestMapping("/complete")
    public String complete() {
        return "country/complete";
    }

}
