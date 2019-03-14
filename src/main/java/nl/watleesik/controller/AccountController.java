package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Account;
import nl.watleesik.domain.Profile;
import nl.watleesik.exceptions.AccountListEmptyException;
import nl.watleesik.exceptions.AccountNotDeletedException;
import nl.watleesik.exceptions.EmailAlreadyInUseException;
import nl.watleesik.repository.AccountRepository;
import nl.watleesik.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "account")
@CrossOrigin(origins = "*")
public class AccountController implements IApiResponse {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/list")
    public ApiResponse<List<Account>> getAccountList() throws AccountListEmptyException {
    	List<Account> accountList = accountRepository.findAll();
    	if (accountList.isEmpty()) throw new AccountListEmptyException();
    	return createResponse(200, "Accountlijst succesvol opgehaald", accountList);
    }

    @PostMapping("/create")
    public ApiResponse<Profile> createAccount(@RequestBody Account account) throws EmailAlreadyInUseException {
        if (accountRepository.findAccountByEmail(account.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException();
        }
        Account createdAccount = accountService.register(account);
        return createResponse(200, "Account succesvol gecreeerd", createdAccount.getProfile());
    }

    @PostMapping("/delete")
    public ApiResponse<?> deleteAccount(@RequestBody Account account) throws AccountNotDeletedException {
        if (accountService.deleteAccount(account)) {
            return createResponse(200, "Account succesvol verwijderd");
        } else {
            throw new AccountNotDeletedException();
        }
    }
}