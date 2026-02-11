package com.springBoot_IT_Conferentie;

import static domain.Category.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
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

import domain.Category;
import domain.Event;
import domain.Room;
import domain.Speaker;
import repository.EventRepository;
import repository.RoomRepository;
import repository.SpeakerRepository;
import repository.UserRepository;
import service.ConferenceService;
import service.EventService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ConferenceService mockConferenceService;
	@MockitoBean
	private EventService mockEventService;
	
	@MockitoBean
	private EventRepository mockEventRepository;
	@MockitoBean
	private RoomRepository mockRoomRepository;
	@MockitoBean
	private SpeakerRepository mockSpeakerRepository;
	@MockitoBean
	private UserRepository mockUserRepository;
	
	private static Speaker speaker1, speaker2, speaker3, speaker4;
	private static Room room;
	private Event expectedEvent;

	@BeforeEach
	private void setUp() {
		speaker1 = new Speaker("Jan", "Jansen", "Google");
		speaker2 = new Speaker("Jane", "Doe", "Google");
		speaker3 = new Speaker("John", "Doe", "Google");
		speaker4 = new Speaker("Alex", "Emanuel", "Google");
		room = new Room(0, "A001", 25);
		expectedEvent = new Event("Test event", room, LocalDate.of(2025, 5, 22), LocalTime.of(14, 0), "1234", 70, 25.50, IT_MANAGEMENT);
		expectedEvent.addSpeaker(speaker1);
	}	
	
	@Test
	public void testGetDetailEvent() throws Exception {
		// Arrange
		// =======		
		when(mockEventRepository.findById(1L)).thenReturn(Optional.of(expectedEvent));
		when(mockConferenceService.getEventsByCategory(IT_MANAGEMENT, 1L)).thenReturn(List.of());
		
		// Act & Assert
		// ============
		mockMvc.perform(get("/events/1"))
		.andExpect(status().isOk())
		.andExpect(view().name("event-detail"))
        .andExpect(model().attributeExists("event"))
        .andExpect(model().attribute("event", expectedEvent))
        .andExpect(model().attributeExists("recommendedEvents"))
        .andExpect(model().attributeExists("categories"))
        .andExpect(model().attributeExists("maxFavoEvents"));
	}
	
	@Test
	public void testGetNoEventFound() throws Exception {
		// Arrange
		// =======
		when(mockEventRepository.findById(1L)).thenReturn(Optional.empty());
		
		// Act & Assert
		// ============
		mockMvc.perform(get("/events/1"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/error/404"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetAddEventFormRequest() throws Exception {
		// Arrange
		// =======
		when(mockRoomRepository.findAll()).thenReturn(List.of());

		// Act & Assert
		// ============
		mockMvc.perform(get("/saveevent"))
		.andExpect(status().isOk())
		.andExpect(view().name("save-event"))
		.andExpect(model().attributeExists("newevent"))
		.andExpect(model().attributeExists("rooms"))
		.andExpect(model().attributeExists("categories"))
		.andExpect(model().attribute("event_id", nullValue()));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetEditEventFormRequest_WithValidId() throws Exception {
		// Arrange
		// =======
		when(mockEventRepository.findById(1L)).thenReturn(Optional.of(expectedEvent));
		when(mockRoomRepository.findAll()).thenReturn(List.of(room));

		// Act & Assert
		// ============
		mockMvc.perform(get("/saveevent").param("id", "1"))
		.andExpect(status().isOk())
		.andExpect(view().name("save-event"))
		.andExpect(model().attributeExists("newevent"))
		.andExpect(model().attribute("newevent", expectedEvent))
		.andExpect(model().attributeExists("rooms"))
		.andExpect(model().attributeExists("categories"))
		.andExpect(model().attributeExists("event_id"))
		.andExpect(model().attribute("event_id", 1L));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testGetEditEventFormRequest_WithInvalidId() throws Exception {
		// Arrange
		// =======
		when(mockEventRepository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert
		// ============
		mockMvc.perform(get("/saveevent").param("id", "999"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/error/404"));
	}
	
	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	void testGetEventFormNoAccessWithUserRole() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/saveevent"))
		.andExpect(status().isForbidden());
	}

	private Event createEvent(String name, Room room, LocalDate date, LocalTime time, String beamerCode, 
            int beamerCheck, double price, Category category, List<Speaker> speakers) {
		Event event = new Event(name, room, date, time, beamerCode, beamerCheck, price, category);
		event.setSpeakers(speakers);
		return event;
	}
	
	private static Stream<Arguments> validEventData() {
	    return Stream.of(
	    	// Naam moet beginnen met een letter
	    	Arguments.of("A", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2)),
	    	Arguments.of("a", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2)),
	    	// Spreker(s): verplicht, meerdere mogelijk, maximum 3
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2, speaker3)),
	    	// De datum moet binnen de conferentieperiode vallen (kies zelf een conferentieperiode). 
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 23), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 25), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	// Beamercheck moet correct zijn: tweecijferige checksum: rest bij deling door 97 van de beamercode.
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "6666", 70, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "8910", 83, 20.00, CYBERSECURITY, List.of(speaker1)),
	    	// Prijs van dit event: moet groter of gelijk zijn aan 9,99 en kleiner dan 100.
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 9.99, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 23.00, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 55.25, CYBERSECURITY, List.of(speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 99.99, CYBERSECURITY, List.of(speaker1))
	    );
	}

	@ParameterizedTest
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@MethodSource("validEventData")
    public void testPostRequestValidEvent(String name, Room room, LocalDate date, LocalTime time, String beamerCode, 
    		int beamerCheck, double price, Category category, List<Speaker> speakers) throws Exception {
		
		// Arrange
		// =======
		Event validEvent = createEvent(name, room, date, time, beamerCode, beamerCheck, price, category, speakers);
		
		// Act & Assert
		// ============
        mockMvc.perform(post("/saveevent")
        .flashAttr("newevent", validEvent)
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/events"));
    }
	
	private static Stream<Arguments> invalidEventData() {
	    return Stream.of(
	    	// Naam moet beginnen met een letter
	    	Arguments.of("1Event", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of(" Event", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of("#Event", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of("@Event", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of(".Event", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of("", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	Arguments.of("   ", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[]{"name"}),
	    	// Spreker(s): verplicht, meerdere mogelijk, maximum 3
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, Collections.EMPTY_LIST, new String[] {"speakers"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2, speaker3, speaker4), new String[] {"speakers"}),
	    	// Lokaal: verplicht
	    	Arguments.of("Testnaam", null, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"room"}),
	    	// De datum moet binnen de conferentieperiode vallen
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 21), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"date"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 4, 21), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"date"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 26), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"date"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 6, 26), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"date"}),
	    	// Beamercode: viercijferige code om de beamer te activeren
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "123", 26, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"beamerCode"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "12345", 26, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"beamerCode"}),
	    	// Beamercheck tweecijferige checksum: rest bij deling door 97 van de beamercode.
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 46, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"beamerCheck"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 71, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"beamerCheck"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 69, 20.00, CYBERSECURITY, List.of(speaker1, speaker2), new String[] {"beamerCheck"}),
	    	// Prijs van dit event: moet groter of gelijk zijn aan 9,99 en kleiner dan 100.
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 9.98, CYBERSECURITY, List.of(speaker1), new String[] {"price"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 100.00, CYBERSECURITY, List.of(speaker1), new String[] {"price"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 7.00, CYBERSECURITY, List.of(speaker1), new String[] {"price"}),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 1000.00, CYBERSECURITY, List.of(speaker1), new String[] {"price"})
	    );
	}
	
	@ParameterizedTest
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@MethodSource("invalidEventData")
	public void testPostRequestInvalidEvent(String name, Room room, LocalDate date, LocalTime time, String beamerCode, 
    		int beamerCheck, double price, Category category, List<Speaker> speakers, String[] expectedErrors) throws Exception 
	{
		// Arrange
		// =======
		Event invalidEvent = createEvent(name, room, date, time, beamerCode, beamerCheck, price, category, speakers);
		
		// Act & Assert
		// ============
        mockMvc.perform(post("/saveevent")
        .flashAttr("newevent", invalidEvent)
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("save-event"))
        .andExpect(model().attributeHasFieldErrors("newevent", expectedErrors));
	}
	
	private static Stream<Arguments> invalidEventData_global() {
	    return Stream.of(
	    	// Geen twee keer dezelfde spreker.
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker1, speaker1)),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(speaker2, speaker2)),
	    	// Geen ingevulde spreker
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(new Speaker("", "", ""))),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(new Speaker("", "", ""), new Speaker("", "", ""))),
	    	Arguments.of("Testnaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, List.of(new Speaker("", "", ""), new Speaker("", "", ""), new Speaker("", "", "")))
	    );
	}

	@ParameterizedTest
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@MethodSource("invalidEventData_global")
	public void testPostRequestInvalidEvent_globalErrors(String name, Room room, LocalDate date, LocalTime time, String beamerCode, 
    		int beamerCheck, double price, Category category, List<Speaker> speakers) throws Exception 
	{
		// Arrange
		// =======
		Event invalidEvent = createEvent(name, room, date, time, beamerCode, beamerCheck, price, category, speakers);
		
		// Act & Assert
		// ============
        mockMvc.perform(post("/saveevent")
        .flashAttr("newevent", invalidEvent)
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("save-event"))
        .andExpect(model().attributeHasErrors("newevent"));
	}
	
	private static Stream<Arguments> invalidSpeakerData() {
	    return Stream.of(
	        // empty
	        Arguments.of(new Speaker("", "Jansen", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "Jansen", ""), new String[]{"speakers[0].organization"}),
	        // spaces
	        Arguments.of(new Speaker("     ", "Jansen", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "      ", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "Jansen", "     "), new String[]{"speakers[0].organization"}),
	        // tabs
	        Arguments.of(new Speaker("\t", "Jansen", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "\t", "TechCorp"), new String[]{"speakers[0].organization"}),
	        Arguments.of(new Speaker("Jan", "Jansen", "\t"), new String[]{"speakers[0].organization"}),
	        // digits
	        Arguments.of(new Speaker("123", "Jansen", "TechCorp"), new String[]{"speakers[0].firstname"}),
	        Arguments.of(new Speaker("Jan", "123", "TechCorp"), new String[]{"speakers[0].lastname"}),
	        Arguments.of(new Speaker("Jan", "Jansen", "123"), new String[]{"speakers[0].organization"}),
	        // special characters
	        Arguments.of(new Speaker("Jan!", "Jansen", "TechCorp"), new String[]{"speakers[0].firstname"}),
	        Arguments.of(new Speaker("Jan", "Jan$en", "TechCorp"), new String[]{"speakers[0].lastname"}),
	        Arguments.of(new Speaker("Jan", "Jansen", "Tech@Corp"), new String[]{"speakers[0].organization"}),
	        // text + digits
	        Arguments.of(new Speaker("J4n", "Jansen", "TechCorp"), new String[]{"speakers[0].firstname"}),
	        Arguments.of(new Speaker("Jan", "Jan5en", "TechCorp"), new String[]{"speakers[0].lastname"}),
	        Arguments.of(new Speaker("Jan", "Jansen", "T3chCorp"), new String[]{"speakers[0].organization"})
	    );
	}

	@ParameterizedTest
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@MethodSource("invalidSpeakerData")
	public void testPostRequestInvalidEvent_InvalidSpeaker(Speaker invalidSpeaker, String[] expectedErrors) throws Exception {
	    // Arrange
		// =======
	    List<Speaker> speakers = List.of(invalidSpeaker);
	    Event invalidEvent = createEvent("ValidNaam", room, LocalDate.of(2025, 5, 22), LocalTime.of(9, 0), "1234", 70, 20.00, CYBERSECURITY, speakers);

	    // Act & Assert
	    // ============   
	    mockMvc.perform(post("/saveevent")
	    .flashAttr("newevent", invalidEvent)
	    .with(csrf()))
	    .andExpect(status().isOk())
	    .andExpect(view().name("save-event"))
	    .andExpect(model().attributeHasFieldErrors("newevent", expectedErrors));
	}
}
