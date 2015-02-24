package ksbysample.webapp.basic.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:constant.properties")
public class Constant {

    public final List<String> CONTINENT_LIST;

    @Autowired
    public Constant(
            @Value("#{'${CONTINENT_LIST}'.split(',')}") List<String> CONTINENT_LIST
    ) {
        this.CONTINENT_LIST = CONTINENT_LIST;
    }

}
