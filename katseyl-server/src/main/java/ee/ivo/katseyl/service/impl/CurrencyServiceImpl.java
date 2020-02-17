package ee.ivo.katseyl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.data.repository.CurrencyRepository;
import ee.ivo.katseyl.service.CurrencyService;
import ee.ivo.katseyl.service.exception.NonExistentIdException;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public List<Currency> findAllCurrencies() {
		return currencyRepository.findAll();
	}

	@Override
	public Currency getCurrencyById(Long id) {
		return currencyRepository.findById(id).orElseThrow(() -> new NonExistentIdException(id.toString()));
	}

	@Override
	public Currency getCurrencyByCode(String code) {
		return currencyRepository.findByCodeIgnoreCase(code).orElseThrow(() -> new NonExistentIdException(code));
	}

}
