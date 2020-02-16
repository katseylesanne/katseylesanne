package ee.ivo.katseyl.service;

import ee.ivo.katseyl.data.model.Currency;

public interface CurrencyService {

	Currency getCurrencyById(Long id);

	Currency getCurrencyByCode(String code);

}
