package com.trivia.repository;

import com.trivia.domain.ClientAnswer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import javax.persistence.SqlResultSetMapping;

import java.util.List;


/**
 * Spring Data JPA repository for the ClientAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientAnswerRepository extends JpaRepository<ClientAnswer, Long> {

    /*
    @Query(value = "select c.user_id,c.correct,c.jhi_time,c.question_id as answerTime " +
        "from client_answer c " +
        "join question q on c.question_id=q.id " +
        "join trivia_question tq on tq.questions_id=q.id " +
        "where tq.trivias_id= :idTrivia ",nativeQuery = true)
    List<Object[]> findAllTrivia(Pageable pageable, @Param("idTrivia") Long idTrivia);

    @Query(value = "select count(*), 10-count(*), count(*)*10, (10-count(*))*10 " +
        "from client_answer e " +
        "join question q on e.question_id=q.id " +
        "join trivia_question tq on tq.questions_id=q.id " +
        "where tq.trivias_id=:idTrivia " +
        "and  e.correct=1 " +
        "and e.user_id=:idUser " +
        "group by e.user_id;",nativeQuery = true)
    Object[] findClientStat(@Param("idTrivia") Long idTrivia,@Param("idUser") Long idUser);
     */
}
