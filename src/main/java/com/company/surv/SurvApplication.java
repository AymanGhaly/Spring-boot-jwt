package com.company.surv;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan()
public class SurvApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		new SpringApplicationBuilder(SurvApplication.class).run(args);

	}
}
