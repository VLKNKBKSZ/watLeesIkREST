package nl.watleesik.repository;

import nl.watleesik.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository <Person, Long> {

    public Person findPersonByName(String name);
    public Person findPersonByAccount_Email(String mail);
}
