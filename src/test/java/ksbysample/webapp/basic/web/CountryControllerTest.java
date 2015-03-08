package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.Application;
import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.service.CountryRepository;
import ksbysample.webapp.basic.test.SecurityMockMvcResource;
import ksbysample.webapp.basic.test.TestDataResource;
import ksbysample.webapp.basic.test.TestHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

import static ksbysample.webapp.basic.test.CustomFlashAttributeResultMatchers.flashEx;
import static ksbysample.webapp.basic.test.FieldErrorsMatchers.fieldErrors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CountryControllerTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Rule
    @Autowired
    public SecurityMockMvcResource secmvc;

    @Autowired
    private CountryRepository countryRepository;

    // テストデータ
    private CountryForm countryFormSuccess = new CountryForm(
            StringUtils.repeat("x", 3)          // code
            , StringUtils.repeat("x", 52)       // name
            , "Asia"                            // continent
            , "Eastern Asia"                    // region
            , new BigDecimal("12345678.00")     // surfaceArea
            , 2147483647L                       // population
            , StringUtils.repeat("x", 45)       // localName
            , StringUtils.repeat("x", 45)       // governmentForm
            , StringUtils.repeat("x", 2)        // code2
    );
    private CountryForm countryFormEmpty = new CountryForm(
            ""                                  // code
            , ""                                // name
            , ""                                // continent
            , ""                                // region
            , null                              // surfaceArea
            , null                              // population
            , ""                                // localName
            , ""                                // governmentForm
            , ""                                // code2
    );
    private CountryForm countryFormSizeDigitsCheck = new CountryForm(
            StringUtils.repeat("x", 4)          // code
            , StringUtils.repeat("x", 53)       // name
            , "test"                            // continent
            , StringUtils.repeat("x", 27)       // region
            , new BigDecimal("12345678.000")    // surfaceArea
            , 123456789012L                     // population
            , StringUtils.repeat("x", 46)       // localName
            , StringUtils.repeat("x", 46)       // governmentForm
            , StringUtils.repeat("x", 3)        // code2
    );
    private CountryForm countryFormValidateError1 = new CountryForm(
            "JP2"                               // code
            , "Japan"                           // name
            , "Europe"                          // continent
            , "Eastern Asia"                    // region
            , new BigDecimal("1")               // surfaceArea
            , 2L                                // population
            , "Nippon"                          // localName
            , "test"                            // governmentForm
            , "JP"                              // code2
    );
    private CountryForm countryFormValidateError2 = new CountryForm(
            "JP2"                               // code
            , "Japan"                           // name
            , "Asia"                            // continent
            , "Southern Europe"                 // region
            , new BigDecimal("1")               // surfaceArea
            , 2L                                // population
            , "Nippon"                          // localName
            , "test"                            // governmentForm
            , "JP"                              // code2
    );

    @Test
    public void testInput() throws Exception {
        // 非認証時はログイン画面にリダイレクトされることを確認する
        secmvc.nonauth.perform(get("/country/input"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/"));

        // 認証時は登録画面(入力)が表示されることを確認する
        secmvc.auth.perform(get("/country/input"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"));
    }

    @Test
    public void testInputBack() throws Exception {
        // 非認証時は403エラー(Forbidden)が返ることを確認する
        secmvc.nonauth.perform(post("/country/input/back"))
                .andExpect(status().isForbidden());

        // 認証時はフォームのデータがFlashスコープにセットされて、登録画面(入力)へリダイレクトされることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/input/back", this.countryFormSuccess)
                        .with(csrf())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/country/input"))
                .andExpect(flashEx().attributes("countryForm", this.countryFormSuccess));
    }

    @Test
    public void testConfirm() throws Exception {
        // 非認証時は403エラー(Forbidden)が返ることを確認する
        secmvc.nonauth.perform(post("/country/confirm"))
                .andExpect(status().isForbidden());

        // 認証時のテスト
        // NotBlank/NotNullの入力チェックのテスト
        MvcResult result = secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormEmpty)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(入力)"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(11))
                .andExpect(fieldErrors().hasFieldError("countryForm", "code", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "name", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "continent", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "continent", "Pattern"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "region", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "surfaceArea", "NotNull"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "population", "NotNull"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "localName", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "governmentForm", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "code2", "NotBlank"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "code2", "countryForm.code2.equalCode"))
                .andReturn();
        // 発生しているfield errorを全て出力するには以下のようにする
        ModelAndView mav = result.getModelAndView();
        BindingResult br = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + "countryForm");
        List<FieldError> listFE = br.getFieldErrors();
        for (FieldError fe : listFE) {
            System.out.println("★★★ " + fe.getField() + " : " + fe.getCode() + " : " + fe.getDefaultMessage());
        }

        // Size/Pattern/Digitsの入力チェックのテスト
        secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormSizeDigitsCheck)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(9))
                .andExpect(fieldErrors().hasFieldError("countryForm", "code", "Size"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "name", "Size"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "continent", "Pattern"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "region", "Size"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "surfaceArea", "Digits"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "population", "Digits"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "localName", "Size"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "governmentForm", "Size"))
                .andExpect(fieldErrors().hasFieldError("countryForm", "code2", "Size"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(入力)"));

        // countryForm.continent.notAsia の入力チェックのテスト
        secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormValidateError1)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(fieldErrors().hasFieldError("countryForm", "continent", "countryForm.continent.notAsia"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(入力)"));

        // countryForm.region.notAsiaPattern の入力チェックのテスト
        secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormValidateError2)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(fieldErrors().hasFieldError("countryForm", "region", "countryForm.region.notAsiaPattern"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(入力)"));

        // データは問題なくても CSRFトークンがない場合には403エラー(Forbidden)が返ることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormSuccess))
                .andExpect(status().isForbidden());

        // データが問題なくCSRFトークンもある場合には登録画面(確認)が表示されることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormSuccess)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().errorCount(0))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/confirm"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(確認)"));
    }

    @Test
    public void testUpdate() throws Exception {
        // 非認証時は403エラー(Forbidden)が返ることを確認する
        secmvc.nonauth.perform(post("/country/update"))
                .andExpect(status().isForbidden());

        // 認証時のテスト
        // データは問題なくても CSRFトークンがない場合には403エラー(Forbidden)が返ることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/update", this.countryFormSuccess))
                .andExpect(status().isForbidden());

        // CSRFトークンがあってもデータに問題がある場合には400エラー(Bad Request)が返ることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/update", this.countryFormSizeDigitsCheck)
                        .with(csrf())
        )
                .andExpect(status().isBadRequest());

        // データが問題なくCSRFトークンもある場合にはDBにデータが登録され、登録画面(完了)へリダイレクトされることを確認する
        secmvc.auth.perform(TestHelper.postForm("/country/update", this.countryFormSuccess)
                        .with(csrf())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/country/complete"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().errorCount(0));

        Country country = countryRepository.findOne("xxx");
        assertThat(country, is(notNullValue()));
        TestHelper.assertEntityByForm(country, this.countryFormSuccess);
    }

    @Test
    public void testComplete() throws Exception {
        // 非認証時は403エラー(Forbidden)が返ることを確認する
        secmvc.nonauth.perform(post("/country/complete"))
                .andExpect(status().isForbidden());

        // 認証時は登録画面(完了)が表示されることを確認する
        secmvc.auth.perform(post("/country/complete")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/complete"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(完了)"));
    }

}