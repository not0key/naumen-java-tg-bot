package io.proj3ct.SpringNaumenBot.bot;

public class Constants {
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_QUIZ = "/quiz";
    public static final String COMMAND_START_QUIZ = "/start_quiz";
    public static final String COMMAND_RATING = "/rating";
    public static final String COMMAND_HELP = "/help";
    public static final String HELP = """
            Справка о доступных командах:
            /quiz
            /start_quiz
            /help""";
    public static final String TEXT_START = """
            Доступны следующие команды:
            /quiz - Список тем для викторины
            /start_quiz - Начать викторину.
            /help - Справка.
            """;
}
