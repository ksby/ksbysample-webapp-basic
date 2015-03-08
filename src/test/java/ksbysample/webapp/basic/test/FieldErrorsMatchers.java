package ksbysample.webapp.basic.test;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class FieldErrorsMatchers extends ModelResultMatchers {

    private FieldErrorsMatchers() {
    }

    public static FieldErrorsMatchers fieldErrors() {
        return new FieldErrorsMatchers();
    }

    public ResultMatcher hasFieldError(String name, String fieldName, String error) {
        return mvcResult -> {
            BindingResult bindingResult = getBindingResult(mvcResult.getModelAndView(), name);
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors(fieldName);
            List<String> fieldErrorListAsCode = new ArrayList<>();
            for (FieldError fe : fieldErrorList) {
                fieldErrorListAsCode.add(fe.getCode());
            }
            assertThat("Expected error code '" + error + "'", fieldErrorListAsCode, hasItem(error));
        };
    }

    private BindingResult getBindingResult(ModelAndView mav, String name) {
        BindingResult bindingResult = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
        assertTrue("No BindingResult for attribute: " + name, bindingResult != null);
        return bindingResult;
    }

}
