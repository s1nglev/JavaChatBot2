package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            switch (callbackData) {
                case "start":
                    sendGenderMenu(chatId);
                    break;
                case "men":
                    sendClothesForMen(chatId);
                    break;
                case "women":
                    sendClothesForWomen(chatId);
                    break;
                default:
                    sendMessage(chatId, "Невідома команда. Спробуйте ще раз.");
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start") || messageText.equals("/back") || messageText.equals("Привіт")) {
                sendStartMenu(chatId);
            }
        }
    }

    private void sendStartMenu(long chatId) {
        String text = "Привіт! Я бот який допоможу вам замовити верхній одяг.";
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Почати замовлення");
        startButton.setCallbackData("start");

        rowInline.add(startButton);
        rowsInline.add(rowInline);
        keyboard.setKeyboard(rowsInline);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendGenderMenu(long chatId) {
        String menu = "Оберіть, для кого ви шукаєте одяг:";
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton menButton = new InlineKeyboardButton();
        menButton.setText("Чоловіки");
        menButton.setCallbackData("men");

        InlineKeyboardButton womenButton = new InlineKeyboardButton();
        womenButton.setText("Жінки");
        womenButton.setCallbackData("women");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(menButton);
        rowInline.add(womenButton);
        rowsInline.add(rowInline);

        keyboard.setKeyboard(rowsInline);

        sendMessage(chatId, menu, keyboard);
    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void sendClothesForMen(long chatId) {
        sendProductWithPhoto(chatId,
                "Куртка Чоловіча Зелена – 1 990,00 ₴",
                "https://www.colins.ua/p/kurtka-cholovicha-zelena-cl1055676-35174",
                new InputFile(new File("src/main/resources/photos/men/green_coat_men.png")));

        sendProductWithPhoto(chatId,
                "Куртка Чоловіча Сіра – 3 499,00 ₴",
                "https://www.colins.ua/p/-kurtka-cholovicha-sira-cl1070751-45485",
                new InputFile(new File("src/main/resources/photos/men/gray_coat_men.png")));

        sendProductWithPhoto(chatId,
                "Куртка Чоловіча Чорна – 3 499,00 ₴",
                "https://www.colins.ua/p/-kurtka-cholovicha-chorna-cl1071018-44337",
                new InputFile(new File("src/main/resources/photos/men/black_coat_men.png")));
    }



    private void sendClothesForWomen(long chatId) {
        sendProductWithPhoto(chatId,
                "Куртка Жіноча Чорна – 1 499,00 ₴",
                "https://www.colins.ua/p/-kurtka-zhinocha-chorna-cl1070351-44919",
                new InputFile(new File("src/main/resources/photos/women/black_coat_women.png")));

        sendProductWithPhoto(chatId,
                "Куртка Жіноча Блакитна – 1 999,00 ₴",
                "https://www.colins.ua/p/-kurtka-zhinocha-blakytna-cl1066890-42088",
                new InputFile(new File("src/main/resources/photos/women/blue_coat_women.png")));
    }



    private void sendProductWithPhoto(long chatId, String description, String productUrl, InputFile photoUrl) {
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(String.valueOf(chatId));
        photoMessage.setPhoto(photoUrl);
        photoMessage.setCaption(description);


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton orderButton = new InlineKeyboardButton();
        orderButton.setText("Детальніше");
        orderButton.setUrl(productUrl);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Меню");
        backButton.setCallbackData("start");

        rowInline.add(backButton);
        rowInline.add(orderButton);
        rowsInline.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        photoMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}