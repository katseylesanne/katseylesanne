package ee.ivo.katseyl.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.AccountService;
import ee.ivo.katseyl.service.TransactionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private AccountService accountService;

	@Override
	public TransactionResult processTransaction(Long sourceAccountId, Long destinationAccountId,
			BigDecimal sourceBalanceChange) {
		if (sourceAccountId == null || destinationAccountId == null || sourceBalanceChange == null) {
			throw new IllegalArgumentException("arguments can not be null");
		}
		if (sourceBalanceChange.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("balance needs to me more than 0");
		}

		Account sourceAccount = accountService.getAccountById(sourceAccountId);
		if (sourceAccount == null) {
			throw new IllegalArgumentException("invalid source account id");
		}

		Account destinationAccount = accountService.getAccountById(destinationAccountId);
		if (destinationAccount == null) {
			throw new IllegalArgumentException("invalid destination account id");
		}

		if (sourceBalanceChange.compareTo(sourceAccount.getBalance()) > 0) {
			throw new IllegalArgumentException("not enough balance on the source account");
		}

		Currency sourceCurrency = sourceAccount.getCurrency();
		Currency destinationCurrency = destinationAccount.getCurrency();

		BigDecimal desetinationBalanceChange = destinationCurrency.getRate()
				.divide(sourceCurrency.getRate(), RoundingMode.HALF_UP).multiply(sourceBalanceChange);

		sourceAccount.setBalance(sourceAccount.getBalance().subtract(sourceBalanceChange));
		destinationAccount.setBalance(destinationAccount.getBalance().add(desetinationBalanceChange));

		sourceAccount = accountService.saveAccount(sourceAccount);
		destinationAccount = accountService.saveAccount(destinationAccount);

		return new TransactionResult(sourceAccount, destinationAccount);
	}

	@Data
	@RequiredArgsConstructor
	public static class TransactionResult {
		private final Account sourceAccount;
		private final Account destinationAccount;
	}
}
