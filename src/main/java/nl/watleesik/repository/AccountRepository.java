package nl.watleesik.repository;

import nl.watleesik.domain.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    public Optional<Account> findAccountByEmail(String email);
}
