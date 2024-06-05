package io.proj3ct.SpringNaumenBot.services;

import io.proj3ct.SpringNaumenBot.domains.Quiz;
import io.proj3ct.SpringNaumenBot.repositories.QuizRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}
