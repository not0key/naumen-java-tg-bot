//package io.proj3ct.SpringNaumenBot.service;
//
//import io.proj3ct.SpringNaumenBot.config.BotConfig;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
//import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
//import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
//import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.util.*;
//
//@Component
//public class TelegramBot extends TelegramLongPollingBot {
//
//    private final BotConfig config;
//    private final Map<String, QuizRoom> quizRooms = new HashMap<>();
//    private final Map<Long, String> activeCommands = new HashMap<>();
//    private static final String HELP_TEXT = "Данный бот создан в образовательных целях, на курсе от Naumen.";
//
//    public TelegramBot(BotConfig config) {
//        this.config = config;
//        List<BotCommand> listOfCommands = new ArrayList<>();
//        listOfCommands.add(new BotCommand("/start", "приветствие"));
//        listOfCommands.add(new BotCommand("/create", "создание комнаты"));
//        listOfCommands.add(new BotCommand("/join", "присоединиться к комнате"));
//        listOfCommands.add(new BotCommand("/help", "информация об данном боте"));
//        try {
//            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
//        } catch (TelegramApiException e) {
//        }
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//            switch (messageText) {
//                case "/start" ->
//                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
//                case "/create" ->
//                    createRoom(chatId);
//                case "/join" -> {
//                    if (!activeCommands.containsKey(chatId) || !activeCommands.get(chatId).equals("/join")) {
//                        activeCommands.put(chatId, "/join");
//                        sendResponse(chatId, "Введите идентификатор комнаты:");
//                    }
//                }
//                case "/help" ->
//                    sendMessage(chatId, HELP_TEXT);
//                default -> {
//                    if (activeCommands.containsKey(chatId) && activeCommands.get(chatId).equals("/join")) {
//                        joinRoom(chatId, messageText);
//                        activeCommands.remove(chatId);
//                    } else {
//                        sendMessage(chatId, "Неизвестная команда. Используйте /start для начала.");
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public String getBotUsername() {
//        return config.getBotName();
//    }
//
//    @Override
//    public String getBotToken() {
//        return config.getToken();
//    }
//
//    private void startCommandReceived(long chatId, String name) {
//        String answer = "Привет, " + name + ", приятно познакомиться!";
//        sendMessage(chatId, answer);
//    }
//
//    private void sendMessage(long chatId, String textToSend) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(textToSend);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void createRoom(long creatorId) {
//        String roomId = UUID.randomUUID().toString();
//        QuizRoom room = new QuizRoom(creatorId);
//        quizRooms.put(roomId, room);
//        sendResponse(creatorId, "Комната создана. Идентификатор комнаты: " + roomId);
//    }
//
//    private void joinRoom(long userId, String roomId) {
//        if (quizRooms.containsKey(roomId)) {
//            QuizRoom room = quizRooms.get(roomId);
//            room.addParticipant(userId);
//            sendResponse(userId, "Вы присоединились к комнате с идентификатором " + roomId);
//            sendParticipantsList(userId, room);
//        } else {
//            sendResponse(userId, "Комната с указанным идентификатором не найдена.");
//        }
//    }
//
//    private String getParticipantName(long participantId) {
//        try {
//            GetChatMember getChatMember = new GetChatMember();
//            getChatMember.setChatId(String.valueOf(participantId));
//            getChatMember.setUserId(participantId);
//
//            ChatMember chatMember = execute(getChatMember);
//
//            return chatMember.getUser().getFirstName();
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//            return "Ошибка получения имени";
//        }
//    }
//
//    private void sendParticipantsList(long userId, QuizRoom room) {
//        StringBuilder messageBuilder = new StringBuilder("Список участников в комнате:\n");
//        Map<Long, Integer> participants = room.getParticipants();
//        for (Map.Entry<Long, Integer> entry : participants.entrySet()) {
//            long participantId = entry.getKey();
//            String participantName = getParticipantName(participantId);
//            messageBuilder.append(participantName).append("\n");
//        }
//        sendMessage(userId, messageBuilder.toString());
//    }
//
//    private void sendResponse(long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(text);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//        }
//    }
//}