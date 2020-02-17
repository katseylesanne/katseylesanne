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
import ee.ivo.katseyl.service.exception.NonExistentIdException;

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
		return accountRepository.findById(id).orElseThrow(() -> new NonExistentIdException(id.toString()));
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
	public Account saveAccount(Account account, Long id) {
		if (account == null || account.getId() == null || id == null || !account.getId().equals(id)) {
			throw new IllegalArgumentException("invalid account " + account + " or id " + id);
		}
		if (!accountRepository.existsById(id)) {
			throw new NonExistentIdException("missing id " + id);
		}
		return accountRepository.save(account);
	}

	@Override
	public List<Account> findAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public void deleteAccount(Long id) {
		if (!accountRepository.existsById(id)) {
			throw new NonExistentIdException("missing id " + id);
		}
		accountRepository.deleteById(id);
	}

	@Override
	public void deleteAccount(Account account) {
		accountRepository.delete(account);
	}
}
