package io.proj3ct.SpringNaumenBot.domains.message;

public class MessageFromUser {
    private final long chatId;
    private final String message;
    private final String name;

    public MessageFromUser(long chatId, String message, String name) {
        this.chatId = chatId;
        this.message = message;
        this.name = name;
    }

    public long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return name;
    }
}
