package nl.watleesik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.Profile;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.service.AccountService;

@RestController
@RequestMapping(path = "account")
@CrossOrigin(origins = "*")
public class AccountController {

	private final AccountRepository accountRepository;
	private final AccountService accountService;

	@Autowired
	public AccountController(AccountRepository accountRepository, AccountService accountService) {
		this.accountRepository = accountRepository;
		this.accountService = accountService;
	}

	@GetMapping("/list")
	public ResponseEntity<List<Account>> getAccountList() {
		return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<?>> createAccount(@RequestBody Account account) {
		ApiResponse<?> response;
		if (accountRepository.findAccountByEmail(account.getEmail()) != null) {
			response = new ApiResponse<>(409, "Emailadres bestaat al", null);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
		Account createdAccount = accountService.register(account);
		response = new ApiResponse<Profile>(200, "Account succesvol gecreeerd", createdAccount.getProfile());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<?>> deleteAccount(@RequestBody Account account) {
		ApiResponse<?> response;
		if (accountService.deleteAccount(account)) {
			response = new ApiResponse<>(200, "Account succesvol verwijderd", null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response = new ApiResponse<>(409, "Kon account niet verwijderen", null);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}
}