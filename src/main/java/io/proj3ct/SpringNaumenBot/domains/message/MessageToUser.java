package io.proj3ct.SpringNaumenBot.domains.message;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Data
public class MessageToUser {
    private long chatId;
    private String text;
    private InlineKeyboardMarkup replyMarkup;

    public MessageToUser(long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public MessageToUser(long chatId, String text, InlineKeyboardMarkup replyMarkup) {
        this.chatId = chatId;
        this.text = text;
        this.replyMarkup = replyMarkup;
    }
}
