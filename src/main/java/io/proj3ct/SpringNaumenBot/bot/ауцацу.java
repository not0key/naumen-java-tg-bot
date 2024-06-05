//package com.example.quizbot;
//
//import com.example.quizbot.models.Question;
//import com.example.quizbot.models.Quiz;
//import com.example.quizbot.models.User;
//import com.example.quizbot.services.QuestionService;
//import com.example.quizbot.services.QuizService;
//import com.example.quizbot.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.util.List;
//
//@Component
//public class Bot extends TelegramLongPollingBot {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private QuizService quizService;
//
//    @Autowired
//    private QuestionService questionService;
//
//    @Autowired
//    private BotMessageCreator messageCreator;
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            Long chatId = update.getMessage().getChatId();
//            String username = update.getMessage().getFrom().getUserName();
//
//            User user = userService.getUserByChatId(chatId);
//            if (user == null) {
//                user = new User();
//                user.setUsername(username);
//                user.setChatId(chatId);
//                userService.saveUser(user);
//            }
//
//            String response;
//            if (messageText.equals("/start")) {
//                response = messageCreator.createMessageStartWorkBot(chatId, username).getText();
//            } else if (messageText.equals("/quiz")) {
//                response = getQuizzes();
//            } else if (messageText.startsWith("/startquiz")) {
//                Long quizId = Long.parseLong(messageText.split(" ")[1]);
//                response = startQuiz(quizId);
//            } else if (messageText.startsWith("/answer")) {
//                Long questionId = Long.parseLong(messageText.split(" ")[1]);
//                String answer = messageText.split(" ")[2];
//                response = checkAnswer(questionId, answer);
//            } else if (messageText.equals("/help")) {
//                response = messageCreator.createMessageAccessButtons(chatId).getText();
//            } else {
//                response = messageCreator.createMessageNotFoundCommand(chatId).getText();
//            }
//
//            sendMessage(chatId, response);
//        }
//    }
//
//    private String getQuizzes() {
//        List<Quiz> quizzes = quizService.getAllQuizzes();
//        StringBuilder response = new StringBuilder("Доступные викторины:\n");
//        for (Quiz quiz : quizzes) {
//            response.append(quiz.getId()).append(". ").append(quiz.getName()).append("\n");
//        }
//        return response.toString();
//    }
//
//    private String startQuiz(Long quizId) {
//        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
//        if (questions.isEmpty()) {
//            return "Эта викторина не содержит вопросов.";
//        }
//        Question question = questions.get(0);
//        return "Вопрос: " + question.getQuestionText() + "\nИспользуйте /answer " + question.getId() + " [ваш ответ], чтобы ответить.";
//    }
//
//    private String checkAnswer(Long questionId, String answer) {
//        Question question = questionService.getQuestionById(questionId);
//        if (question == null) {
//            return "Недопустимый идентификатор вопроса.";
//        }
//        if (question.getCorrectAnswer().equalsIgnoreCase(answer)) {
//            return "Верно!";
//        } else {
//            return "Неверно! Правильный ответ: " + question.getCorrectAnswer();
//        }
//    }
//
//    private void sendMessage(Long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId.toString());
//        message.setText(text);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public String getBotUsername() {
//        return "your_telegram_bot_username";
//    }
//
//    @Override
//    public String getBotToken() {
//        return "your_telegram_bot_token";
//    }
//}
//
//    }
