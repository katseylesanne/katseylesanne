package ee.ivo.katseyl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ivo.katseyl.service.TransactionService;
import ee.ivo.katseyl.web.dto.TransactionDto;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping
	public void processTransaction(@RequestBody TransactionDto transactionDto) {
		transactionService.processTransaction(transactionDto.getSourceAccountId(),
				transactionDto.getDestinationAccountId(), transactionDto.getBalance());
	}

}
