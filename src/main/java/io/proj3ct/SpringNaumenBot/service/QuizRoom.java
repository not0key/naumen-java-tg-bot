package io.proj3ct.SpringNaumenBot.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuizRoom {
    private final long creatorId;
    private final Map<Long, Integer> userScores = new HashMap<>();

    public QuizRoom() {
        this.creatorId = 0;
    }

    public QuizRoom(long creatorId) {
        this.creatorId = creatorId;
    }

    public void addParticipant(long userId) {
        userScores.put(userId, 0);
    }

    public Map<Long, Integer> getParticipants() {
        return userScores;
    }
}