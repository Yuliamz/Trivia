package com.trivia.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "answer_1", nullable = false)
    private String answer1;

    @NotNull
    @Column(name = "answer_2", nullable = false)
    private String answer2;

    @NotNull
    @Column(name = "answer_3", nullable = false)
    private String answer3;

    @NotNull
    @Column(name = "answer_4", nullable = false)
    private String answer4;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "correct_answer", nullable = false)
    private Integer correctAnswer;

    @Column(name = "jhi_time")
    private Integer time;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private Set<ClientAnswer> clientAnswers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "questions",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Trivia> trivias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public Question question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public Question answer1(String answer1) {
        this.answer1 = answer1;
        return this;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public Question answer2(String answer2) {
        this.answer2 = answer2;
        return this;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public Question answer3(String answer3) {
        this.answer3 = answer3;
        return this;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public Question answer4(String answer4) {
        this.answer4 = answer4;
        return this;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public Question correctAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getTime() {
        return time;
    }

    public Question time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Set<ClientAnswer> getClientAnswers() {
        return clientAnswers;
    }

    public Question clientAnswers(Set<ClientAnswer> clientAnswers) {
        this.clientAnswers = clientAnswers;
        return this;
    }

    public Question addClientAnswer(ClientAnswer clientAnswer) {
        this.clientAnswers.add(clientAnswer);
        clientAnswer.setQuestion(this);
        return this;
    }

    public Question removeClientAnswer(ClientAnswer clientAnswer) {
        this.clientAnswers.remove(clientAnswer);
        clientAnswer.setQuestion(null);
        return this;
    }

    public void setClientAnswers(Set<ClientAnswer> clientAnswers) {
        this.clientAnswers = clientAnswers;
    }

    public Set<Trivia> getTrivias() {
        return trivias;
    }

    public Question trivias(Set<Trivia> trivias) {
        this.trivias = trivias;
        return this;
    }

    public Question addTrivia(Trivia trivia) {
        this.trivias.add(trivia);
        trivia.getQuestions().add(this);
        return this;
    }

    public Question removeTrivia(Trivia trivia) {
        this.trivias.remove(trivia);
        trivia.getQuestions().remove(this);
        return this;
    }

    public void setTrivias(Set<Trivia> trivias) {
        this.trivias = trivias;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", answer1='" + getAnswer1() + "'" +
            ", answer2='" + getAnswer2() + "'" +
            ", answer3='" + getAnswer3() + "'" +
            ", answer4='" + getAnswer4() + "'" +
            ", correctAnswer=" + getCorrectAnswer() +
            ", time=" + getTime() +
            "}";
    }
}
