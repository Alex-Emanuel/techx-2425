package repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import domain.MyUser;

public interface UserRepository extends CrudRepository<MyUser, Long>
{
	Optional<MyUser> findByUsername(String string);
}
