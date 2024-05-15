package io.proj3ct.SpringNaumenBot.bot;

import io.proj3ct.SpringNaumenBot.domains.message.MessageToUser;

public interface Bot {
    void sendMessage(MessageToUser message);
}
