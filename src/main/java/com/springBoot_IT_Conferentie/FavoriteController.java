package com.springBoot_IT_Conferentie;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import domain.Category;
import domain.User;
import jakarta.servlet.http.HttpServletRequest;
import repository.UserRepository;
import service.ConferenceService;
import service.UserService;

@Controller
public class FavoriteController {
	
	@Autowired
	private ConferenceService conferenceService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@GetMapping("/favorites")
	public String getEvents(@RequestParam(required = false) String category,
	                         @RequestParam(required = false) LocalDate date,
	                         HttpServletRequest request, Model model) {
		
		user = userRepository.findByUsername("user@example.com").get();
	    model.addAttribute("user", user);
		
	    model.addAttribute("eventList", userService.getFilteredFavoriteEvents(category, date, user));
	    model.addAttribute("categories", Category.values());
	    model.addAttribute("dates", conferenceService.getDateRange());

	    model.addAttribute("selectedCategory", category);
	    model.addAttribute("selectedDate", date);	    
	    model.addAttribute("maxFavoEvents", User.MAX_EVENTS);
	    
	    String currentRequestURI = (request != null) ? request.getRequestURI() : "/error/404";
        model.addAttribute("request", currentRequestURI);
	    
	    return "favorites";
	}
	
	@PostMapping("/favorites/favorite")
    public String toggleFavorite(@RequestParam Long eventId, @RequestHeader(required = false) String referer) {
		userRepository.findByUsername("user@example.com").ifPresent(user -> {
            userService.addRemoveFavoriteEvent(eventId, user);
        });
        return "redirect:" + (referer != null ? referer : "/events");
    }
}
