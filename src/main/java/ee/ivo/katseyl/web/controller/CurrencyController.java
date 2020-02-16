package ee.ivo.katseyl.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.service.CurrencyService;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;

	@GetMapping
	public List<Currency> getAllCurrencies() {
		return currencyService.findAllCurrencies();
	}

	@GetMapping("/{id}")
	public Currency getCurrencyById(@PathVariable Long id) {
		return currencyService.getCurrencyById(id);
	}

	@GetMapping("/code/{code}")
	public Currency getCurrencyByCode(@PathVariable String code) {
		return currencyService.getCurrencyByCode(code);
	}

}
