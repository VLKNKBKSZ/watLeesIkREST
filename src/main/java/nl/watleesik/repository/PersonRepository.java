package nl.watleesik.repository;

import nl.watleesik.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Profile, Long> {

    public Profile findPersonByName(String name);
}
