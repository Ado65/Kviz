package com.RMA.kviz;

public class Question {
    private String qustionId;
    private String qustionName;
    private String qustionAnswer;

    public Question() {
    }

    public Question(String qustionId, String qustionName, String qustionAnswer) {
        this.qustionId = qustionId;
        this.qustionName = qustionName;
        this.qustionAnswer = qustionAnswer;
    }

    public String getQustionId() {
        return qustionId;
    }

    public String getQustionName() {
        return qustionName;
    }

    public String getQustionAnswer() {
        return qustionAnswer;
    }
}
