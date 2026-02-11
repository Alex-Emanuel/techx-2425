package com.springBoot_IT_Conferentie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Event;
import service.ConferenceService;

@RestController
@RequestMapping(value = "/rest")
public class ConferenceRestController {

	@Autowired
	private ConferenceService conferenceService;
	
	// ophalen van eventgegevens van een gegeven datum
	@GetMapping("/events/{date}")
	public List<Event> getEventsByDate(@PathVariable String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate convertedDate = LocalDate.parse(date, formatter);
	    return conferenceService.getEventsByDate(convertedDate);
	}
}
