package nl.watleesik.controller;

import java.time.LocalDateTime;

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
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.security.JWTTokenProvider;
import nl.watleesik.service.AccountService;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
	private final AccountRepository accountRepository;
	private final AccountService accountService;

	@Autowired
	public LoginController(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider,
			AccountRepository accountRepository, AccountService accountService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.accountRepository = accountRepository;
		this.accountService = accountService;
	}

	@PostMapping("/login")
	public ResponseEntity<JWTAuthenticationResponse> authenticateAccount(@RequestBody Account account) {
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		Account authenticatedAccount = accountRepository.findAccountByEmail(account.getEmail());
		JWTAuthenticationResponse jwtResponse = new JWTAuthenticationResponse(
													token, 
													authenticatedAccount.getEmail(), 
													authenticatedAccount.getRole());
		authenticatedAccount.setLastLogin(LocalDateTime.now());
		accountRepository.save(authenticatedAccount);
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/account/register")
	public ResponseEntity<ApiResponse<?>> register(@RequestBody Account account) {
		ApiResponse<?> response;
		if (accountRepository.findAccountByEmail(account.getEmail()) != null) {
			response = new ApiResponse<>(409, "Emailadres bestaat al", null);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
		
		Account savedAccount = accountService.register(account);
		response = new ApiResponse<Profile>(200, "Account succesvol geregistreerd", savedAccount.getProfile());
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
