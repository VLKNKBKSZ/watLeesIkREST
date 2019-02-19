package nl.watleesik.repository;

import nl.watleesik.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    public Profile findProfileByName(String name);
}
