package repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import domain.Speaker;

public interface SpeakerRepository extends CrudRepository<Speaker, Long>
{
	Optional<Speaker> findByFirstnameAndLastnameAndOrganization(String firstname, String lastname, String organization);
}
