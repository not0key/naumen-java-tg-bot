package io.proj3ct.SpringNaumenBot.bot;

public class Constants {
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_CREATE = "/create";
    public static final String COMMAND_JOIN = "/join";
    public static final String COMMAND_STARTQUIZ = "/startquiz";
    public static final String COMMAND_HELP = "/help";
    public static final String HELP = """
            Справка о доступных командах:
            /create
            /join
            /startquiz
            /help""";
    public static final String TEXT_START = """
            Доступны следующие команды:
            /create - Создать комнату.
            /startquiz - Начать викторину.
            /join - Присоединиться к комнате.
            /help - Справка.
            """;
}
