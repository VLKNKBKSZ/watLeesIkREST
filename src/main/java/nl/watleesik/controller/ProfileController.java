package nl.watleesik.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.ProfileRepository;

@RestController
@RequestMapping(path = "profile")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ProfileController {
	
	private final ProfileRepository profileRepository;

	public ProfileController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@GetMapping("/profile-update/{id}")
	public Profile findProfileById(@PathVariable String id) {
		return profileRepository.findById(Long.parseLong(id)).get();
	}
	
	@PutMapping("/profile-update")
	public ResponseEntity<ApiResponse<?>> updateProfile(@RequestBody Profile profile) {
		Profile updatedProfile = profileRepository.save(profile);
		return new ResponseEntity<>(new ApiResponse<Profile>(200, "Profile is updated", updatedProfile), HttpStatus.OK);
	}
}
