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
import nl.watleesik.domain.Profile;
import nl.watleesik.exceptions.EmailAlreadyInUseException;
import nl.watleesik.exceptions.EmailNotFoundException;
import nl.watleesik.exceptions.ResetTokenExpiredException;
import nl.watleesik.exceptions.ResetTokenInvalidException;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.PasswordResetTokenRepository;
import nl.watleesik.security.JWTTokenProvider;
import nl.watleesik.service.AccountService;

@RestController
@CrossOrigin(origins = "*")
public class LoginController implements IApiResponse {

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
	public ApiResponse<JWTAuthenticationResponse> authenticateAccount(@RequestBody Account account) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);
		Account authenticatedAccount = accountRepository.findAccountByEmail(account.getEmail()).get();
		
		JWTAuthenticationResponse jwtResponse = new JWTAuthenticationResponse(
				token,
				authenticatedAccount.getEmail(),
				authenticatedAccount.getRole());
		authenticatedAccount.setLastLogin(LocalDateTime.now());
		accountRepository.save(authenticatedAccount);
		return createResponse(200, "Authenticatie succesvol", jwtResponse);
	}

	@PostMapping("/account/register")
	public ApiResponse<Profile> register(@RequestBody Account account) throws EmailAlreadyInUseException {
		
		if (accountRepository.findAccountByEmail(account.getEmail()).isPresent()) {
			throw new EmailAlreadyInUseException();
		}
		
		Account savedAccount = accountService.register(account);
		return createResponse(200, "Account succesvol geregistreerd", savedAccount.getProfile());
	}
	
	@PostMapping("/account/forgot")
	public ApiResponse<?> forgotPassword(@RequestBody String email) throws EmailNotFoundException {
		Optional<Account> optionalAccount = accountRepository.findAccountByEmail(email);
		if (!optionalAccount.isPresent()) {
			throw new EmailNotFoundException();
		}
		accountService.sendResetPasswordMail(optionalAccount.get());
		return createResponse(200, "Password reset link sent to email");	
	}
	
	@PostMapping("account/reset")
	public ApiResponse<?> resetPassword(@RequestBody ResetRequest resetRequest) 
			throws ResetTokenExpiredException, ResetTokenInvalidException {
		Optional<PasswordResetToken> token = passwordResetTokenRepository.findByToken(resetRequest.getToken());
		if (token.isPresent()) {
			if (token.get().isExpired()) throw new ResetTokenExpiredException();
			if (accountService.processPasswordReset(token.get(), resetRequest.getPassword())) {
				return createResponse(200, "Wachtwoord is gewijzigd");
			}
		} 
		throw new ResetTokenInvalidException();
	}
}