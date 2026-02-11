package repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import domain.Category;
import domain.Event;
import domain.Room;

public interface EventRepository extends CrudRepository<Event, Long>
{
	Optional<List<Event>> findByCategory(Category cat);
	Optional<List<Event>> findByDate(LocalDate date);
	Optional<List<Event>> findByCategoryAndDate(Category cat, LocalDate date);
	
	Optional<Event> findByDateAndTimeAndRoom(LocalDate date, LocalTime time, Room room);
	Optional<Event> findByNameAndDate(String name, LocalDate date);
	
}
