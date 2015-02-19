package ksbysample.webapp.basic.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:constant.properties")
@Getter
public class Constant {

    @Value("#{'${continent.list}'.split(',')}")
    private List<String> CONTINENT_LIST;

}
