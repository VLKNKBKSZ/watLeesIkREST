package nl.watleesik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.watleesik.domain.Account;
import nl.watleesik.repository.AccountRepository;

@RestController
@RequestMapping(path = "account")
@CrossOrigin(origins = "*")
public class AccountController {

	private final AccountRepository accountRepository;

	@Autowired
	public AccountController(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@GetMapping("/account-list")
	public ResponseEntity<List<Account>> getAccountList() {
		return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
	}	
}
