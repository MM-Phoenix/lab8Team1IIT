package com.catbot;

import org.fluentd.logger.FluentLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;

public class App extends TelegramLongPollingBot {

    private static final FluentLogger logger = FluentLogger.getLogger("app", "127.0.0.1", 8080);

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            String user = update.getMessage().getFrom().getUserName();
            String chatId = update.getMessage().getChatId().toString();

            Map<String, Object> info = new HashMap<>();
            info.put("level", "INFO");
            info.put("message", "User %s sent message %s".formatted(user, message));
            info.put("service", "my-java-app");

            logger.log("MessageInfo", info);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("Cats are amazing animals 🐱");

            try {
                execute(response);

                SendPhoto photo = new SendPhoto();
                photo.setChatId(chatId);
                photo.setPhoto(new InputFile("https://upload.wikimedia.org/wikipedia/commons/5/5e/Sleeping_cat_on_her_back.jpg"));
                photo.setCaption("Live life as a cat. Sleep, purr, meow 🐈");

                execute(photo);
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("level", "ERROR");
                error.put("message", "Error sending message %s".formatted(e.getMessage()));
                error.put("service", "my-java-app");

                logger.log("MessageInfo", error);
            }
        }
    }

    public String getBotUsername() {
        return "YourCatBot";
    }

    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }

    public static void main(String[] args) throws Exception {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new App());
    }
}
