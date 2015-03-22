package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.web.CountryForm;
import ksbysample.webapp.basic.web.CountryListForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static ksbysample.webapp.basic.service.CountrySpecifications.*;

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

        return new PageImpl<>(countryList, pageable, count);
    }

    public Page<Country> findCountryJpa(CountryListForm countryListForm, Pageable pageable) {
        return countryRepository.findAll(
                Specifications
                        .where(codeContains(countryListForm.getCode()))
                        .and(nameContains(countryListForm.getName()))
                        .and(continentContains(countryListForm.getContinent()))
                        .and(localNameContains(countryListForm.getLocalName()))
                , pageable);
    }

    public void save(CountryForm countryForm) {
        Country country = new Country();
        BeanUtils.copyProperties(countryForm, country);
        countryRepository.save(country);
    }

}
