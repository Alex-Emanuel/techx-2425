package com.springBoot_IT_Conferentie;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import domain.MyUser;
import domain.Role;
import repository.UserRepository;
import service.ConferenceService;
import service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class FavoriteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserService mockUserService;
	@MockitoBean
	private ConferenceService mockConferenceService;
	@MockitoBean
	private UserRepository mockUserRepository;
	
	private MyUser mockUser;
	
	@BeforeEach
	private void setUp() {
		mockUser = MyUser.builder()
				.username("user")
				.password("kaas")
				.role(Role.USER)
				.build();
	}	
	
	@Test
	void testGetFavoriteEventsNoAccessNotLoggedIn() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/favorites"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetFavoriteEventsWithAdminRole() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/favorites"))
		.andExpect(status().isForbidden());
	}	
	
	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetFavoriteEventsRequest() throws Exception {
		// Arrange
		// =======
		when(mockUserRepository.findByUsername("user"))
			.thenReturn(Optional.of(mockUser));
		when(mockUserService.getFilteredFavoriteEvents(null, null, mockUser))
			.thenReturn(Collections.emptyList());
		when(mockConferenceService.getDateRange()).thenReturn(List.of(
			    LocalDate.of(2025, 5, 22),
			    LocalDate.of(2025, 5, 25)
		));
		
		// Act & Assert
		// ============
		mockMvc.perform(get("/favorites"))
		.andExpect(view().name("favorites"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("eventList"))
		.andExpect(model().attributeExists("categories"))
		.andExpect(model().attributeExists("dates"))
		.andExpect(model().attributeExists("maxFavoEvents"));
	}
	
	@Test
	@WithMockUser(username = "user", roles = { "USER" })
    public void testPostRequestValidEvent() throws Exception {
		// Arrange
		// =======
		when(mockUserRepository.findByUsername("user"))
		    .thenReturn(Optional.of(mockUser));
		doNothing().when(mockUserService).addRemoveFavoriteEvent(1L, mockUser);
		
		// Act & Assert
		// ============
		mockMvc.perform(post("/favorites/favorite")
		.param("eventId", "1")
		.header("referer", "/favorites")
		.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/favorites"));
    }

}
