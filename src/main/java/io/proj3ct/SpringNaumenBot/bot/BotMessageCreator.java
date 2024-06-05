package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.proj3ct.SpringNaumenBot.bot.Constants.HELP;
import static io.proj3ct.SpringNaumenBot.bot.Constants.TEXT_START;

@Component
@RequiredArgsConstructor
public class BotMessageCreator {
    private final Map<String, QuizService> quizRooms = new HashMap<>();

    public String generateRoomId() {
        StringBuilder roomId = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            char randomChar;
            if (random.nextBoolean()) {
                randomChar = (char) (random.nextInt(26) + 'A');
            } else {
                randomChar = (char) (random.nextInt(10) + '0');
            }
            roomId.append(randomChar);
        }

        return roomId.toString();
    }

    /**
     * Создается сообщение для пользователя с текстом приветствия и списком возможных команд бота.
     */
    public MessageToUser createMessageStartWorkBot(long chatId, String name) {
        String answer = "Привет, " + name + ", это телеграм бот для проведения онлайн викторин.\n" +
                TEXT_START;
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
