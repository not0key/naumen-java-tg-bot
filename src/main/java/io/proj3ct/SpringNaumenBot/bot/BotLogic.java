package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static io.proj3ct.SpringNaumenBot.bot.Constants.*;

@Component
@RequiredArgsConstructor
public class BotLogic {

    private final BotMessageCreator botMessageCreator;
    private final Map<Long, String> activeCommands = new HashMap<>();


    public MessageToUser onUpdateReceived(MessageFromUser messageFromUser) {
        if (messageFromUser.getMessage() == null || messageFromUser.getMessage().isEmpty()) {
            return null;
        }
        String messageText = messageFromUser.getMessage();
        long chatId = messageFromUser.getChatId();
        String activeCommand = activeCommands.get(chatId);

        if (activeCommand != null) {
            if (activeCommand.equals(COMMAND_JOIN)) {
                String roomId = messageFromUser.getMessage().trim();
                if (botMessageCreator.joinRoom(roomId, chatId)) {
                    activeCommands.remove(chatId);
                    return botMessageCreator.createMessageJoinSuccess(chatId);
                } else {
                    activeCommands.remove(chatId);
                    return botMessageCreator.createMessageJoinError(chatId);
                }
            }
        }

        switch (messageText) {
            case COMMAND_START -> {
                return botMessageCreator.createMessageStartWorkBot(chatId, messageFromUser.getUserName());
            }
            case COMMAND_CREATE -> {
                return botMessageCreator.createMessangeCreateRoom(chatId);
            }
            case COMMAND_JOIN -> {
                activeCommands.put(chatId, COMMAND_JOIN);
                return new MessageToUser(chatId, "Введите идентификатор комнаты:");
            }
            case COMMAND_STARTQUIZ -> {
                return botMessageCreator.createMessageStartQuiz(chatId);
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
