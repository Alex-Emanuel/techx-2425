package com.springBoot_IT_Conferentie;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import service.ConferenceService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class ConferenceControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ConferenceService mockService;

	@Test
	public void testGetEventsRequest() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/events"))
		.andExpect(view().name("events"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("eventList"))
		.andExpect(model().attributeExists("categories"))
		.andExpect(model().attributeExists("dates"))
		.andExpect(model().attributeExists("maxFavoEvents"));
	}
	
	@Test
	public void testLoadEventImage() throws Exception {
		// Arrange
		// =======
		byte[] mockImage = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		ResponseEntity<byte[]> expectedResponse = ResponseEntity
				.ok()
				.header("Content-Type", "image/jpeg")
				.body(mockImage);
		when(mockService.loadEventImage(1L)).thenReturn(expectedResponse);
		
		// Act & Assert
		// ============
		mockMvc.perform(get("/images/events/1"))
		.andExpect(status().isOk())
		.andExpect(header().string("Content-Type", "image/jpeg"))
		.andExpect(content().bytes(mockImage));
	}
}
