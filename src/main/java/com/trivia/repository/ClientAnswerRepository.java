package com.trivia.repository;

import com.trivia.domain.ClientAnswer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClientAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientAnswerRepository extends JpaRepository<ClientAnswer, Long> {

}
