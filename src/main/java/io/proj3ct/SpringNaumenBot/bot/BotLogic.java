package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuestionService;
import io.proj3ct.SpringNaumenBot.services.QuizService;
import io.proj3ct.SpringNaumenBot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.proj3ct.SpringNaumenBot.bot.Constants.*;

@Component
@RequiredArgsConstructor
public class BotLogic {

    private final BotMessageCreator botMessageCreator;
//    private final UserService userService;
//    private final QuizService quizService;
//    private final QuestionService questionService;


    public MessageToUser onUpdateReceived(MessageFromUser messageFromUser) {
        if (messageFromUser.getMessage() == null || messageFromUser.getMessage().isEmpty()) {
            return null;
        }
        String messageText = messageFromUser.getMessage();
        long chatId = messageFromUser.getChatId();

        switch (messageText) {
            case COMMAND_START -> {
                return botMessageCreator.createMessageStartWorkBot(chatId, messageFromUser.getUserName());
            }
            case COMMAND_QUIZ -> {
                return botMessageCreator.createMessageGetQuizzes(chatId);
           }
            case COMMAND_START_QUIZ -> {
                Long quizId = Long.parseLong(messageText.split(" ")[1]);
                return botMessageCreator.createMessageStartQuiz(chatId, quizId);
            }
            case COMMAND_HELP -> {
                return botMessageCreator.createMessageAccessButtons(chatId);
            }
            default -> {
                return botMessageCreator.createMessageNotFoundCommand(chatId);
            }
        }
    }
}
