package nl.watleesik.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import nl.watleesik.repository.BookRepository;
import nl.watleesik.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.ProfileRepository;

@RestController
@RequestMapping(path = "profile")
@CrossOrigin(origins = "*")
public class ProfileController {
	
	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;
	private final BookRepository bookRepository;
	private final BookService bookService;

	@Autowired
	public ProfileController(ProfileRepository profileRepository, AccountRepository accountRepository, BookRepository bookRepository,
							 BookService bookService) {
		this.profileRepository = profileRepository;
		this.accountRepository = accountRepository;
		this.bookRepository	= bookRepository;
		this.bookService = bookService;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<?>> findProfileById(Principal principal) {
		// TODO: HK Adjust for optional
		Account account = accountRepository.findAccountByEmail(principal.getName()).get();
		ApiResponse<?> response = new ApiResponse<Profile>(200, "Profiel is opgehaald", account.getProfile());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<?>> updateProfile(@RequestBody Profile profile, Principal principal) {
		profile.setUpdatedOn(LocalDateTime.now());
		Profile updatedProfile = profileRepository.save(profile);
		ApiResponse<?> response = new ApiResponse<Profile>(200, "Profiel is aangepast", updatedProfile);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}






}
