package ksbysample.webapp.basic;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class ApplicationTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void testApplicationProductMode() {
        String SPRING_PROFILES_ACTIVE = System.getProperty("spring.profiles.active");
        System.setProperty("spring.profiles.active", "product");
        Application.main(new String[]{"--server.port=8081"});
        String output = this.outputCapture.toString();
        assertThat(output.contains("Started Application"), is(true));
        System.setProperty("spring.profiles.active", SPRING_PROFILES_ACTIVE);
    }

    @Test
    public void testApplicationDevelopMode() {
        String SPRING_PROFILES_ACTIVE = System.getProperty("spring.profiles.active");
        System.setProperty("spring.profiles.active", "develop");
        Application.main(new String[]{"--server.port=8082"});
        String output = this.outputCapture.toString();
        assertThat(output.contains("Started Application"), is(true));
        System.setProperty("spring.profiles.active", SPRING_PROFILES_ACTIVE);
    }

    @Test
    public void testApplicationEmptyMode() {
        String SPRING_PROFILES_ACTIVE = System.getProperty("spring.profiles.active");
        System.setProperty("spring.profiles.active", "");
        try {
            Application.main(new String[]{"--server.port=8083"});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String output = this.outputCapture.toString();
        assertThat(output.contains("Started Application"), is(false));
        assertThat(output.contains("JVMの起動時引数 -Dspring.profiles.active で develop か product を指定して下さい"), is(true));
        System.setProperty("spring.profiles.active", SPRING_PROFILES_ACTIVE);
    }

}