package ee.ivo.katseyl.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.data.repository.AccountRepository;
import ee.ivo.katseyl.service.AccountService;
import ee.ivo.katseyl.service.CurrencyService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account addAccount(String accountName, BigDecimal balance, Long currencyId) {
		Account account = new Account();
		account.setAccountName(accountName);
		account.setBalance(balance);
		Currency currency = currencyService.getCurrencyById(currencyId);
		account.setCurrency(currency);

		return saveAccount(account);
	}

	@Override
	public Account getAccountById(Long id) {
		return accountRepository.findById(id).orElse(null);
	}

	@Override
	public Account getAccountByName(String name) {
		return accountRepository.findByAccountName(name).orElse(null);
	}

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public List<Account> findAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public void deleteAccount(Account account) {
		accountRepository.delete(account);
	}
}
