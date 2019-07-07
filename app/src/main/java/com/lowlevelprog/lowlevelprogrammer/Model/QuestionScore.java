package com.lowlevelprog.lowlevelprogrammer.Model;


public class QuestionScore {
    private String QuestScore, User, Score;

    public QuestionScore() {

    }

    public String getQuestScore() {
        return QuestScore;
    }

    public void setQuestScore(String questScore) {
        QuestScore = questScore;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public QuestionScore(String questScore, String user, String score){
        QuestScore = questScore;
        User = user;
        Score = score;
    }
}
