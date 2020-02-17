package ee.ivo.katseyl.service;

import java.util.List;

import ee.ivo.katseyl.data.model.Currency;

public interface CurrencyService {

	List<Currency> findAllCurrencies();

	Currency getCurrencyById(Long id);

	Currency getCurrencyByCode(String code);

}
