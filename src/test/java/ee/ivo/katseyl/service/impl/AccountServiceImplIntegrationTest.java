package ee.ivo.katseyl.service.impl;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.AccountService;
import ee.ivo.katseyl.service.CurrencyService;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class AccountServiceImplIntegrationTest {

	@TestConfiguration
	static class AccountServiceImplTestContextConfiguration {
		@Bean
		public AccountService accountService() {
			return new AccountServiceImpl();
		}

		@Bean
		public CurrencyService currencyService() {
			return new CurrencyServiceImpl();
		}
	}

	@Autowired
	private AccountService accountService;
	@Autowired
	private CurrencyService currencyService;

	private Account testAccount;

	@BeforeEach
	public void setUp() {
		Currency currency = currencyService.getCurrencyByCode("EUR");
		testAccount = accountService.addAccount("test_name", BigDecimal.ZERO, currency.getId());
	}

	@Test
	public void testGetAccountById() {
		Account foundAccount = accountService.getAccountById(testAccount.getId());
		Assertions.assertEquals("test_name", foundAccount.getAccountName());
		Assertions.assertEquals(0, foundAccount.getBalance().compareTo(BigDecimal.ZERO));
		Assertions.assertEquals("EUR", foundAccount.getCurrency().getCode());
	}

	@Test
	public void testAccountNameUniqueConstraint() {
		Currency currency = currencyService.getCurrencyByCode("EUR");
		accountService.addAccount("unique_account_name", BigDecimal.ZERO, currency.getId());

		Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
			accountService.addAccount("unique_account_name", BigDecimal.ZERO, currency.getId());
		});
	}

	@Test
	public void testEditAccount() {
		testAccount.setAccountName("test_name_2");
		Account savedAccount = accountService.saveAccount(testAccount);
		Assertions.assertEquals("test_name_2", savedAccount.getAccountName());
		Assertions.assertEquals(0, savedAccount.getBalance().compareTo(BigDecimal.ZERO));
		Assertions.assertEquals("EUR", savedAccount.getCurrency().getCode());
	}

	@Test
	public void testDeleteAccount() {
		Long testAccountId = testAccount.getId();
		accountService.deleteAccount(testAccount);
		Assertions.assertNull(accountService.getAccountById(testAccountId));
	}
}
