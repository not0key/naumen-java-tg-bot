package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.proj3ct.SpringNaumenBot.bot.Constants.*;

@Component
@RequiredArgsConstructor
public class BotLogic {

    private final BotMessageCreator botMessageCreator;

    public MessageToUser onUpdateReceived(MessageFromUser messageFromUser) {
        if (messageFromUser.getMessage() == null || messageFromUser.getMessage().isEmpty()) {
            return null;
        }
        String messageText = messageFromUser.getMessage();
        long chatId = messageFromUser.getChatId();

        String[] messageParts = messageText.split(" ");
        String command = messageParts[0];

        switch (command) {
            case COMMAND_START -> {
                return botMessageCreator.createMessageStartWorkBot(chatId, messageFromUser.getUserName());
            }
            case COMMAND_QUIZ -> {
                return botMessageCreator.createMessageGetQuizzes(chatId);
            }
            case COMMAND_START_QUIZ -> {
                if (messageParts.length == 1) {
                    return botMessageCreator.createMessageListQuiz(chatId);
                } else {
                    try {
                        Long quizId = Long.parseLong(messageParts[1]);
                        return botMessageCreator.createMessageStartQuiz(chatId, quizId);
                    } catch (NumberFormatException e) {
                        return botMessageCreator.createMessageInvalidCommand(chatId, "Идентификатор викторины должен быть числом.");
                    }
                }
            }
            case COMMAND_HELP -> {
                return botMessageCreator.createMessageAccessButtons(chatId);
            }
            default -> {
                if (command.startsWith("/")) {
                    return botMessageCreator.createMessageNotFoundCommand(chatId);
                } else {
                    return botMessageCreator.createMessageCheckAnswer(chatId, messageText);
                }
            }
        }
    }
}
