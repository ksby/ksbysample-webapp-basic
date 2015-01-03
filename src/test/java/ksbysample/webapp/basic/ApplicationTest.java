package ksbysample.webapp.basic;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ApplicationTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

/*
    @Test(expected = Exception.class)
    public void testApplicationProductMode() {
        System.setProperty("spring.profiles.active", "product");
        Application.main(new String[] {"--server.port=8081"});
        String output = this.outputCapture.toString();
        assertTrue(output, output.contains("Started Application"));
        System.setProperty("spring.profiles.active", "");
    }

    @Test(expected = Exception.class)
    public void testApplicationDevelopMode() {
        System.setProperty("spring.profiles.active", "develop");
        Application.main(new String[]{"--server.port=8082"});
        String output = this.outputCapture.toString();
        assertTrue(output, output.contains("Started Application"));
        System.setProperty("spring.profiles.active", "");
    }

    @Test(expected = Exception.class)
    public void testApplicationEmptyMode() {
        Application.main(new String[]{"--server.port=8083"});
        String output = this.outputCapture.toString();
        assertFalse(output, output.contains("Started Application"));
        assertTrue(output, output.contains("JVMの起動時引数 -Dspring.profiles.active で develop か product を指定して下さい"));
    }
*/

}