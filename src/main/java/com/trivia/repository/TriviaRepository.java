package com.trivia.repository;

import com.trivia.domain.Trivia;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Trivia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TriviaRepository extends JpaRepository<Trivia, Long> {
    @Query("select distinct trivia from Trivia trivia left join fetch trivia.questions")
    List<Trivia> findAllWithEagerRelationships();

    @Query("select trivia from Trivia trivia left join fetch trivia.questions where trivia.id =:id")
    Trivia findOneWithEagerRelationships(@Param("id") Long id);

}
