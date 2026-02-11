package com.springBoot_IT_Conferentie;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import service.ConferenceService;
import service.ConferenceServiceImpl;
import service.EventService;
import service.EventServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import validator.BeamerValidator;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class ITConferenceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ITConferenceApplication.class, args);
	}
	
	@Bean
	LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		return slr;
	}

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/events");
        registry.addViewController("/error").setViewName("error/404");
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
	
	@Bean
	ConferenceService conferenceservice() {
		return new ConferenceServiceImpl();
	}
	
	@Bean
	UserService userservice() {
		return new UserServiceImpl();
	}
	
	@Bean
	EventService eventservice() {
		return new EventServiceImpl();
	}
	
	@Bean
	Validator beamerValidator() {
		return new BeamerValidator();
	}
}
