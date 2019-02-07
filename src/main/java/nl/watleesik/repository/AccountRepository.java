package nl.watleesik.repository;

import nl.watleesik.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

    public Account findAccountByEmail(String email);
}
