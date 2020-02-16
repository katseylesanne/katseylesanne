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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.AccountService;
import ee.ivo.katseyl.service.CurrencyService;
import ee.ivo.katseyl.service.TransactionService;
import ee.ivo.katseyl.service.impl.TransactionServiceImpl.TransactionResult;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceImplIntegrationTest {

	@TestConfiguration
	static class AccountServiceImplTestContextConfiguration {
		@Bean
		public AccountService accountService() {
			return new AccountServiceImpl();
		}

		@Bean
		public TransactionService transactionService() {
			return new TransactionServiceImpl();
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
	@Autowired
	private TransactionService transactionService;

	@BeforeEach
	public void setUp() {
		Currency currencyEur = currencyService.getCurrencyByCode("EUR");
		accountService.addAccount("source_account_1", BigDecimal.TEN, currencyEur.getId());
		accountService.addAccount("destination_account_1", BigDecimal.TEN, currencyEur.getId());

		Currency currencyUsd = currencyService.getCurrencyByCode("USD");
		Currency currencyGbp = currencyService.getCurrencyByCode("GBP");
		accountService.addAccount("source_account_2", BigDecimal.TEN, currencyUsd.getId());
		accountService.addAccount("destination_account_2", BigDecimal.TEN, currencyGbp.getId());
	}

	@Test
	public void testTransaction_sameCurrency_success() {
		Account sourceAccount = accountService.getAccountByName("source_account_1");
		Account destinationAccount = accountService.getAccountByName("destination_account_1");

		TransactionResult transactionResult = transactionService.processTransaction(sourceAccount.getId(),
				destinationAccount.getId(), BigDecimal.ONE);
		sourceAccount = transactionResult.getSourceAccount();
		destinationAccount = transactionResult.getDestinationAccount();

		Assertions.assertEquals(0, sourceAccount.getBalance().compareTo(BigDecimal.valueOf(9)));
		Assertions.assertEquals(0, destinationAccount.getBalance().compareTo(BigDecimal.valueOf(11)));
	}

	@Test
	public void testTransaction_differentCurrency_success() {
		Account sourceAccount = accountService.getAccountByName("source_account_2");
		Account destinationAccount = accountService.getAccountByName("destination_account_2");

		TransactionResult transactionResult = transactionService.processTransaction(sourceAccount.getId(),
				destinationAccount.getId(), BigDecimal.ONE);
		sourceAccount = transactionResult.getSourceAccount();
		destinationAccount = transactionResult.getDestinationAccount();

		Assertions.assertEquals(0, sourceAccount.getBalance().compareTo(BigDecimal.valueOf(9)));
		Assertions.assertEquals(0, destinationAccount.getBalance().compareTo(new BigDecimal("10.76650")));
	}

	@Test
	public void testTransaction_nullSourceAccountId_exception() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(null, 2L, BigDecimal.ONE);
		});
	}

	@Test
	public void testTransaction_nullDestinationAccountId_exception() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(1L, null, BigDecimal.ONE);
		});
	}

	@Test
	public void testTransaction_nullSourceBalanceChange_exception() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(1L, 2L, null);
		});
	}

	@Test
	public void testTransaction_zeroSourceBalanceChange_exception() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(1L, 2L, BigDecimal.ZERO);
		});
	}

	@Test
	public void testTransaction_notExistingSourceAccountId_exception() {
		Account destinationAccount = accountService.getAccountByName("destination_account_1");
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(999999L, destinationAccount.getId(), BigDecimal.ZERO);
		});
	}

	@Test
	public void testTransaction_notExistingDestinationAccountId_exception() {
		Account sourceAccount = accountService.getAccountByName("source_account_1");
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(sourceAccount.getId(), 999999L, BigDecimal.ZERO);
		});
	}

	@Test
	public void testTransaction_notEnoughFundsOnSourceAccount_exception() {
		Account sourceAccount = accountService.getAccountByName("source_account_1");
		Account destinationAccount = accountService.getAccountByName("destination_account_1");
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			transactionService.processTransaction(sourceAccount.getId(), destinationAccount.getId(),
					BigDecimal.valueOf(100L));
		});
	}
}
