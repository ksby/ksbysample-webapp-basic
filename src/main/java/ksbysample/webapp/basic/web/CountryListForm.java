package ksbysample.webapp.basic.web;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CountryListForm {

    @Size(max = 3, message = "{error.size.max}")
    private String code;

    @Size(max = 52, message = "{error.size.max}")
    private String name;

    @Pattern(regexp = "^(|Asia|Europe|North America|Africa|Oceania|Antarctica|South America)$", message = "{countryListForm.continent.pattern}")
    private String continent;

    @Size(max = 45, message = "{error.size.max}")
    private String localName;

    private int page;

    private int size;

}
