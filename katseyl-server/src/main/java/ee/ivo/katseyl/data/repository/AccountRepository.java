package ee.ivo.katseyl.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.ivo.katseyl.data.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountName(String accountName);

}
