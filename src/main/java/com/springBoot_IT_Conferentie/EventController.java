package com.springBoot_IT_Conferentie;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import domain.Category;
import domain.Event;
import domain.Speaker;
import domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import repository.EventRepository;
import repository.RoomRepository;
import repository.UserRepository;
import service.ConferenceService;
import service.EventService;

@Controller
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private ConferenceService conferenceService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
    private Validator beamerValidator;
	
	private User user;
	
	private Long event_id = -1L;

	@GetMapping("/events/{id}")
	public String getEventDetail(@PathVariable Long id, HttpServletRequest request, Model model) {
	    Optional<Event> optionalEvent = eventRepository.findById(id);
	    
	    if (optionalEvent.isPresent()) {
	        Event event = optionalEvent.get();
	        model.addAttribute("event", event);
	        user = userRepository.findByUsername("admin@example.com").get();
	        model.addAttribute("user", user);
	        model.addAttribute("recommendedEvents", conferenceService.getEventsByCategory(event.getCategory(), event.getId()));
	        model.addAttribute("categories", Category.values());
	        model.addAttribute("maxFavoEvents", User.MAX_EVENTS);
	        
	        String currentRequestURI = (request != null) ? request.getRequestURI() : "/error/404";
	        model.addAttribute("request", currentRequestURI);
	        return "event-detail";
	    } else {
	        return "redirect:/error/404";
	    }
	}
	
	@GetMapping("/saveevent")
	public String getEventForm(@RequestParam(required = false) Long id, Model model) {
		user = userRepository.findByUsername("admin@example.com").get();
		
		Event event;
    	event_id = id;
    	
        if (id != null) {
            event = eventRepository.findById(id).get();
            if (event == null) 
            	return "redirect:/error/404";
            int aantal = 3 - event.getSpeakers().size();
            for (int i = 0; i < aantal; i++)
    			event.addSpeaker(new Speaker());
        } else {
            event = new Event();
            for (int i = 0; i < 3; i++)
    			event.addSpeaker(new Speaker());
        }
        
        model.addAttribute("user", user);
	    model.addAttribute("newevent", event);
	    model.addAttribute("rooms", roomRepository.findAll());
	    model.addAttribute("categories", Category.values());
	    model.addAttribute("event_id", event_id);
	    return "save-event";
	}
	
	@PostMapping("/saveevent")
	public String processRegistration(@ModelAttribute("newevent") @Valid Event newevent, 
	         BindingResult result, Model model, @RequestParam(required = false) MultipartFile image) {
		
		user = userRepository.findByUsername("admin@example.com").get();
        
		beamerValidator.validate(newevent, result);
        
	    if (result.hasErrors()) {
	    	model.addAttribute("user", user);
	        model.addAttribute("rooms", roomRepository.findAll());
	        model.addAttribute("categories", Category.values());
	        model.addAttribute("event_id", event_id);
	        return "save-event";
	    }
		
		Optional<Event> existingEvent = Optional.empty();
        if(event_id != null && event_id != -1)
        	existingEvent = eventRepository.findById(event_id);
		
        if (existingEvent.isPresent()) {
        	Event knownEvent = existingEvent.get();
            eventService.editEvent(knownEvent, newevent, image);
            return "redirect:/events/" + knownEvent.getId();
        } 

		eventService.saveImage(image, newevent);
	    eventService.makeEvent(newevent);
	    return "redirect:/events";
	}

}
