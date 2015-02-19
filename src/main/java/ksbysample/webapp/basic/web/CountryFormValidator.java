package ksbysample.webapp.basic.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class CountryFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CountryForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CountryForm countryForm = (CountryForm)target;

        // Code と Code2 に同じ文字列を入力している場合はエラー
        if (StringUtils.equals(countryForm.getCode(), countryForm.getCode2())) {
            errors.rejectValue("code2", "countryForm.code2.equalCode");
        }

        // Name = "Japan" あるいは "日本" の場合、Continent に Asia 以外が選択されている場合はエラー
        if ((StringUtils.equals(countryForm.getName(), "Japan") || StringUtils.equals(countryForm.getName(), "日本"))
                && (!StringUtils.equals(countryForm.getContinent(), "Asia"))) {
            errors.rejectValue("continent", "countryForm.continent.notAsia");
        }

        // Continent = "Asia" の場合、Region は "Eastern Asia", "Middle East", "Southeast Asia", "Southern and Central Asia"
        // のいずれかでない場合はエラー
        if ((StringUtils.equals(countryForm.getContinent(), "Asia"))
                && (!Pattern.matches("^(Eastern Asia|Middle East|Southeast Asia|Southern and Central Asia)$", countryForm.getRegion()))) {
            errors.rejectValue("region", "countryForm.region.notAsiaPattern");
        }
    }

}
