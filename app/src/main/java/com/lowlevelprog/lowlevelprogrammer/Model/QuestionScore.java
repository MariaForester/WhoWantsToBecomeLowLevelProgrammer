package com.lowlevelprog.lowlevelprogrammer.Model;


public class QuestionScore {
    private String QuestScore, User, Score, ModeID, ModeName;

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

    public String getModeID() {
        return ModeID;
    }

    public void setModeID(String modeID) {
        ModeID = modeID;
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String modeName) {
        ModeName = modeName;
    }

    public QuestionScore(String questScore, String user, String score, String modeID,
                         String modeName) {
        QuestScore = questScore;
        User = user;
        Score = score;
        ModeID = modeID;
        ModeName = modeName;
    }
}
