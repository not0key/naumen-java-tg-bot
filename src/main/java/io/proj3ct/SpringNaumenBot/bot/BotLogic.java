package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.proj3ct.SpringNaumenBot.bot.Constants.COMMAND_HELP;
import static io.proj3ct.SpringNaumenBot.bot.Constants.COMMAND_START;

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
        switch (messageText) {
            case COMMAND_START -> {
                return botMessageCreator.createMessageStartWorkBot(chatId, messageFromUser.getUserName());
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
