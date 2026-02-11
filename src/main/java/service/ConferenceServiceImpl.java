package service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import domain.Category;
import domain.Event;
import repository.EventRepository;

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
	    List<Event> events = (List<Event>) eventRepository.findAll();

	    // Filter on category
	    if (category != null && !category.isEmpty()) {
	        events = events.stream()
	                       .filter(event -> event.getCategory().name().equalsIgnoreCase(category))
	                       .collect(Collectors.toList());
	    }

	    // Filter on date
	    if (date != null) {
	        events = events.stream()
	                       .filter(event -> event.getDate().equals(date))
	                       .collect(Collectors.toList());
	    }

	    events.sort(Comparator.comparing(Event::getDate).thenComparing(Comparator.comparing(Event::getTime)));
	    
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
}
