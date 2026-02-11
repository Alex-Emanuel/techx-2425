package repository;

import org.springframework.data.repository.CrudRepository;

import domain.Room;

public interface RoomRepository extends CrudRepository<Room, String>
{}
