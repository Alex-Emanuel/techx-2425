package service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Event;
import domain.User;
import repository.EventRepository;
import repository.UserRepository;

public class UserServiceImpl implements UserService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void addRemoveFavoriteEvent(Long eventId, User user) {
		Event event = eventRepository.findById(eventId).get();
		
		if (user.getFavoriteEvents().contains(event))
	    	user.removeFavoriteEvent(event);
	    else
	    	user.addFavoriteEvent(event);
		
		userRepository.save(user);
	}

	@Override
	public List<Event> getFilteredFavoriteEvents(String category, LocalDate date, User user) {
		List<Event> events = user.getFavoriteEvents().stream().collect(Collectors.toList());
		
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

	    events.sort(Comparator.comparing(Event::getDate)
	    			.thenComparing(Comparator.comparing(Event::getTime)
	    			.thenComparing(Comparator.comparing(Event::getName))));
	    
	    return events;
	}
}
