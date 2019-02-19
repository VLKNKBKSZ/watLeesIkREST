package nl.watleesik.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nl.watleesik.domain.Account;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.ProfileRepository;

@Service
public class RegistrationService {

	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
		
	public RegistrationService(ProfileRepository personRepository, PasswordEncoder passwordEncoder,
			AccountRepository accountRepository) {
		this.profileRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
	}

	public Account register(Account account) {
		
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setCreatedOn(LocalDateTime.now());
		account.setUpdatedOn(LocalDateTime.now());
		Profile profile = new Profile();
		profileRepository.save(profile);
		account.setProfile(profile);
		return accountRepository.save(account);
		
	}
}
