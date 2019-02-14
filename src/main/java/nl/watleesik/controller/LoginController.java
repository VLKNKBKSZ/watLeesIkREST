package nl.watleesik.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import nl.watleesik.api.ApiResponse;
import nl.watleesik.api.JWTAuthenticationResponse;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.Person;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.security.JWTTokenProvider;
import nl.watleesik.service.RegistrationService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
	private final AccountRepository accountRepository;
	private final RegistrationService registrationService;

	@Autowired
	public LoginController(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider,
			AccountRepository accountRepository, RegistrationService registrationService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.accountRepository = accountRepository;
		this.registrationService = registrationService;
	}

	@PostMapping("/login")
	public ResponseEntity<JWTAuthenticationResponse> authenticateAccount(@RequestBody Account account) {
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
		LOG.debug("AuthenticationManager returns {}", authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JWTAuthenticationResponse(token));
	}

	@PostMapping("/account/register")
	public ResponseEntity<ApiResponse<Person>> register(@RequestBody Account account) {
		if (accountRepository.findAccountByEmail(account.getEmail()) != null) {
			return new ResponseEntity<>(new ApiResponse<>(409, "Email address already registered", null), 
					HttpStatus.CONFLICT);
		}
		Person person = registrationService.register(account);

		return new ResponseEntity<>(new ApiResponse<Person>(200, "Account succesfully registered", person), 
				HttpStatus.OK);
	}
}
