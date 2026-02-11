package com.springBoot_IT_Conferentie;

import java.util.Locale;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import perform.PerformRestConference;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
@ComponentScan({"service", "com.springBoot_IT_Conferentie"})
public class ITConferenceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ITConferenceApplication.class, args);
		
		try {
			new PerformRestConference();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bean
	LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.getDefault());
		return slr;
	}

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/events");
        registry.addViewController("/error").setViewName("error/404");
        registry.addViewController("/403").setViewName("error/403");
    }
	
	@Bean
    SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        mappings.put("exception.DoubleBookingException", 
        		     "error/eventDoubleBooked");
        mappings.put("exception.DuplicateEventNameException", 
   		     "error/eventDuplicateName");
        mappings.put("exception.RoomAlreadyExistsException", 
      		     "error/roomAlreadyExists");
        mappings.put("java.lang.NumberFormatException", 
                "error/generic_error");

        r.setDefaultErrorView("error/error");
        r.setExceptionMappings(mappings);
        return r;
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
