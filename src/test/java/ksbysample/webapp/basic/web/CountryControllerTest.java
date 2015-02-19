package ksbysample.webapp.basic.web;

import ksbysample.webapp.basic.Application;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CountryControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testInput() throws Exception {
        this.mvc.perform(get("/country/input")).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/input"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(入力)"));
    }

    @Ignore
    @Test
    public void testConfirm() throws Exception {
        this.mvc.perform(get("/country/confirm")).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/confirm"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(確認)"));
    }

    @Ignore
    @Test
    public void testUpdate() throws Exception {
        this.mvc.perform(get("/country/update")).andExpect(status().isFound())
                .andExpect(redirectedUrl("/country/complete"));
    }

    @Test
    public void testComplete() throws Exception {
        this.mvc.perform(get("/country/complete")).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("country/complete"))
                .andExpect(xpath("/html/head/title").string("Countryデータ登録画面(完了)"));
    }

}