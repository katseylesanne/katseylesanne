package ee.ivo.katseyl.web.controller;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import ee.ivo.katseyl.data.model.Account;
import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.AccountService;
import ee.ivo.katseyl.service.CurrencyService;
import ee.ivo.katseyl.service.impl.AccountServiceImpl;
import ee.ivo.katseyl.service.impl.CurrencyServiceImpl;
import ee.ivo.katseyl.web.dto.TransactionDto;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionControllerTest {

	@TestConfiguration
	static class TransactionControllerTestContextConfiguration {
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
	private MockMvc mvc;

	@Autowired
	private AccountService accountService;
	@Autowired
	private CurrencyService currencyService;

	private Account testAccount1;
	private Account testAccount2;

	@BeforeEach
	public void setUp() {
		Currency usdCurrency = currencyService.getCurrencyByCode("USD");
		Currency gbpCurrency = currencyService.getCurrencyByCode("GBP");
		testAccount1 = accountService.addAccount("test_name", BigDecimal.TEN, usdCurrency.getId());
		testAccount2 = accountService.addAccount("test_name_2", BigDecimal.ONE, gbpCurrency.getId());
	}

	@Test
	public void testProcessTransaction() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/transaction")
				.content(asJsonString(TransactionDto.builder().sourceAccountId(testAccount1.getId())
						.destinationAccountId(testAccount2.getId()).balance(new BigDecimal("2.50")).build()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount.id",
						Matchers.equalTo(testAccount1.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.destinationAccount").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.destinationAccount.id",
						Matchers.equalTo(testAccount2.getId().intValue())));
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
