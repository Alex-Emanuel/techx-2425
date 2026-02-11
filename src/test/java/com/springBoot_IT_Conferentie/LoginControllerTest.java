package com.springBoot_IT_Conferentie;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserService mockUserService;
	@MockitoBean
	private UserRepository mockUserRepository;
	@MockitoBean
	private ConferenceService mockConferenceService;
	
	private MyUser mockUser;
	private final PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@BeforeEach
	private void setUp() {
		mockUser = MyUser.builder()
				.username("user")
				.password(encoder.encode("kaas"))
				.role(Role.USER)
				.build();
		
		when(mockUserRepository.findByUsername("user")).thenReturn(Optional.of(mockUser));
	}	
	
	@Test
	void loginGet() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
	}
	
	@WithMockUser
	@Test
	void testAccessWithUserRole() throws Exception {
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
		.andExpect(status().isOk());
	}
	
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	@Test
	void testAccessWithAdminRole() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/list"))
		.andExpect(status().isOk())
		.andExpect(view().name("rooms"));
	}
	
	@WithMockUser(username = "user", roles = { "NOT_USER" })
	@Test
	void testNoAccessWithWrongUserRole1() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/favorites"))
		.andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = "user", roles = { "NOT_USER" })
	@Test
	void testNoAccessWithWrongUserRole2() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(get("/rooms/list"))
		.andExpect(status().isForbidden());
	}
	
	@Test
	void testWrongPassword() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(formLogin("/login")
		.user("username", "user")
		.password("password", "wrongpassword"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/login?error"));
	}
	
	@Test
	void testCorrectPassword() throws Exception {
		// Act & Assert
		// ============
		mockMvc.perform(formLogin("/login")
		.user("username", "user")
		.password("password", "kaas"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/events"));
	}

	@ParameterizedTest
	@CsvSource({"/login, login", "/403, error/403"})
	void testGetViews(String url, String expectedViewName) throws Exception {
	    mockMvc.perform(get(url))
	        .andExpect(status().isOk())
	        .andExpect(view().name(expectedViewName));
	}
}
