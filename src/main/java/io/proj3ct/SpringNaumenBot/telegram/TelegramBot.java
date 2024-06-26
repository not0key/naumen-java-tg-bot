package io.proj3ct.SpringNaumenBot.telegram;

import io.proj3ct.SpringNaumenBot.bot.Bot;
import io.proj3ct.SpringNaumenBot.bot.BotConfig;
import io.proj3ct.SpringNaumenBot.bot.BotLogic;
import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.UserRatingService;
import io.proj3ct.SpringNaumenBot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot implements Bot {

    private final BotConfig botConfig;
    private final BotLogic botLogic;
    @Autowired
    private final UserService userService;

    public TelegramBot(BotConfig botConfig, BotLogic botLogic, UserService userService) {
        this.botConfig = botConfig;
        this.botLogic = botLogic;
        this.userService = userService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Это телеграмм бот для проведения викторин."));
        listOfCommands.add(new BotCommand("/quiz", "Список доступных тем для викторины."));
        listOfCommands.add(new BotCommand("/start_quiz", "Начать викторину."));
        listOfCommands.add(new BotCommand("/rating", "Рейтинг пользователей."));
        listOfCommands.add(new BotCommand("/help", "Справка."));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getFirstName();

            // Проверка, существует ли пользователь с данным chatId
            User existingUser = userService.getUserByChatId(chatId);
            if (existingUser == null) {
                // Пользователь не существует, регистрируем его
                userService.registerUser(chatId, name);
            }

            MessageFromUser message = new MessageFromUser(chatId, text, name);
            MessageToUser messageToUser = botLogic.onUpdateReceived(message);
            if (messageToUser == null) {
                return;
            }
            if (messageToUser.getReplyMarkup() != null) {
                sendInlineKeyBoardMessage(messageToUser);
            } else {
                this.sendMessage(messageToUser);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            // Получение имени пользователя при колбэке
            String name = callbackQuery.getFrom().getFirstName();

            // Проверка, существует ли пользователь с данным chatId
            User existingUser = userService.getUserByChatId(chatId);
            if (existingUser == null) {
                // Пользователь не существует, регистрируем его
                userService.registerUser(chatId, name);
            }

            MessageFromUser message = new MessageFromUser(chatId, data, name);
            MessageToUser messageToUser = botLogic.onUpdateReceived(message);
            if (messageToUser != null) {
                this.sendMessage(messageToUser);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void sendMessage(MessageToUser message) {
        try {
            execute(convertMessageToUserToSendMessage(message));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Не удалось отправить сообщение."
                    + e.getMessage(), e);
        }
    }

    private SendMessage convertMessageToUserToSendMessage(MessageToUser messageToUser) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageToUser.getChatId());
        if (messageToUser.getText() != null) sendMessage.setText(messageToUser.getText());
        return sendMessage;
    }

    private void sendInlineKeyBoardMessage(MessageToUser messageToUser) {
        SendMessage message = new SendMessage();
        message.setChatId(messageToUser.getChatId());
        message.setText(messageToUser.getText());
        message.setReplyMarkup(messageToUser.getReplyMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Не удалось отправить сообщение с кнопками."
                    + e.getMessage(), e);
        }
    }

}
