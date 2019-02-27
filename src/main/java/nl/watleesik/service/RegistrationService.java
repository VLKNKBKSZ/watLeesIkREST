package nl.watleesik.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nl.watleesik.domain.Account;
import nl.watleesik.domain.Address;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.AddressRepository;
import nl.watleesik.repository.ProfileRepository;

@Service
public class RegistrationService {

	private final ProfileRepository profileRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;

	public RegistrationService(ProfileRepository profileRepository, AddressRepository addressRepository,
			PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
		this.profileRepository = profileRepository;
		this.addressRepository = addressRepository;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
	}

	public Account register(Account account) {
		
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setCreatedOn(LocalDateTime.now());
		account.setUpdatedOn(LocalDateTime.now());
		Address address = new Address();
		addressRepository.save(address);
		Profile profile = new Profile();
		profile.setAddress(address);
		profile.setUpdatedOn(LocalDateTime.now());
		profileRepository.save(profile);
		account.setProfile(profile);
		return accountRepository.save(account);		
	}
}
