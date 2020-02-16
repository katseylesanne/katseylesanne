package ee.ivo.katseyl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.ivo.katseyl.data.model.Currency;
import ee.ivo.katseyl.data.repository.CurrencyRepository;
import ee.ivo.katseyl.service.CurrencyService;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public Currency getCurrencyById(Long id) {
		return currencyRepository.findById(id).orElse(null);
	}

	@Override
	public Currency getCurrencyByCode(String code) {
		return currencyRepository.findByCodeIgnoreCase(code);
	}

}
