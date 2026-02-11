package service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Room;
import exception.CustomGenericException;
import exception.RoomAlreadyExistsException;
import repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Override
    public Room editRoom(Room editedRoom, Long id) {

        Optional<Room> knownRoomOpt = roomRepository.findById(id);
        
        if (knownRoomOpt.isPresent()) {
            Room knownRoom = knownRoomOpt.get();
    		if(!editedRoom.getRoomcode().equals(knownRoom.getRoomcode()))
    	        checkDouble(editedRoom);
            knownRoom.setRoomcode(editedRoom.getRoomcode());
            knownRoom.setCapacity(editedRoom.getCapacity());
            return roomRepository.save(knownRoom);
        }
        return null;
    }

	@Override
	public Room addRoom(Room room) {
		checkDouble(room);
    	return roomRepository.save(room);
	}
	
	private void checkDouble(Room room) {
		if(roomRepository.existsByRoomcode(room.getRoomcode()))
			throw new RoomAlreadyExistsException(room.getRoomcode());
	}

	@Override
	public int findCapacityOfRoomById(Long id) {
		Optional<Room> room = roomRepository.findById(id);
		if (room.isEmpty())
			throw new CustomGenericException("404", "Room not found for this ID!");
		return room.get().getCapacity();
	}
}
