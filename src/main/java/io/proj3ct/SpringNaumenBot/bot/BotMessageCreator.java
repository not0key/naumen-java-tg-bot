package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.Question;
import io.proj3ct.SpringNaumenBot.domains.Quiz;
import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.domains.UserRating;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuestionService;
import io.proj3ct.SpringNaumenBot.services.QuizService;
import io.proj3ct.SpringNaumenBot.services.UserRatingService;
import io.proj3ct.SpringNaumenBot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
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

    @Autowired
    private final UserRatingService userRatingService;

    @Autowired
    private final UserService userService;

    private final Map<Long, Map<Long, Integer>> userAttempts = new HashMap<>();
    private final Map<Long, Long> userCurrentQuestion = new HashMap<>();
    private final Map<Long, Integer> earnedPoints = new HashMap<>();


    public MessageToUser createMessageStartWorkBot(long chatId, String name) {
        String answer = "Привет, " + name + ", это телеграм бот для проведения викторин.\n" +
                TEXT_START;
        return new MessageToUser(chatId, answer);
    }

    public MessageToUser createMessageGetQuizzes(long chatId) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        StringBuilder response = new StringBuilder("Доступные викторины:\n");
        for (Quiz quiz : quizzes) {
            response.append(quiz.getId()).append(". ").append(quiz.getName()).append("\n");
        }
        return new MessageToUser(chatId, response.toString());
    }

    public MessageToUser createMessageListQuiz(long chatId) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        if (quizzes.isEmpty()) {
            return new MessageToUser(chatId, "Нет доступных викторин.");
        }

        String text = "Выберите викторину:";
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(quiz.getName());
            button.setCallbackData("/start_quiz " + quiz.getId());
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }

        keyboardMarkup.setKeyboard(rows);
        return new MessageToUser(chatId, text, keyboardMarkup);
    }

    public MessageToUser createMessageStartQuiz(long chatId, Long quizId) {
        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
        if (questions.isEmpty()) {
            return new MessageToUser(chatId, "Эта викторина не содержит вопросов.");
        }
        Question question = questions.get(0);
        userCurrentQuestion.put(chatId, question.getId());
        return new MessageToUser(chatId, "Вопрос: " + question.getQuestionText() + "\nВведите ваш ответ в чат.");
    }

    public MessageToUser createMessageCheckAnswer(long chatId, String userAnswer) {
        Long questionId = userCurrentQuestion.get(chatId);
        if (questionId == null) {
            return new MessageToUser(chatId, "Пожалуйста, начните викторину, чтобы ответить на вопросы.");
        }

        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return new MessageToUser(chatId, "Недопустимый идентификатор вопроса.");
        }

        userAttempts.putIfAbsent(chatId, new HashMap<>());
        Map<Long, Integer> attemptsForUser = userAttempts.get(chatId);
        attemptsForUser.putIfAbsent(questionId, 0);

        int attempts = attemptsForUser.get(questionId);
        String responseText;

        int earnedPointsForQuiz = earnedPoints.getOrDefault(chatId, 0);

        if (question.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
            responseText = "Верно!";
            attemptsForUser.remove(questionId);
            userCurrentQuestion.remove(chatId);
            updateRating(chatId, true);
            earnedPoints.put(chatId, earnedPointsForQuiz + 1);
        } else {
            attempts++;
            if (attempts < 3) {
                attemptsForUser.put(questionId, attempts);
                responseText = "Неверно! Попробуйте еще раз. У вас осталось " + (3 - attempts) + " попытки.";
                return new MessageToUser(chatId, responseText);
            } else {
                responseText = "Неверно! Правильный ответ: " + question.getCorrectAnswer();
                attemptsForUser.remove(questionId);
                userCurrentQuestion.remove(chatId);
                updateRating(chatId, false);
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
            userCurrentQuestion.put(chatId, nextQuestion.getId());
            responseText += "\n\nСледующий вопрос: " + nextQuestion.getQuestionText() +
                    "\nВведите ваш ответ в чат.";
        } else {
            responseText += "\n\nВы ответили на все вопросы в этой викторине.";
            responseText += "\n\nЗа прохождение викторины вам начислено " + (earnedPointsForQuiz + 1) + " очков.";
            earnedPoints.remove(chatId);
        }

        return new MessageToUser(chatId, responseText);
    }

    public MessageToUser createMessageRating(long chatId) {
        List<UserRating> topRatings = userRatingService.getTopRatings(10); // Получение топ-10 рейтингов
        StringBuilder response = new StringBuilder("Топ пользователей по рейтингу:\n");
        for (int i = 0; i < topRatings.size(); i++) {
            UserRating rating = topRatings.get(i);
            response.append(i + 1).append(". ").append(rating.getUser().getUsername())
                    .append(" - ").append(rating.getRating()).append(" очков\n");
        }
        return new MessageToUser(chatId, response.toString());
    }

//    public MessageToUser createUserRatingMessage(long chatId, Long userId) {
//        int rating = userRatingService.getUserRating(userId);
//        return new MessageToUser(chatId, "Ваш текущий рейтинг: " + rating);
//    }

    public MessageToUser createMessageAccessButtons(long chatId) {
        return new MessageToUser(chatId, HELP);
    }

    public MessageToUser createMessageNotFoundCommand(long chatId) {
        String answer = "Команда не найдена";
        return new MessageToUser(chatId, answer);
    }

    public MessageToUser createMessageInvalidCommand(long chatId, String errorMessage) {
        return new MessageToUser(chatId, "Неверно введена команда: " + errorMessage);
    }

    private void updateRating(long chatId, boolean isCorrect) {
        User user = userService.getUserByChatId(chatId);
        if (user != null) {
            if (isCorrect) {
                userRatingService.updateUserRating(user.getId(), 1);
            }
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }
}

