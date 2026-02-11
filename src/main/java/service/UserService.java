package service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import domain.Event;
import domain.MyUser;

public interface UserService {

	@Transactional
	public void addRemoveFavoriteEvent(Long eventId, MyUser user);
	
	@Transactional
	public List<Event> getFilteredFavoriteEvents(String category, LocalDate date, MyUser user);
}
