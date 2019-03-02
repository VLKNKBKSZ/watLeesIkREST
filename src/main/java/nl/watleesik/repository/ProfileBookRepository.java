package nl.watleesik.repository;

import nl.watleesik.domain.ProfileBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileBookRepository extends JpaRepository<ProfileBook, Long> {

    public ProfileBook findById(long d);
}
