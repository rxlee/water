package com.linkel.water;

import com.linkel.water.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@SpringBootApplication
@ComponentScan(value = "com.linkel.water")
@PropertySource(value= "classpath:/application.properties")
public class Application {
	@Configuration
    @Profile("production")
    @PropertySource("classpath:/application.properties")
    static class Production
    { }

    @Configuration
    @Profile("local")
    @PropertySource({"classpath:/application.properties"})
    static class Local
    { }
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        Server server = context.getBean(Server.class);
        server.start();
	}

	@Value("${tcp.port}")
    private int tcpPort;


    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;
    

}