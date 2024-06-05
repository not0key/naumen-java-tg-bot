package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.Question;
import io.proj3ct.SpringNaumenBot.domains.Quiz;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuestionService;
import io.proj3ct.SpringNaumenBot.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.proj3ct.SpringNaumenBot.bot.Constants.HELP;
import static io.proj3ct.SpringNaumenBot.bot.Constants.TEXT_START;

@Component
@RequiredArgsConstructor
public class BotMessageCreator {

    @Autowired
    private final QuizService quizService;

    @Autowired
    private final QuestionService questionService;

    private final Map<Long, Map<Long, Integer>> userAttempts = new HashMap<>();

    /**
     * Создается сообщение для пользователя с текстом приветствия и списком возможных команд бота.
     */
    public MessageToUser createMessageStartWorkBot(long chatId, String name) {
        String answer = "Привет, " + name + ", это телеграм бот для проведения викторин.\n" +
                TEXT_START;
        return new MessageToUser(chatId, answer);
    }

    /**
     * Создается сообщение для пользователя о доступных викторинах.
     */
    public MessageToUser createMessageGetQuizzes(long chatId) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        StringBuilder response = new StringBuilder("Доступные викторины:\n");
        for (Quiz quiz : quizzes) {
            response.append(quiz.getId()).append(". ").append(quiz.getName()).append("\n");
        }
        return new MessageToUser(chatId, response.toString());
    }

    /**
     * Создается сообщение для пользователя о начале викторины.
     */
    public MessageToUser createMessageStartQuiz(long chatId, Long quizId) {
        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
        if (questions.isEmpty()) {
            return new MessageToUser(chatId, "Эта викторина не содержит вопросов.");
        }
        Question question = questions.get(0);
        return new MessageToUser(chatId, "Вопрос: " + question.getQuestionText() + "\nИспользуйте /answer " + question.getId() + " [ваш ответ], чтобы ответить.");
    }

    /**
     * Создается сообщение для отправки ответа на вопрос.
     */
    public MessageToUser createMessageCheckAnswer(long chatId, Long questionId, String userAnswer) {
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return new MessageToUser(chatId, "Недопустимый идентификатор вопроса.");
        }

        userAttempts.putIfAbsent(chatId, new HashMap<>());
        Map<Long, Integer> attemptsForUser = userAttempts.get(chatId);
        attemptsForUser.putIfAbsent(questionId, 0);

        int attempts = attemptsForUser.get(questionId);
        String responseText;

        if (question.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
            responseText = "Верно!";
            attemptsForUser.remove(questionId);
        } else {
            attempts++;
            if (attempts < 3) {
                attemptsForUser.put(questionId, attempts);
                responseText = "Неверно! Попробуйте еще раз. У вас осталось " + (3 - attempts) + " попытки.";
                return new MessageToUser(chatId, responseText);
            } else {
                responseText = "Неверно! Правильный ответ: " + question.getCorrectAnswer();
                attemptsForUser.remove(questionId);
            }
        }

        List<Question> questions = questionService.getQuestionsByQuizId(question.getQuiz().getId());

        int currentQuestionIndex = -1;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(questionId)) {
                currentQuestionIndex = i;
                break;
            }
        }

        if (currentQuestionIndex != -1 && currentQuestionIndex < questions.size() - 1) {
            Question nextQuestion = questions.get(currentQuestionIndex + 1);
            responseText += "\n\nСледующий вопрос: " + nextQuestion.getQuestionText() +
                    "\nИспользуйте /answer " + nextQuestion.getId() + " [ваш ответ], чтобы ответить.";
        } else {
            responseText += "\n\nВы ответили на все вопросы в этой викторине.";
        }

        return new MessageToUser(chatId, responseText);
    }

    /**
     * Создается сообщение для пользователя с текстом о доступных командах бота.
     */
    public MessageToUser createMessageAccessButtons(long chatId) {
        return new MessageToUser(chatId, HELP);
    }

    /**
     * Создается сообщение для пользователя с текстом о недоступной команде.
     */
    public MessageToUser createMessageNotFoundCommand(long chatId) {
        String answer = "Команда не найдена";
        return new MessageToUser(chatId, answer);
    }

    public MessageToUser createMessageInvalidCommand(long chatId, String errorMessage) {
        return new MessageToUser(chatId, "Неверно введена команда: " + errorMessage);
    }
}
