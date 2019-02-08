package nl.watleesik.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nl.watleesik.domain.Account;
import nl.watleesik.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private AccountRepository accountRepository;

	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountRepository.findAccountByEmail(username);
		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		return account;
	}
}
