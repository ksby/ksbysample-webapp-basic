package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.web.CountryListForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryMapper countryMapper;

    public List<Country> findCountry(CountryListForm countryListForm) {
        return countryMapper.selectCountry(countryListForm);
    }

}
