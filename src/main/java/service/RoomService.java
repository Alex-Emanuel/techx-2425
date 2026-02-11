package service;

import org.springframework.transaction.annotation.Transactional;

import domain.Room;

public interface RoomService {
	
	@Transactional
	public Room editRoom(Room editedRoom, Long id);

	@Transactional
	public Room addRoom(Room room);

	@Transactional
	public int findCapacityOfRoomById(Long id);
}
