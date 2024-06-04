package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.QuizRoom;
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
    private final Map<String, QuizRoom> quizRooms = new HashMap<>();

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
     * Создается сообщение для пользователя о создание комнаты.
     */
    public MessageToUser createMessangeCreateRoom(long chatId) {
        String roomId = generateRoomId();
        QuizRoom room = new QuizRoom(chatId);
        quizRooms.put(roomId, room);
        String message = "Комната создана. Идентификатор комнаты: " + roomId;
        return new MessageToUser(chatId, message);
    }

    /**
     *Метод присоединения к комнате.
     */
    public boolean joinRoom(String roomId, long userId) {
        if (quizRooms.containsKey(roomId)) {
            QuizRoom room = quizRooms.get(roomId);
            room.addParticipant(userId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Создается сообщение для пользователя об успешном приосоединении к комнате.
     */
    public MessageToUser createMessageJoinSuccess(long chatId) {
        return new MessageToUser(chatId, "Вы успешно присоединились к комнате.");
    }

    /**
     *Создается сообщение для пользователя об ошибке присоединения к комнате.
     */
    public MessageToUser createMessageJoinError(long chatId) {
        return new MessageToUser(chatId, "Ошибка при присоединении к комнате. Убедитесь, что вы ввели правильный идентификатор комнаты.");
    }

    /**
     * Создается сообщение для пользователя о начале викторины.
     */
    public MessageToUser createMessageStartQuiz(long chatId) {
        return new MessageToUser(chatId, "Викторина началась.");
    }

    /**
     * Создается сообщение для пользователя с текстом о недоступной команде.
     */
    public MessageToUser createMessageNotFoundCommand(long chatId) {
        String answer = "Команда не найдена";
        return new MessageToUser(chatId, answer);
    }
}
