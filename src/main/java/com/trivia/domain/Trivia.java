package com.trivia.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Trivia.
 */
@Entity
@Table(name = "trivia")
public class Trivia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_start", nullable = false)
    private Instant start;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "jhi_level", nullable = false)
    private Integer level;

    @ManyToMany
    @JoinTable(name = "trivia_question",
               joinColumns = @JoinColumn(name="trivias_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="questions_id", referencedColumnName="id"))
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStart() {
        return start;
    }

    public Trivia start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Integer getDuration() {
        return duration;
    }

    public Trivia duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getLevel() {
        return level;
    }

    public Trivia level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Trivia questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Trivia addQuestion(Question question) {
        this.questions.add(question);
        question.getTrivias().add(this);
        return this;
    }

    public Trivia removeQuestion(Question question) {
        this.questions.remove(question);
        question.getTrivias().remove(this);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
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
        Trivia trivia = (Trivia) o;
        if (trivia.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trivia.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trivia{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", duration=" + getDuration() +
            ", level=" + getLevel() +
            "}";
    }
}
