package nl.watleesik.repository;

import nl.watleesik.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    public Person findPersonByName(String name);
    public Person findPersonByAccount_Email(String mail);
}
