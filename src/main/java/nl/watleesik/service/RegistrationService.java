package nl.watleesik.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nl.watleesik.domain.Account;
import nl.watleesik.domain.Person;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.PersonRepository;

@Service
public class RegistrationService {

	private final PersonRepository personRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
		
	public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder,
			AccountRepository accountRepository) {
		this.personRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
	}

	public Account register(Account account) {
		
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setCreatedOn(LocalDateTime.now());
		account.setUpdatedOn(LocalDateTime.now());
		Person person = new Person();
		personRepository.save(person);
		account.setPerson(person);
		return accountRepository.save(account);
		
	}
}
