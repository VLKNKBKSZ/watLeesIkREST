package nl.watleesik.controller;

import java.time.LocalDateTime;
import java.util.Optional;

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
import nl.watleesik.api.ResetRequest;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.PasswordResetToken;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.PasswordResetTokenRepository;
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
	private final PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	public LoginController(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider,
			AccountRepository accountRepository, AccountService accountService,
			PasswordResetTokenRepository passwordResetTokenRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.accountRepository = accountRepository;
		this.accountService = accountService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<JWTAuthenticationResponse> authenticateAccount(@RequestBody Account account) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);
		Optional<Account> optionalAccount = accountRepository.findAccountByEmail(account.getEmail());
		if (optionalAccount.isPresent()) {
			Account authenticatedAccount = optionalAccount.get();
			JWTAuthenticationResponse jwtResponse = new JWTAuthenticationResponse(
					token,
					authenticatedAccount.getEmail(),
					authenticatedAccount.getRole());
			authenticatedAccount.setLastLogin(LocalDateTime.now());
			accountRepository.save(authenticatedAccount);
			return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
		} else {
			// TODO: check how to handle this situation (if it ever occures)
			return null;
		} 
		
	}

	@PostMapping("/account/register")
	public ResponseEntity<ApiResponse<?>> register(@RequestBody Account account) {
		if (accountRepository.findAccountByEmail(account.getEmail()).isPresent()) {
			return createResponse(409, "Emailadres bestaat al", HttpStatus.CONFLICT);
		}
		
		Account savedAccount = accountService.register(account);
		return createResponse(200, "Account succesvol geregistreerd", savedAccount.getProfile(), HttpStatus.OK);
	}
	
	@PostMapping("/account/forgot")
	public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody String email) {
		Optional<Account> optionalAccount = accountRepository.findAccountByEmail(email);
		if (!optionalAccount.isPresent()) {
			return createResponse(404, "Emailadres niet gevonden", HttpStatus.NOT_FOUND);
		}
		accountService.sendResetPasswordMail(optionalAccount.get());
		return createResponse(200, "Password reset link sent to email", HttpStatus.OK);	
	}
	
	@PostMapping("account/reset")
	public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetRequest resetRequest) {
		Optional<PasswordResetToken> token = passwordResetTokenRepository.findByToken(resetRequest.getToken());
		if (token.isPresent()) {
			if (token.get().isExpired()) {
				return createResponse(403, "Reset token is verlopen", HttpStatus.FORBIDDEN);
			}
			if (accountService.processPasswordReset(token.get(), resetRequest.getPassword())) {
				return createResponse(200, "Wachtwoord is gewijzigd", HttpStatus.OK);
			}
		} 
		return createResponse(403, "Ongeldig token", HttpStatus.FORBIDDEN);
		
	}
	
	private <T> ResponseEntity<ApiResponse<?>> createResponse(int statusCode, String message, T object, HttpStatus httpStatus) {
		ApiResponse<T> response = new ApiResponse<T>(statusCode, message, object);
		return new ResponseEntity<>(response, httpStatus);
	}
	
	private <T> ResponseEntity<ApiResponse<?>> createResponse(int statusCode, String message, HttpStatus httpStatus) {
		return createResponse(statusCode, message, null, httpStatus);
	}
}