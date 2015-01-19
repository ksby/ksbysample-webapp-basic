package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/countryList")
public class CountryListController {

    @Autowired
    private CountryService countryService;

    @RequestMapping
    public String index(@Validated CountryListForm countryListForm
            , BindingResult bindingResult
            , Model model) {

        if (bindingResult.hasErrors()) {
            return "countryList";
        }

        List<Country> countryList = countryService.findCountry(countryListForm);
        model.addAttribute("countryList", countryList);

        return "countryList";
    }

}
