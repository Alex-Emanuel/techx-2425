package com.springBoot_IT_Conferentie;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

import domain.Category;
import domain.Event;
import domain.Room;
import domain.Speaker;
import service.ConferenceService;

@SpringBootTest
class ConferenceRestControllerTest {

	@Mock
	private ConferenceService mock;
	
	private ConferenceRestController controller;
	private MockMvc mockMvc;

	private final String NAME = "Test";
	private final Room ROOM = new Room(0, "A123", 30);
	private final String INPUT_DATE = "23-05-2025";
	private final LocalDate DATE = LocalDate.of(2025, 5, 23);
	private final LocalTime TIME = LocalTime.of(10, 0);
	private final String BEAMERCODE = "B001";
	private final int BEAMERCHECK = 1;
	private final double PRICE = 25.0;
	private final Category CATEGORY = Category.CYBERSECURITY;
	private final List<Speaker> SPEAKERS = List.of(new Speaker("Alice", "Vermeulen", "InnovateTech"));
	private final String IMAGENAME = "placeholder.jpg";
	private LocalDate expectedDate;
		
	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new ConferenceRestController();
		mockMvc = standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "conferenceService", mock);
	}
	
	private Event anEvent(String name, Room room, int year, int month, int day, LocalTime time, String beamerCode, 
            int beamerCheck, double price, Category category, List<Speaker> speakers) {
		expectedDate = LocalDate.of(year, month, day);
		Event event = new Event(name, room, expectedDate, time, beamerCode, beamerCheck, price, category);
		event.setSpeakers(speakers);
		return event;
	}
	
	private void performRest(String uri) throws Exception {
		mockMvc.perform(get("/rest" + uri))
		.andExpect(status().isOk()) 
		.andExpect(jsonPath("$[0].id").value(0))
		.andExpect(jsonPath("$[0].name").value(NAME))
		.andExpect(jsonPath("$[0].category").value(CATEGORY.name()))
		.andExpect(jsonPath("$[0].date[0]").value(expectedDate.getYear()))
		.andExpect(jsonPath("$[0].date[1]").value(expectedDate.getMonthValue()))
		.andExpect(jsonPath("$[0].date[2]").value(expectedDate.getDayOfMonth()))
		.andExpect(jsonPath("$[0].time[0]").value(TIME.getHour()))
		.andExpect(jsonPath("$[0].time[1]").value(TIME.getMinute()))
		.andExpect(jsonPath("$[0].room").value(ROOM))
		.andExpect(jsonPath("$[0].room.roomcode").value(ROOM.getRoomcode()))
		.andExpect(jsonPath("$[0].beamerCode").value(BEAMERCODE))
		.andExpect(jsonPath("$[0].beamerCheck").value(BEAMERCHECK))
		.andExpect(jsonPath("$[0].price").value(PRICE))
		.andExpect(jsonPath("$[0].speakers").isArray())
		.andExpect(jsonPath("$[0].speakers[0].firstname").value(SPEAKERS.get(0).getFirstname()))
	    .andExpect(jsonPath("$[0].speakers[0].lastname").value(SPEAKERS.get(0).getLastname()))
	    .andExpect(jsonPath("$[0].speakers[0].organization").value(SPEAKERS.get(0).getOrganization()))
		.andExpect(jsonPath("$[0].imageName").value(IMAGENAME));
	}
	
	@ParameterizedTest
	@CsvSource({ "22-05-2025,2025,5,22", "23-05-2025,2025,5,23", "24-05-2025,2025,5,24", "25-05-2025,2025,5,25" })
	void testGetEventsByDate_isOk(String inputDate, int year, int month, int day) throws Exception {
	    Event event = anEvent(NAME, ROOM, year, month, day, TIME, BEAMERCODE, BEAMERCHECK, PRICE, CATEGORY, SPEAKERS);
	    List<Event> events = List.of(event);
	    when(mock.getEventsByDate(expectedDate)).thenReturn(events);
	    performRest("/events/" + inputDate);
	    Mockito.verify(mock).getEventsByDate(expectedDate);
	}
	
	@Test
	public void testGetEventsByDate_emptyList() throws Exception {
		Mockito.when(mock.getEventsByDate(DATE)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/rest/events/" + INPUT_DATE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
		
		Mockito.verify(mock).getEventsByDate(DATE);
	}
	
	@Test
	public void testGetEventsByDate_noEmptyList() throws Exception {
		Event event1 = anEvent(NAME, ROOM, 2025, 5, 23, TIME, BEAMERCODE, BEAMERCHECK, PRICE, CATEGORY, SPEAKERS);
		LocalDate expectedDate1 = expectedDate;
		Event event2 = anEvent("Test2", new Room(0, "B123", 40), 2025, 5, 23, TIME, BEAMERCODE, BEAMERCHECK, PRICE, CATEGORY, SPEAKERS);
		LocalDate expectedDate2 = expectedDate;
		List<Event> listEvent = List.of(event1, event2);
		Mockito.when(mock.getEventsByDate(DATE)).thenReturn(listEvent);

		mockMvc.perform(get("/rest/events/" + INPUT_DATE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].id").value(event1.getId()))
				.andExpect(jsonPath("$[0].name").value(event1.getName()))
				.andExpect(jsonPath("$[0].date[0]").value(expectedDate1.getYear()))
				.andExpect(jsonPath("$[0].date[1]").value(expectedDate1.getMonthValue()))
				.andExpect(jsonPath("$[0].date[2]").value(expectedDate1.getDayOfMonth()))
				.andExpect(jsonPath("$[0].room").value(event1.getRoom()))
				.andExpect(jsonPath("$[1].id").value(event2.getId()))
				.andExpect(jsonPath("$[1].name").value(event2.getName()))
				.andExpect(jsonPath("$[1].date[0]").value(expectedDate2.getYear()))
				.andExpect(jsonPath("$[1].date[1]").value(expectedDate2.getMonthValue()))
				.andExpect(jsonPath("$[1].date[2]").value(expectedDate2.getDayOfMonth()))
				.andExpect(jsonPath("$[1].room").value(event2.getRoom()));
		
		Mockito.verify(mock).getEventsByDate(DATE);
	}
}
