package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.Application;
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
import org.yaml.snakeyaml.Yaml;

import static ksbysample.webapp.basic.test.CustomModelResultMatchers.modelEx;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CountryListControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 非認証時の場合 {

        @Rule
        @Autowired
        public SecurityMockMvcResource secmvc;

        @Test
        public void ログイン画面にリダイレクトされる() throws Exception {
            secmvc.nonauth.perform(get("/countryList"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

    }

    public static class 認証時の場合 {

        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = Application.class)
        @WebAppConfiguration
        public static class 画面初期表示の場合 {

            @Rule
            @Autowired
            public SecurityMockMvcResource secmvc;

            @Test
            public void 検索一覧画面が表示される() throws Exception {
                secmvc.auth.perform(get("/countryList"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"));
            }

        }

        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = Application.class)
        @WebAppConfiguration
        public static class 検索条件は何も入力しないで検索する場合 {

            @Rule
            @Autowired
            public TestDataResource testDataResource;

            @Rule
            @Autowired
            public SecurityMockMvcResource secmvc;

            // テストデータ
            private CountryListForm countryListFormEmpty
                    = (CountryListForm) new Yaml().load(getClass().getResourceAsStream("countryListForm_empty.yaml"));

            @Test
            public void 検索ボタンをクリックすると１ページ目が表示される() throws Exception {
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormEmpty)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(0)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(48)))
                        .andExpect(modelEx().property("page.numberOfElements", is(5)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(false)));
            }

            @Test
            public void 検索条件はそのままで２ページ目へ() throws Exception {
                this.countryListFormEmpty.setPage(1);
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormEmpty)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(1)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(48)))
                        .andExpect(modelEx().property("page.numberOfElements", is(5)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(false)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(false)));
            }

            @Test
            public void 検索条件はそのままで最終ページへ() throws Exception {
                this.countryListFormEmpty.setPage(47);
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormEmpty)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(47)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(48)))
                        .andExpect(modelEx().property("page.numberOfElements", is(4)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(43)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(false)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(true)));
            }

        }

        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = Application.class)
        @WebAppConfiguration
        public static class 検索条件を入力して検索する場合 {

            @Rule
            @Autowired
            public TestDataResource testDataResource;

            @Rule
            @Autowired
            public SecurityMockMvcResource secmvc;

            // テストデータ
            private CountryListForm countryListFormCode
                    = (CountryListForm) new Yaml().load(getClass().getResourceAsStream("countryListForm_code.yaml"));
            private CountryListForm countryListFormName
                    = (CountryListForm) new Yaml().load(getClass().getResourceAsStream("countryListForm_name.yaml"));
            private CountryListForm countryListFormContinent
                    = (CountryListForm) new Yaml().load(getClass().getResourceAsStream("countryListForm_continent.yaml"));
            private CountryListForm countryListFormLocalName
                    = (CountryListForm) new Yaml().load(getClass().getResourceAsStream("countryListForm_localName.yaml"));

            @Test
            public void Codeのみ入力して検索ボタンをクリックすると１件ヒットする() throws Exception {
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormCode)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(0)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(1)))
                        .andExpect(modelEx().property("page.numberOfElements", is(1)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(true)));
            }

            @Test
            public void Nameのみ入力して検索ボタンをクリックすると８件ヒットする() throws Exception {
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormName)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(model().attributeExists("page"))
                        .andExpect(model().attributeExists("ph"))
                        .andExpect(modelEx().property("page.number", is(1)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(2)))
                        .andExpect(modelEx().property("page.numberOfElements", is(3)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(false)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(true)));
            }

            @Test
            public void Continentのみ入力して検索ボタンをクリックすると２件ヒットする() throws Exception {
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormContinent)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(5)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(6)))
                        .andExpect(modelEx().property("page.numberOfElements", is(3)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(1)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(false)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(true)));
            }

            @Test
            public void LocalNameのみ入力して検索ボタンをクリックすると２件ヒットする() throws Exception {
                secmvc.auth.perform(TestHelper.postForm("/countryList", this.countryListFormLocalName)
                                .with(csrf())
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/html;charset=UTF-8"))
                        .andExpect(view().name("countryList"))
                        .andExpect(xpath("/html/head/title").string("検索/一覧画面"))
                        .andExpect(modelEx().property("page.number", is(0)))
                        .andExpect(modelEx().property("page.size", is(5)))
                        .andExpect(modelEx().property("page.totalPages", is(1)))
                        .andExpect(modelEx().property("page.numberOfElements", is(2)))
                        .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                        .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                        .andExpect(modelEx().property("ph.hiddenNext", is(true)));
            }

        }

    }

}