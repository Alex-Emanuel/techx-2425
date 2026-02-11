package com.springBoot_IT_Conferentie;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import domain.Category;
import domain.MyUser;
import service.ConferenceService;

@Controller
public class ConferenceController {

	@Autowired
	private ConferenceService conferenceService;
	
	@GetMapping("/events")
	public String getEvents(@RequestParam(required = false) String category,
	                         @RequestParam(required = false) LocalDate date,
	                         Model model) {
		
	    model.addAttribute("eventList", conferenceService.getFilteredEvents(category, date));
	    model.addAttribute("categories", Category.values());
	    model.addAttribute("dates", conferenceService.getDateRange());

	    model.addAttribute("selectedCategory", category);
	    model.addAttribute("selectedDate", date);	    
	    model.addAttribute("maxFavoEvents", MyUser.MAX_EVENTS);
	    
	    return "events";
	}
	
	@GetMapping(value = "/images/events/{id}")
	public ResponseEntity<byte[]> loadEventImage(@PathVariable Long id) {
		return conferenceService.loadEventImage(id);
	}
}
