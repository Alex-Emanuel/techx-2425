package com.springBoot_IT_Conferentie;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import domain.Event;
import domain.MyUser;
import domain.Role;
import exception.CustomGenericException;
import repository.UserRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@Autowired
	private UserRepository userRepository;

	@ModelAttribute("username")
    public String populateUsername(Authentication authentication) {
        return authentication == null ? "" : authentication.getName();
    }
    
	@ModelAttribute("role")
    public String populateRoles(Authentication authentication) {
        if (authentication == null) 
        	return null;
        
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String auth = authority.getAuthority();
            try {
            	return Role.valueOf(auth.replace("ROLE_", ""))
            			   .getDisplayRole();
            } catch (IllegalArgumentException e) {
                return "";
            }
        }
        return "";
    }
    
	@ModelAttribute("favoriteEvents")
    public Set<Event> populateFavoriteEvents(Authentication authentication) {
        if (authentication == null) 
        	return Set.of();

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                             .map(MyUser::getFavoriteEvents)
                             .orElse(Set.of());
    }
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
		model.addAttribute("errCode", "400");
		model.addAttribute("errMsg", "Ongeldige input voor ID.");
	    return "error/generic_error";
	}
	
	@ExceptionHandler(CustomGenericException.class)
    public ModelAndView handleCustomException(CustomGenericException ex) {
        ModelAndView model
                = new ModelAndView("error/generic_error");
        model.addObject("errCode", ex.getErrCode());
        model.addObject("errMsg", ex.getErrMsg());
        return model;
    }
}