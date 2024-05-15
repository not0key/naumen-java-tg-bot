package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuizRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.proj3ct.SpringNaumenBot.bot.Constants.HELP;

@Component
@RequiredArgsConstructor
public class BotMessageCreator {
    private final Map<String, QuizRoomService> quizRooms = new HashMap<>();

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
     * Создается сообщение для пользователя о создание комнаты.
     */
    public MessageToUser createMessangeCreateRoom(long chatId) {
        String roomId = UUID.randomUUID().toString();
        QuizRoomService room = new QuizRoomService(chatId); // предположим, что QuizRoom принимает идентификатор создателя комнаты
        quizRooms.put(roomId, room);
        String message = "Комната создана. Идентификатор комнаты: " + roomId;
        return new MessageToUser(chatId, message);
    }

    /**
     * Создается сообщение для пользователя с текстом о недоступной команде.
     */
    public MessageToUser createMessageNotFoundCommand(long chatId) {
        String answer = "Команда не найдена";
        return new MessageToUser(chatId, answer);
    }
}
