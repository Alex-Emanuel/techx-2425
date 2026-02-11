package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import domain.Category;
import domain.Event;

public interface EventRepository extends CrudRepository<Event, Long>
{
	Optional<List<Event>> findByCategory(Category cat);
}
