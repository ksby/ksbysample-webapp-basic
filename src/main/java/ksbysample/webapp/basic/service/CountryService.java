package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.web.CountryListForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryMapper countryMapper;

    public Page<Country> findCountry(CountryListForm countryListForm, Pageable pageable) {
        long count = countryMapper.selectCountryCount(countryListForm);

        List<Country> countryList = Collections.emptyList();
        if (count > 0) {
            countryList = countryMapper.selectCountry(countryListForm);
        }

        Page<Country> page = new PageImpl<Country>(countryList, pageable, count);
        return page;
    }

}
