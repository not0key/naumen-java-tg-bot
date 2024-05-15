package io.proj3ct.SpringNaumenBot.services;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class QuizRoomService {
    private final long creatorId;
    private final Map<Long, Integer> userScores = new HashMap<>();

    public QuizRoomService() {
        this.creatorId = 0;
    }

    public QuizRoomService(long creatorId) {
        this.creatorId = creatorId;
    }

    public void addParticipant(long userId) {
        userScores.put(userId, 0);
    }

    public void removeParticipant(long userId) {
        userScores.remove(userId);
    }

    public Map<Long, Integer> getParticipants() {
        return userScores;
    }

    public int getScore(long userId) {
        return userScores.getOrDefault(userId, 0);
    }

    public void increaseScore(long userId, int points) {
        int currentScore = userScores.getOrDefault(userId, 0);
        userScores.put(userId, currentScore + points);
    }

    public void decreaseScore(long userId, int points) {
        int currentScore = userScores.getOrDefault(userId, 0);
        userScores.put(userId, Math.max(0, currentScore - points));
    }

    public void resetScores() {
        userScores.replaceAll((userId, score) -> 0);
    }

    public boolean hasParticipant(long userId) {
        return userScores.containsKey(userId);
    }

    public int getTotalParticipants() {
        return userScores.size();
    }

    public long getCreatorId() {
        return creatorId;
    }
}
