package ee.ivo.katseyl.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

	private Long sourceAccountId;
	private Long destinationAccountId;
	private BigDecimal balance;

}
