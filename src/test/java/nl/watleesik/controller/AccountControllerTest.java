package nl.watleesik.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import nl.watleesik.domain.Account;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.security.CustomUserDetailsService;
import nl.watleesik.security.JWTAuthenticationEntryPoint;
import nl.watleesik.security.JWTTokenProvider;
import nl.watleesik.service.AccountService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountRepository accountRepo;	
	
	@MockBean
	private AccountService accountService;
	
	// MockBeans required for securityContext, can probably be configured differently, for now this works...
	@MockBean CustomUserDetailsService cudService;
	@MockBean JWTAuthenticationEntryPoint jwtEntryPoint;
	@MockBean JWTTokenProvider jwtProvider;
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testGetAccountList_HappyPath() throws Exception {
		// Prepare
		List<Account> accountList = new ArrayList<>();
		accountList.add(new Account(1L, "email_1"));
		accountList.add(new Account(2L, "email_2"));
		when(accountRepo.findAll()).thenReturn(accountList);
		
		// Execute & verify
		mockMvc.perform(get("/account/list")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.message").value("Accountlijst succesvol opgehaald"))
				.andExpect(jsonPath("$.result.[0].email").value("email_1"))
				.andExpect(jsonPath("$.result.[1].email").value("email_2"));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testGetAccountList_EmptyList() throws Exception {
		// Prepare
		List<Account> accountList = new ArrayList<>();
		when(accountRepo.findAll()).thenReturn(accountList);
		
		// Execute & verify
		MvcResult result = mockMvc.perform(get("/account/list")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andReturn();
		assertEquals(result.getResponse().getErrorMessage(), "Accountlijst is leeg");
	}

}
