package nl.watleesik.repository;

import nl.watleesik.domain.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole,Long> {

    public AccountRole findAccountRoleByRole(String role);

}
