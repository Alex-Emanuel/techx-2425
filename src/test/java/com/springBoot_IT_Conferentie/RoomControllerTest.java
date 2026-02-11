package com.springBoot_IT_Conferentie;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.Room;
import repository.EventRepository;
import repository.RoomRepository;
import repository.SpeakerRepository;
import repository.UserRepository;
import service.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private EventRepository mockEventRepository;
	@MockitoBean
	private RoomRepository mockRoomRepository;
	@MockitoBean
	private SpeakerRepository mockSpeakerRepository;
	@MockitoBean
	private UserRepository mockUserRepository;
	@MockitoBean
	private RoomService mockRoomService;
	
	private static Room room;
	
	@BeforeEach
	private void setUp() {
		room = new Room(0, "A001", 25);
	}	
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetRoomsRequest() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/list"))
		.andExpect(view().name("rooms"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("rooms"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetAddRoomFormRequest() throws Exception {
		// Arrange
		// =======
		when(mockRoomRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/save"))
		.andExpect(status().isOk())
		.andExpect(view().name("save-room"))
		.andExpect(model().attributeExists("newroom"))
		.andExpect(model().attribute("room_id", nullValue()));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetEditRoomFormRequest_WithValidId() throws Exception {
		// Arrange
		// =======
		when(mockRoomRepository.findById(1L)).thenReturn(Optional.of(room));

		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/save").param("id", "1"))
		.andExpect(status().isOk())
		.andExpect(view().name("save-room"))
		.andExpect(model().attributeExists("newroom"))
		.andExpect(model().attribute("newroom", room))
		.andExpect(model().attributeExists("room_id"))
		.andExpect(model().attribute("room_id", 1L));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetEditRoomFormRequest_WithInvalidId() throws Exception {
		// Arrange
		// =======
		when(mockRoomRepository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/save").param("id", "999"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/error/404"));
	}
	
	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	void testGetRoomFormNoAccessWithUserRole() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/save"))
		.andExpect(status().isForbidden());
	}
	
	private Room createRoom(String roomcode, int capacity) {
		return new Room(0, roomcode, capacity);
	}
	
	private static Stream<Arguments> validRoomData() {
	    return Stream.of(
	    	// Naam van het lokaal moet beginnen met een letter, gevolgd door 3 cijfers
	    	Arguments.of("A001", 20),
	    	Arguments.of("A123", 20),
	    	Arguments.of("a123", 20),
	    	Arguments.of("B000", 20),
	    	Arguments.of("C999", 20),
	    	//Capaciteit is groter dan 0 en kleiner of gelijk aan 50
	    	Arguments.of("A123", 1),
	    	Arguments.of("A123", 10),
	    	Arguments.of("A123", 25),
	    	Arguments.of("A123", 49),
	    	Arguments.of("A123", 50)
	    );
	}
	
	@ParameterizedTest
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@MethodSource("validRoomData")
    public void testPostRequestAddValidRoom(String roomcode, int capacity) throws Exception {
		
		// Arrange
		// =======
		Room validRoom = createRoom(roomcode, capacity);
		
		// Act & Assert
		// ============
        mockMvc.perform(post("/rooms/save")
        .flashAttr("newroom", validRoom)
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/rooms/list"))
		.andExpect(flash().attribute("newCapacity", capacity))
        .andExpect(flash().attribute("newName", roomcode))
        .andExpect(flash().attribute("action", "room.saved.added"));
    }
	
	private static Stream<Arguments> invalidRoomData() {
	    return Stream.of(
	    	// Naam van het lokaal moet beginnen met een letter, gevolgd door 3 cijfers
	    	Arguments.of("1234", 20, new String[] {"roomcode"}),
	    	Arguments.of("AB12", 20, new String[] {"roomcode"}),
	    	Arguments.of("A12", 20, new String[] {"roomcode"}),
	    	Arguments.of("A1234", 20, new String[] {"roomcode"}),
	    	Arguments.of("A12B", 20, new String[] {"roomcode"}),
	    	Arguments.of("1A23", 20, new String[] {"roomcode"}),
	    	Arguments.of("", 20, new String[] {"roomcode"}), // empty
	    	Arguments.of("    ", 20, new String[] {"roomcode"}), // spaces
	    	Arguments.of("\t", 20, new String[] {"roomcode"}), // tab
	    	Arguments.of("A 12", 20, new String[] {"roomcode"}),
	    	Arguments.of("A12!", 20, new String[] {"roomcode"}),
	    	Arguments.of(" a12", 20, new String[] {"roomcode"}),
	    	Arguments.of("A-12", 20, new String[] {"roomcode"}),
	    	//Capaciteit is groter dan 0 en kleiner of gelijk aan 50
	    	Arguments.of("A123", -100, new String[] {"capacity"}),
	    	Arguments.of("A123", -1, new String[] {"capacity"}),
	    	Arguments.of("A123", 51, new String[] {"capacity"}),
	    	Arguments.of("A123", 500, new String[] {"capacity"})
	    );
	}
	
	@ParameterizedTest
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@MethodSource("invalidRoomData")
    public void testPostRequestInvalidRoom(String roomcode, int capacity, String[] expectedErrors) 
    		throws Exception 
	{	
		// Arrange
		// =======
		Room invalidRoom = createRoom(roomcode, capacity);
		
		// Act & Assert
		// ============
        mockMvc.perform(post("/rooms/save")
        .flashAttr("newroom", invalidRoom)
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("save-room"))
        .andExpect(model().attributeHasFieldErrors("newroom", expectedErrors));
    }
}
