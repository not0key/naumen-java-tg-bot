package io.proj3ct.SpringNaumenBot.services;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String text;
    private List<Answer> answers;

    public Question(String text) {
        this.text = text;
        this.answers = new ArrayList<>();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    // Геттер для текста вопроса
    public String getText() {
        return text;
    }

    // Геттер для списка ответов
    public List<Answer> getAnswers() {
        return answers;
    }
}
