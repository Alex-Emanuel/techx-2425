package com.springBoot_IT_Conferentie;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import domain.Category;
import domain.MyUser;
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
	
	@GetMapping("/favorites")
	public String getEvents(@RequestParam(required = false) String category,
	                         @RequestParam(required = false) LocalDate date,
	                         Authentication authentication, Model model) {
		
	    MyUser user = userRepository.findByUsername(authentication.getName()).get();
	    model.addAttribute("eventList", userService.getFilteredFavoriteEvents(category, date, user));
	    model.addAttribute("categories", Category.values());
	    model.addAttribute("dates", conferenceService.getDateRange());

	    model.addAttribute("selectedCategory", category);
	    model.addAttribute("selectedDate", date);	    
	    model.addAttribute("maxFavoEvents", MyUser.MAX_EVENTS);
	    
	    return "favorites";
	}
	
	@PostMapping("/favorites/favorite")
	public String toggleFavorite(@RequestParam Long eventId, @RequestHeader(required = false) String referer,
	                             Authentication authentication) {
		
	    String username = authentication.getName();
	    userRepository.findByUsername(username).ifPresent(user -> {
	        userService.addRemoveFavoriteEvent(eventId, user);
	    });

	    return "redirect:" + (referer != null ? referer : "/events");
	}
}
