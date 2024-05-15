package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.proj3ct.SpringNaumenBot.bot.Constants.HELP;

@Component
@RequiredArgsConstructor
public class BotMessageCreator {
    /**
     * Создается сообщение для пользователя с текстом приветствия и списком возможных команд бота.
     */
    public MessageToUser createMessageStartWorkBot(long chatId, String name) {
        String answer = "Привет, " + name + ", это телеграм бот для проведения онлайн викторин.\n";
        return new MessageToUser(chatId, answer);
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
