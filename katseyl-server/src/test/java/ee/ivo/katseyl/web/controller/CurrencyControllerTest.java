package ee.ivo.katseyl.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.CurrencyService;
import ee.ivo.katseyl.service.impl.CurrencyServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CurrencyControllerTest {

	@TestConfiguration
	static class CurrencyControllerTestContextConfiguration {
		@Bean
		public CurrencyService currencyService() {
			return new CurrencyServiceImpl();
		}
	}

	@Autowired
	private MockMvc mvc;

	@Autowired
	private CurrencyService currencyService;

	private List<Currency> allCurrencies;

	@BeforeEach
	public void setUp() {
		allCurrencies = currencyService.findAllCurrencies();
	}

	@Test
	public void testGetAllCurrencies() throws Exception {
		String[] allCurrencyCodes = allCurrencies.stream().map(c -> c.getCode()).collect(Collectors.toList())
				.toArray(new String[allCurrencies.size()]);

		mvc.perform(MockMvcRequestBuilders.get("/api/currency")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].code", Matchers.containsInAnyOrder(allCurrencyCodes)));
	}

	@Test
	public void testGetCurrencyById() throws Exception {
		Assertions.assertTrue(allCurrencies.size() > 0);
		Currency currency = allCurrencies.get(0);

		mvc.perform(MockMvcRequestBuilders.get("/api/currency/{id}", currency.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(currency.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo(currency.getCode())));
	}

	@Test
	public void testGetCurrencyByCode() throws Exception {
		Assertions.assertTrue(allCurrencies.size() > 0);
		Currency currency = allCurrencies.get(0);

		mvc.perform(MockMvcRequestBuilders.get("/api/currency/code/{code}", currency.getCode()))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(currency.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo(currency.getCode())));
	}
}
