package ee.ivo.katseyl.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.service.AccountService;

@Validated
@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping
	public List<Account> getAllAccounts() {
		return accountService.findAllAccounts();
	}

	@GetMapping("/{id}")
	public Account getAccount(@PathVariable Long id) {
		return accountService.getAccountById(id);
	}

	@PostMapping
	public Account addAccount(@RequestBody Account account) {
		return accountService.saveAccount(account);
	}

	@PutMapping("/{id}")
	public Account saveAccount(@RequestBody Account account, @PathVariable Long id) {
		return accountService.saveAccount(account, id);
	}

	@DeleteMapping("/{id}")
	public void deleteAccount(@PathVariable Long id) {
		accountService.deleteAccount(id);
	}

}
