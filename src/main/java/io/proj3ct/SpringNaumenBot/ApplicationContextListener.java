package io.proj3ct.SpringNaumenBot;

import io.proj3ct.SpringNaumenBot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private final TelegramBot telegramBot;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        TelegramBotsApi bot = null;
        try {
            bot = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try {
            bot.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
