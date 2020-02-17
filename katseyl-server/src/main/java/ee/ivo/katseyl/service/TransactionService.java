package ee.ivo.katseyl.service;

import java.math.BigDecimal;

import ee.ivo.katseyl.service.impl.TransactionServiceImpl.TransactionResult;

public interface TransactionService {

	TransactionResult processTransaction(Long sourceAccountId, Long destinationAccountId, BigDecimal balance);

}
