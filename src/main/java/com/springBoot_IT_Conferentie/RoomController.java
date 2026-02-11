package com.springBoot_IT_Conferentie;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Room;
import jakarta.validation.Valid;
import repository.RoomRepository;
import service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomService roomService;
	
	@GetMapping(value = "/list")
	public String getRooms(Model model) {
	    model.addAttribute("rooms", roomRepository.findAll());
	    return "rooms";
	}
	
	@GetMapping(value = "/save")
	public String getRoomForm(@RequestParam(required = false) Long id, Model model) {
		
		Room room;
    	
        if (id != null) {
        	Optional<Room> optionalRoom = roomRepository.findById(id);
            if (optionalRoom.isEmpty())
                return "redirect:/error/404";
            room = optionalRoom.get();
        } else {
        	room = new Room();
        }
        
	    model.addAttribute("newroom", room);
	    model.addAttribute("room_id", id);
	    return "save-room";
	}
	
	@PostMapping("/save")
    public String saveRoom(@ModelAttribute("newroom") @Valid Room room, BindingResult result,
            @RequestParam(required = false, defaultValue = "-1") Long room_id,
            Model model, RedirectAttributes ra) {
        
        if (result.hasErrors()) {
            model.addAttribute("room_id", room_id);
            return "save-room";
        }
        
        String action;
        
        if (room_id != null && room_id != -1) {
        	roomService.editRoom(room, room_id);
        	action = "room.saved.updated";
        }
        else {
        	roomService.addRoom(room);
        	action = "room.saved.added";
        }
        ra.addFlashAttribute("newCapacity", room.getCapacity());
        ra.addFlashAttribute("newName", room.getRoomcode());
        ra.addFlashAttribute("action", action);
        return "redirect:/rooms/list";
    }
}
