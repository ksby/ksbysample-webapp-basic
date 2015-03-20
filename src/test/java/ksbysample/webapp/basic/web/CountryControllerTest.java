package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.Application;
import ksbysample.webapp.basic.domain.Country;
import ksbysample.webapp.basic.service.CountryRepository;
import ksbysample.webapp.basic.test.SecurityMockMvcResource;
import ksbysample.webapp.basic.test.TestDataResource;
import ksbysample.webapp.basic.test.TestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.yaml.snakeyaml.Yaml;

import static ksbysample.webapp.basic.test.CustomFlashAttributeResultMatchers.flashEx;
import static ksbysample.webapp.basic.test.FieldErrorsMatchers.fieldErrors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CountryControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 非認証時の場合 {

        @Rule
        @Autowired
        public SecurityMockMvcResource secmvc;

        @Test
        public void 入力画面の表示はログイン画面へリダイレクトされる() throws Exception {
            // 非認証時はログイン画面にリダイレクトされることを確認する
            secmvc.nonauth.perform(get("/country/input"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        public void 確認画面の表示は403エラーが返る() throws Exception {
            // 非認証時は403エラー(Forbidden)が返ることを確認する
            secmvc.nonauth.perform(post("/country/confirm"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void 確認画面の戻るボタン押下時の処理は403エラーが返る() throws Exception {
            // 非認証時は403エラー(Forbidden)が返ることを確認する
            secmvc.nonauth.perform(post("/country/input/back"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void 確認画面の登録ボタン押下時の処理は403エラーが返る() throws Exception {
            // 非認証時は403エラー(Forbidden)が返ることを確認する
            secmvc.nonauth.perform(post("/country/update"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void 完了画面の表示は403エラーが返る() throws Exception {
            // 非認証時は403エラー(Forbidden)が返ることを確認する
            secmvc.nonauth.perform(post("/country/complete"))
                    .andExpect(status().isForbidden());
        }

    }

    public static class 認証時の場合 {

        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = Application.class)
        @WebAppConfiguration
        public static class CSRFトークンがない場合のテスト {

            @Rule
            @Autowired
            public SecurityMockMvcResource secmvc;

            // テストデータ
            private CountryForm countryFormSuccess
                    = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_success.yaml"));

            @Test
            public void 確認画面はCSRFトークンがない場合403エラーが返る() throws Exception {
                // データは問題なくても CSRFトークンがない場合には403エラー(Forbidden)が返ることを確認する
                secmvc.auth.perform(TestHelper.postForm("/country/confirm", this.countryFormSuccess))
                        .andExpect(status().isForbidden());
            }

            @Test
            public void 登録ボタン押下時の処理はCSRFトークンがない場合403エラーが返る() throws Exception {
                // データは問題なくても CSRFトークンがない場合には403エラー(Forbidden)が返ることを確認する
                secmvc.auth.perform(TestHelper.postForm("/country/update", this.countryFormSuccess))
                        .andExpect(status().isForbidden());
            }

        }

        public static class 入力画面のテスト {

            @RunWith(SpringJUnit4ClassRunner.class)
            @SpringApplicationConfiguration(classes = Application.class)
            @WebAppConfiguration
            public static class DBを使用しない処理のテスト {

                @Rule
                @Autowired
                public SecurityMockMvcResource secmvc;

                // テストデータ
                private CountryForm countryFormEmpty
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_empty.yaml"));
                private CountryForm countryFormSizeDigitsCheck
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_sizedigitscheck.yaml"));
                private CountryForm countryFormValidateError1
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_validateerror1.yaml"));
                private CountryForm countryFormValidateError2
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_validateerror2.yaml"));

                @Test
                public void 入力画面を表示する() throws Exception {
                    // 認証時は登録画面(入力)が表示されることを確認する
                    secmvc.auth.perform(get("/country/input"))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType("text/html;charset=UTF-8"))
                            .andExpect(view().name("country/input"));
                }

                @Test
                public void データ未入力時には入力チェックエラーが発生する() throws Exception {
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
//                    // 発生しているfield errorを全て出力するには以下のようにする
//                    ModelAndView mav = result.getModelAndView();
//                    BindingResult br = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + "countryForm");
//                    List<FieldError> listFE = br.getFieldErrors();
//                    for (FieldError fe : listFE) {
//                        System.out.println("★★★ " + fe.getField() + " : " + fe.getCode() + " : " + fe.getDefaultMessage());
//                    }
                }

                @Test
                public void 文字数桁数オーバー時には入力チェックエラーが発生する() throws Exception {
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
                }

                @Test
                public void countryForm_continent_notAsiaの入力チェックエラーのテスト() throws Exception {
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
                }

                @Test
                public void countryForm_region_notAsiaPatternの入力チェックエラーのテスト() throws Exception {
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
                }

            }

        }

        public static class 確認画面のテスト {

            @RunWith(SpringJUnit4ClassRunner.class)
            @SpringApplicationConfiguration(classes = Application.class)
            @WebAppConfiguration
            public static class DBを使用しない処理のテスト {

                @Rule
                @Autowired
                public SecurityMockMvcResource secmvc;

                // テストデータ
                private CountryForm countryFormSuccess
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_success.yaml"));
                private CountryForm countryFormSizeDigitsCheck
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_sizedigitscheck.yaml"));

                @Test
                public void 確認画面を表示する() throws Exception {
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
                public void 戻るボタンを押すと入力画面へ戻る() throws Exception {
                    // 認証時はフォームのデータがFlashスコープにセットされて、登録画面(入力)へリダイレクトされることを確認する
                    secmvc.auth.perform(TestHelper.postForm("/country/input/back", this.countryFormSuccess)
                                    .with(csrf())
                    )
                            .andExpect(status().isFound())
                            .andExpect(redirectedUrl("/country/input"))
                            .andExpect(flashEx().attributes("countryForm", this.countryFormSuccess));
                }

                @Test
                public void データに入力チェックエラーがある場合400エラーが返る() throws Exception {
                    // CSRFトークンがあってもデータに問題がある場合には400エラー(Bad Request)が返ることを確認する
                    secmvc.auth.perform(TestHelper.postForm("/country/update", this.countryFormSizeDigitsCheck)
                                    .with(csrf())
                    )
                            .andExpect(status().isBadRequest());
                }

            }

            @RunWith(SpringJUnit4ClassRunner.class)
            @SpringApplicationConfiguration(classes = Application.class)
            @WebAppConfiguration
            public static class DBを使用する処理のテスト {

                @Rule
                @Autowired
                public TestDataResource testDataResource;

                @Rule
                @Autowired
                public SecurityMockMvcResource secmvc;

                @Autowired
                private CountryRepository countryRepository;

                // テストデータ
                private CountryForm countryFormSuccess
                        = (CountryForm) new Yaml().load(getClass().getResourceAsStream("countryForm_success.yaml"));

                @Test
                public void データが問題なければDBにデータが登録され完了画面へリダイレクトされる() throws Exception {
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

            }

        }

        public static class 完了画面のテスト {

            @RunWith(SpringJUnit4ClassRunner.class)
            @SpringApplicationConfiguration(classes = Application.class)
            @WebAppConfiguration
            public static class DBを使用しない処理のテスト {

                @Rule
                @Autowired
                public SecurityMockMvcResource secmvc;

                @Test
                public void 完了画面を表示する() throws Exception {
                    secmvc.auth.perform(get("/country/complete"))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType("text/html;charset=UTF-8"))
                            .andExpect(view().name("country/complete"))
                            .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(完了)"));
                }

            }

        }

    }

}