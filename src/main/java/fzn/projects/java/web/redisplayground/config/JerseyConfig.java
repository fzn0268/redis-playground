package fzn.projects.java.web.redisplayground.config;

import fzn.projects.java.web.redisplayground.endpoint.CarEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(CarEndpoint.class);
    }
}
