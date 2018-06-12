package com.trivia.service.dto;

import com.trivia.domain.ClientAnswer;
import com.trivia.domain.User;

import java.util.List;

public class StatDTO {
    private User user;
    private int score=0;
    private int correct=0;
    private int incorrect=0;
    private int answerTime=0;
    private int correctPercent=0;
    private int incorrectPercent=0;

    public StatDTO(User user, int correct, int incorrect, int answerTime, int correctPercent, int incorrectPercent) {
        this.user = user;
        this.correct = correct;
        this.incorrect = incorrect;
        this.answerTime = answerTime;
        this.correctPercent = correctPercent;
        this.incorrectPercent = incorrectPercent;
    }

    public StatDTO() {
    }

    public StatDTO(List<ClientAnswer> clientAnswers,User user) {
        this.user=user;
        for (int i = 0; i < clientAnswers.size(); i++) {
            if (clientAnswers.get(i).getUser().getId()==user.getId()){
                this.answerTime+=clientAnswers.get(i).getTime();
                if (clientAnswers.get(i).isCorrect()){
                    this.correct+=1;
                    if (clientAnswers.get(i).getTime()>=10000){
                        this.score+=8;
                    }else if (clientAnswers.get(i).getTime()>=5000){
                        this.score+=9;
                    }else {
                        this.score+=10;
                    }

                }else{
                    this.incorrect+=1;
                    this.score-=2;
                }
            }
        }
        this.correctPercent=this.incorrect*10;
        this.incorrectPercent=this.incorrect*10;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(int answerTime) {
        this.answerTime = answerTime;
    }

    public int getCorrectPercent() {
        return correctPercent;
    }

    public void setCorrectPercent(int correctPercent) {
        this.correctPercent = correctPercent;
    }

    public int getIncorrectPercent() {
        return incorrectPercent;
    }

    public void setIncorrectPercent(int incorrectPercent) {
        this.incorrectPercent = incorrectPercent;
    }
}
