package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.web.CountryForm;
import ksbysample.webapp.basic.web.CountryListForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountryRepository countryRepository;

    public Page<Country> findCountry(CountryListForm countryListForm, Pageable pageable) {
        long count = countryMapper.selectCountryCount(countryListForm);

        List<Country> countryList = Collections.emptyList();
        if (count > 0) {
            countryList = countryMapper.selectCountry(countryListForm);
        }

        return new PageImpl<Country>(countryList, pageable, count);
    }

    @Transactional
    public void save(CountryForm countryForm) {
        Country country = new Country();
        BeanUtils.copyProperties(countryForm, country);
        countryRepository.save(country);
    }

}
