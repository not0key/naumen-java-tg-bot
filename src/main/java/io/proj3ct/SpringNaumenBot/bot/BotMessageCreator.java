package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.Question;
import io.proj3ct.SpringNaumenBot.domains.Quiz;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuestionService;
import io.proj3ct.SpringNaumenBot.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.proj3ct.SpringNaumenBot.bot.Constants.HELP;
import static io.proj3ct.SpringNaumenBot.bot.Constants.TEXT_START;

@Component
@RequiredArgsConstructor
public class BotMessageCreator {

    @Autowired
    private final QuizService quizService;

    @Autowired
    private final QuestionService questionService;

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
}
