package ee.ivo.katseyl.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.ivo.katseyl.data.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

	Optional<Currency> findByCodeIgnoreCase(String code);

}
