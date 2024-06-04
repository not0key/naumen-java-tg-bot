package io.proj3ct.SpringNaumenBot.services;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class QuizRoom {
    private long chatId;
    private Set<Long> participants;
    private List<Question> questions;
    private boolean isRunning;
    private LocalDateTime startTime;
    private Duration duration;

    public QuizRoom(long chatId) {
        this.chatId = chatId;
        this.participants = new HashSet<>(); // инициализация participants
        this.questions = new ArrayList<>();
        this.isRunning = false;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void startQuiz(Duration duration) {
        this.startTime = LocalDateTime.now();
        this.duration = duration;
        this.isRunning = true;
    }

    public void endQuiz() {
        this.isRunning = false;
    }

    public void addParticipant(long userId) {
        participants.add(userId);
    }

    public List<Long> getParticipants() {
        return new ArrayList<>(participants);
    }

    public boolean isParticipant(long userId) {
        return participants.contains(userId);
    }
}

