package nl.watleesik.repository;

import nl.watleesik.domain.Account;
import nl.watleesik.domain.AccountRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends CrudRepository<AccountRole,Long> {
    public Account findAccountRoleByRole(String role);
}
