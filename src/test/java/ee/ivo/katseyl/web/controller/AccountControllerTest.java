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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountControllerTest {

	@TestConfiguration
	static class AccountControllerTestContextConfiguration {
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
		Currency currency = currencyService.getCurrencyByCode("EUR");
		testAccount1 = accountService.addAccount("test_name", BigDecimal.ZERO, currency.getId());
		testAccount2 = accountService.addAccount("test_name_2", BigDecimal.ZERO, currency.getId());
	}

	@Test
	public void testGetAccountList() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/account")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].accountName",
						Matchers.containsInAnyOrder(testAccount1.getAccountName(), testAccount2.getAccountName())));
	}

	@Test
	public void testGetAccount() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/account/{id}", testAccount1.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accountName",
						Matchers.equalTo(testAccount1.getAccountName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.equalTo(0.0)));
	}

	@Test
	public void testGetAccount_nonExistentId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/account/{id}", 999999L)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testAddAccount() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/account/")
				.content(asJsonString(Account.builder().accountName("add_account_test").balance(BigDecimal.ONE)
						.currency(Currency.builder().id(1L).build()).build()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accountName", Matchers.equalTo("add_account_test")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.equalTo(1)));
	}

	@Test
	public void testPutAccount() throws Exception {
		testAccount1.setAccountName("new_account_name");

		mvc.perform(MockMvcRequestBuilders.put("/api/account/{id}", testAccount1.getId())
				.content(asJsonString(testAccount1)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(testAccount1.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.accountName",
						Matchers.equalTo(testAccount1.getAccountName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.equalTo(0)));
	}

	@Test
	public void testPutAccount_nonExistentId() throws Exception {
		Account putAccount = Account.builder().id(999999L).accountName("non_existent").balance(BigDecimal.ONE)
				.currency(testAccount1.getCurrency()).build();

		mvc.perform(MockMvcRequestBuilders.put("/api/account/{id}", 999999L).content(asJsonString(putAccount))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testDeleteAccount() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/account/{id}", testAccount1.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testDeleteAccount_nonExistentId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/account/{id}", 999999L)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
