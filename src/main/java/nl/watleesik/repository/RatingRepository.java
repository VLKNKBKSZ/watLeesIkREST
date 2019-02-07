package nl.watleesik.repository;

import nl.watleesik.domain.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository <Rating,Long> {
}
