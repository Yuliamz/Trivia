package com.trivia.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientAnswer.
 */
@Entity
@Table(name = "client_answer")
public class ClientAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "correct", nullable = false)
    private Boolean correct;

    @NotNull
    @Column(name = "jhi_time", nullable = false)
    private Integer time;

    @ManyToOne
    private Question question;

    @ManyToOne
    private User user;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public ClientAnswer correct(Boolean correct) {
        this.correct = correct;
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Integer getTime() {
        return time;
    }

    public ClientAnswer time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Question getQuestion() {
        return question;
    }

    public ClientAnswer question(Question question) {
        this.question = question;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
        ClientAnswer clientAnswer = (ClientAnswer) o;
        if (clientAnswer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientAnswer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientAnswer{" +
            "id=" + getId() +
            ", correct='" + isCorrect() + "'" +
            ", time=" + getTime() +
            "}";
    }
}
