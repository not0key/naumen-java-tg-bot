package io.proj3ct.SpringNaumenBot.telegram;

import io.proj3ct.SpringNaumenBot.bot.Bot;
import io.proj3ct.SpringNaumenBot.bot.BotConfig;
import io.proj3ct.SpringNaumenBot.bot.BotLogic;
import io.proj3ct.SpringNaumenBot.domains.message.MessageFromUser;
import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;
import io.proj3ct.SpringNaumenBot.services.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    public TelegramBot(BotConfig botConfig, BotLogic botLogic) {
        this.botConfig = botConfig;
        this.botLogic = botLogic;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Это телеграмм бот для проведения онлайн викторин."));
        listOfCommands.add(new BotCommand("/quiz", "Это телеграмм бот для проведения онлайн викторин."));
        listOfCommands.add(new BotCommand("/start_quiz", "Начать викторину."));
        listOfCommands.add(new BotCommand("/answer", "Начать викторину."));
        listOfCommands.add(new BotCommand("/help", "Справка."));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        MessageFromUser message = new MessageFromUser(
                update.getMessage().getChatId(),
                update.getMessage().getText(),
                update.getMessage().getChat().getFirstName()
        );
        MessageToUser messageToUser = botLogic.onUpdateReceived(message);
        if (messageToUser == null) {
            return;
        }
        this.sendMessage(messageToUser);
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
            throw new RuntimeException("Не удалось отправить сообщение. "
                    + e.getMessage(), e);
        }
    }

    private SendMessage convertMessageToUserToSendMessage(MessageToUser messageToUser) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageToUser.getChatId());
        if (messageToUser.getText() != null) sendMessage.setText(messageToUser.getText());
        return sendMessage;
    }

}
