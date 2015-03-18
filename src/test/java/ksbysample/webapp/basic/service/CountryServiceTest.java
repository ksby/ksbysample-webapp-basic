package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.Application;
import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.test.TestDataResource;
import ksbysample.webapp.basic.test.TestHelper;
import ksbysample.webapp.basic.web.CountryForm;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CountryServiceTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    // テストデータ
    private CountryForm countryFormSave
            = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_save.yaml"));
    private CountryForm countryFormCodeNull
            = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_codenull.yaml"));

    @Test
    public void データを１件保存する() throws Exception {
        countryService.save(countryFormSave);

        // 保存されていることを確認する
        Country country = countryRepository.findOne(countryFormSave.getCode());
        assertThat(country, is(notNullValue()));
        TestHelper.assertEntityByForm(country, countryFormSave);
    }

    @Test(expected = Exception.class)
    public void Codeが空の場合Exceptionが発生する() throws Exception {
        countryService.save(countryFormCodeNull);
    }

}
