package io.proj3ct.SpringNaumenBot.services;

import io.proj3ct.SpringNaumenBot.domains.Question;
import io.proj3ct.SpringNaumenBot.domains.Quiz;
import io.proj3ct.SpringNaumenBot.repositories.QuestionRepository;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Getter
    @ManyToOne
    private Quiz quiz;

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
}
