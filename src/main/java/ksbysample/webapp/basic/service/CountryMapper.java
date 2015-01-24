package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.web.CountryListForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryMapper {

    public long selectCountryCount(@Param("countryListForm") CountryListForm countryListForm);

    public List<Country> selectCountry(@Param("countryListForm") CountryListForm countryListForm);

}
