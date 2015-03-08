package ksbysample.webapp.basic.test;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class CustomFlashAttributeResultMatchers extends FlashAttributeResultMatchers {

    public static CustomFlashAttributeResultMatchers flashEx() {
        return new CustomFlashAttributeResultMatchers();
    }

    public ResultMatcher attributes(final String name, Object form) {
        return mvcResult -> {
            for (Field field : form.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                assertThat("Flash attribute", mvcResult.getFlashMap().get(name), hasProperty(field.getName(), is(field.get(form))));
            }
        };
    }

}
