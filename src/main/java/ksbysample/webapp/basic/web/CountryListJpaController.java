package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.helper.PagenationHelper;
import ksbysample.webapp.basic.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/countryListJpa")
public class CountryListJpaController {

    @Autowired
    private CountryService countryService;

    private static final int DEFAULT_PAGEABLE_SIZE = 5;

    @RequestMapping
    public String index(@Validated CountryListForm countryListForm
            , BindingResult bindingResult
            , @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable
            , Model model) {

        if (bindingResult.hasErrors()) {
            return "countryListJpa";
        }

        Page<Country> page = countryService.findCountryJpa(countryListForm, pageable);
        PagenationHelper ph = new PagenationHelper(page);

        model.addAttribute("page", page);
        model.addAttribute("ph", ph);

        return "countryListJpa";
    }

}
