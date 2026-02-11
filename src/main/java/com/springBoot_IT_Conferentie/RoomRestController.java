package com.springBoot_IT_Conferentie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.RoomService;

@RestController
@RequestMapping(value = "/rest")
public class RoomRestController {
	
	@Autowired
	private RoomService roomService;

	// Ophalen van een capaciteit van een gegeven lokaal
	@GetMapping("/rooms/{id}/capacity")
    public int getCapacityOfRoom(@PathVariable Long id) {
        return roomService.findCapacityOfRoomById(id);
    }
}