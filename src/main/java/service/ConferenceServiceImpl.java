package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.Category;
import domain.Event;
import repository.EventRepository;

@Service
public class ConferenceServiceImpl implements ConferenceService {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public ResponseEntity<byte[]> loadEventImage(Long id) {		
		return eventRepository.findById(id)
				.map(event -> ResponseEntity.ok()
								.contentType(MediaType.IMAGE_JPEG)
								.body(event.getImage()))
		        .orElse(ResponseEntity.notFound().build());
	}

	@Override
	public List<LocalDate> getDateRange() {
		List<Event> events = getFilteredEvents(null, null);
		return events.stream()
	             .map(Event::getDate)
	             .distinct()
	             .sorted()
	             .collect(Collectors.toList());
	}
	
	@Override
	public List<Event> getFilteredEvents(String category, LocalDate date) {
	    List<Event> events = new ArrayList<>();

	    // Filter on both category and date
	    if (category != null && !category.isEmpty() && date != null)
	        events = eventRepository.findByCategoryAndDate(Category.valueOf(category.toUpperCase()), date)
	        		.orElse(Collections.emptyList());
	    // Filter on category
	    else if (category != null && !category.isEmpty())
	        events = eventRepository.findByCategory(Category.valueOf(category.toUpperCase()))
	        		.orElse(Collections.emptyList());
	    // Filter on date
	    else if (date != null)
	        events = eventRepository.findByDate(date).orElse(Collections.emptyList());
	    // No filters
	    else 
	        events = (List<Event>) eventRepository.findAll();
	    
	    // Sort
	    events.sort(Comparator.comparing(Event::getDate)
	                          .thenComparing(Event::getTime));
	    return events;
	}

	@Override
	public List<Event> getEventsByCategory(Category category, Long currentEventId) {
		
		List<Event> events = eventRepository.findByCategory(category).get()
		        .stream()
		        .filter(event -> !currentEventId.equals(event.getId()))
		        .sorted(Comparator.comparing(Event::getDate)
		            .thenComparing(Event::getTime)
		            .thenComparing(Event::getName))
		        .limit(3)
		        .collect(Collectors.toList());

		return events;
	}

	@Override
	public List<Event> getEventsByDate(LocalDate date) {
		return eventRepository.findByDate(date).orElse(Collections.emptyList());
	}
}
