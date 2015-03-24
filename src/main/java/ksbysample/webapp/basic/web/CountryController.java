package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.config.Constant;
import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.exception.InvalidRequestException;
import ksbysample.webapp.basic.service.CountryRepository;
import ksbysample.webapp.basic.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Controller
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private Constant constant;

    @Autowired
    private CountryFormValidator countryFormValidator;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(countryFormValidator);
    }

    @RequestMapping("/input")
    public String input(CountryForm countryForm
            , Model model) {

        model.addAttribute("continentList", constant.CONTINENT_LIST);
        return "country/input";
    }

    @RequestMapping("/input/back")
    public String inputBack(CountryForm countryForm
            , RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("countryForm", countryForm);
        return "redirect:/country/input";
    }

    @RequestMapping("/confirm")
    public String confirm(@Validated CountryForm countryForm
            , BindingResult bindingResult
            , Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("continentList", constant.CONTINENT_LIST);
            return "country/input";
        }

        // code に入力された文字列をキーに持つデータが登録されている場合にはエラーにする
        Country country = countryRepository.findOne(countryForm.getCode());
        if (country != null) {
            bindingResult.reject("countryForm.global.duplicate");
            model.addAttribute("continentList", constant.CONTINENT_LIST);
            return "country/input";
        }

        return "country/confirm";
    }

    @RequestMapping("/update")
    public String update(@Validated CountryForm countryForm
            , BindingResult bindingResult
            , Locale locale
            , Model model
            , HttpServletResponse response) throws IOException, InvalidRequestException {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(messageSource.getMessage("common.invalidRequestException.message", new Object[]{bindingResult.toString()}, locale));
        }

        countryService.save(countryForm);
        return "redirect:/country/complete";
    }

    @RequestMapping("/complete")
    public String complete() {
        return "country/complete";
    }

}
