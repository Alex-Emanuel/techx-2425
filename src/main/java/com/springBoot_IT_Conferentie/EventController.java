package com.springBoot_IT_Conferentie;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import domain.Category;
import domain.Event;
import domain.Speaker;
import domain.MyUser;
import jakarta.validation.Valid;
import repository.EventRepository;
import repository.RoomRepository;
import service.ConferenceService;
import service.EventService;

@Controller
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private ConferenceService conferenceService;
	
	@Autowired
	private EventService eventService;

	@GetMapping("/events/{id}")
	public String getEventDetail(@PathVariable Long id, Model model) {
	    Optional<Event> optionalEvent = eventRepository.findById(id);
	    
	    if (optionalEvent.isPresent()) {
	        Event event = optionalEvent.get();
	        model.addAttribute("event", event);
	        model.addAttribute("recommendedEvents", conferenceService.getEventsByCategory(event.getCategory(), event.getId()));
	        model.addAttribute("categories", Category.values());
	        model.addAttribute("maxFavoEvents", MyUser.MAX_EVENTS);
	        return "event-detail";
	    } else {
	        return "redirect:/error/404";
	    }
	}
	
	@GetMapping("/saveevent")
	public String getEventForm(@RequestParam(required = false) Long id, Model model) {
		Event event;
    	
        if (id != null) {
            Optional<Event> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty())
                return "redirect:/error/404";
            event = optionalEvent.get();
            int aantal = 3 - event.getSpeakers().size();
            for (int i = 0; i < aantal; i++)
                event.addSpeaker(new Speaker());
        } else {
            event = new Event();
            for (int i = 0; i < 3; i++)
                event.addSpeaker(new Speaker());
        }
        
	    model.addAttribute("newevent", event);
	    model.addAttribute("rooms", roomRepository.findAll());
	    model.addAttribute("categories", Category.values());
	    model.addAttribute("event_id", id);
	    return "save-event";
	}
	
	@PostMapping("/saveevent")
	public String processRegistration(@ModelAttribute("newevent") @Valid Event newevent, 
	         BindingResult result, Model model, 
	         @RequestParam(required = false, defaultValue = "-1") Long event_id,
	         @RequestParam(required = false) MultipartFile image) {
        
	    if (result.hasErrors()) {
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
