package com.springBoot_IT_Conferentie;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import domain.Room;
import exception.CustomGenericException;
import service.RoomService;

@SpringBootTest
class RoomRestControllerTest {

	@Mock
	private RoomService mock;
	
	private RoomRestController controller;
	private MockMvc mockMvc;
	
	private int expectedCapacity;
	
	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new RoomRestController();
		mockMvc = standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "roomService", mock);
	}
	
	private Room aRoom(String roomcode, int capacity) {
		Room room = new Room(0, roomcode, capacity);
		expectedCapacity = room.getCapacity();
		return room;
	}
	
	private void performRest(String uri) throws Exception {
		mockMvc.perform(get("/rest" + uri))
			.andExpect(status().isOk())
			.andExpect(content().string(String.valueOf(expectedCapacity)));
	}
	
	@ParameterizedTest
	@CsvSource({ "A001,30", "B002,50", "C003,10" })
	public void testGetCapacityOfRoom_isOk(String roomcode, int capacity) throws Exception {
		Room room = aRoom(roomcode, capacity);
		long roomId = room.getId();
	    when(mock.findCapacityOfRoomById(roomId)).thenReturn(expectedCapacity);
	    performRest("/rooms/" + roomId + "/capacity");
	    verify(mock).findCapacityOfRoomById(roomId);
	}
	
	@Test
	public void testGetCapacityOfRoom_notFound() throws Exception {
		Mockito.when(mock.findCapacityOfRoomById(10L)).thenThrow(new CustomGenericException("404", "Room not found for this ID!"));
		Exception exception = assertThrows(Exception.class, () -> {
			mockMvc.perform(get("/rest/rooms/" + 10 + "/capacity")).andReturn();
	    });
		assertTrue(exception.getCause() instanceof CustomGenericException);
		Mockito.verify(mock).findCapacityOfRoomById(10L);
	}
}
