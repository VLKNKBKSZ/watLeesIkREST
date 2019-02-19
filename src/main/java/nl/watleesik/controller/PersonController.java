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
import nl.watleesik.repository.PersonRepository;

@RestController
@RequestMapping(path = "profile")
@CrossOrigin(origins = {"http://localhost:4200"})
public class PersonController {
	
	private final PersonRepository personRepository;

	public PersonController(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@GetMapping("/profile-update/{id}")
	public Profile findPersonById(@PathVariable String id) {
		return personRepository.findById(Long.parseLong(id)).get();
	}
	
	@PutMapping("/profile-update")
	public ResponseEntity<ApiResponse<?>> updatePerson(@RequestBody Profile person) {
		Profile updatedPerson = personRepository.save(person);
		return new ResponseEntity<>(new ApiResponse<Profile>(200, "Profile is updated", updatedPerson), HttpStatus.OK);
	}
}
