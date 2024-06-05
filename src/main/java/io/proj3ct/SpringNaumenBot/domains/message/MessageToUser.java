package io.proj3ct.SpringNaumenBot.domains.message;

public class MessageToUser {
    private final long chatId;
    private final String text;

    public MessageToUser(long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }
}
