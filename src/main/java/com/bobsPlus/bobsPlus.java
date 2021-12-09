package com.bobsPlus;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@SpringBootApplication
public class bobsPlus {

    public static void main(String[] args) {
        SpringApplication.run(bobsPlus.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        // Enable SSL Trafic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        // Add HTTP to HTTPS redirect
        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());

        return tomcat;
    }
 
	private Connector httpToHttpsRedirectConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8080);
	       	connector.setSecure(false);
		connector.setRedirectPort(443);
		return connector;
	}
}
