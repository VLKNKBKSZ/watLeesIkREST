package nl.watleesik.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	public ProfileController(ProfileRepository profileRepository, AccountRepository accountRepository) {
		this.profileRepository = profileRepository;
		this.accountRepository = accountRepository;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<?>> findProfileById(Principal principal) {
		Account account = accountRepository.findAccountByEmail(principal.getName());
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
