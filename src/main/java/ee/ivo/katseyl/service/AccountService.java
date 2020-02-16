package ee.ivo.katseyl.service;

import java.math.BigDecimal;
import java.util.List;

import ee.ivo.katseyl.data.model.Account;

public interface AccountService {

	Account addAccount(String accountName, BigDecimal balance, Long currencyId);

	Account getAccountById(Long id);

	Account getAccountByName(String name);

	Account saveAccount(Account account);

	List<Account> findAllAccounts();

	void deleteAccount(Account account);

}
