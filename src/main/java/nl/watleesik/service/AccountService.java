package nl.watleesik.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.watleesik.api.ResetRequest;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.Address;
import nl.watleesik.domain.Mail;
import nl.watleesik.domain.PasswordResetToken;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.repository.AddressRepository;
import nl.watleesik.repository.PasswordResetTokenRepository;
import nl.watleesik.repository.ProfileRepository;

@Slf4j
@Service
public class AccountService {

	private final ProfileRepository profileRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final EmailService emailService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;

	public AccountService(ProfileRepository profileRepository, AddressRepository addressRepository,
			PasswordEncoder passwordEncoder, AccountRepository accountRepository, EmailService emailService,
			PasswordResetTokenRepository passwordResetTokenRepository) {
		super();
		this.profileRepository = profileRepository;
		this.addressRepository = addressRepository;
		this.passwordEncoder = passwordEncoder;
		this.accountRepository = accountRepository;
		this.emailService = emailService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
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
//		emailService.sendSimpleMessage(createNewPasswordMail(account));
		return accountRepository.save(account);		
	}
	
	public boolean deleteAccount(Account account) {
		if (account != null) {
			accountRepository.delete(account);
			profileRepository.delete(account.getProfile());
			addressRepository.delete(account.getProfile().getAddress());			
			return true;
		} else return false;		
	}
	
	public void sendResetPasswordMail(Account account) {
		PasswordResetToken token = new PasswordResetToken();
		token.setToken(UUID.randomUUID().toString());
		token.setAccount(account);
		token.setExpireTime(30);
		passwordResetTokenRepository.save(token);
		String resetUrl = "http://localhost:4200/" + "/reset-password/" + token.getToken();
		emailService.sendSimpleMessage(createNewPasswordMail(account, resetUrl));
		log.debug("token: {}", token);
	}
	
	public boolean processPasswordReset(PasswordResetToken token, String newPassword) {
		Account account = token.getAccount();
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
		passwordResetTokenRepository.delete(token);
		return true;
	}
	
	private Mail createNewPasswordMail(Account account, String resetUrl) {
		Mail mail = new Mail();
		mail.setTo(account.getEmail());
		mail.setFrom("watleesik.gmail");
		mail.setSubject("Registratie");
		mail.setContent("Klik op deze link om uw wachtwoord te resetten: " + resetUrl);
		return mail;
	}
}
