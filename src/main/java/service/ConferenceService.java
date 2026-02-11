package service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import domain.Category;
import domain.Event;

public interface ConferenceService {

	@Transactional
	public ResponseEntity<byte[]> loadEventImage(@PathVariable Long id);
	
	@Transactional
	public List<LocalDate> getDateRange();
	
	@Transactional
	public List<Event> getFilteredEvents(String category, LocalDate date);

	@Transactional
	public List<Event> getEventsByCategory(Category category, Long currentEventId);

	@Transactional
	public List<Event> getEventsByDate(LocalDate date);
}
