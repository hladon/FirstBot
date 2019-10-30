
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {


    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
            message=generateMessage(update);

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    //Checking commands
    private static SendMessage generateMessage(Update update) {
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("My keyboard")
                .setReplyMarkup(keybord1());
        if (text.equals("key1")) {
            message.setText(EmojiParser.parseToUnicode(":fire:"));
        }
        if (text.equals("key2")) {
            message.setText(EmojiParser.parseToUnicode(":poop:"));
        }
        if (text.equals("key3")) {
            message.setText(EmojiParser.parseToUnicode(":alien:"));
        }
        if (text.equals("key4")) {
            message.setText(EmojiParser.parseToUnicode(":wolf:"));
        }
        return message;
    }

    //Creating keyboard
    private static ReplyKeyboardMarkup keybord1() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> buttons = new ArrayList<KeyboardRow>();
        KeyboardRow row = new KeyboardRow();
        row.add("key1");
        row.add("key2");
        buttons.add(row);
        row = new KeyboardRow();
        row.add("key3");
        row.add("key4");
        buttons.add(row);
        keyboard.setKeyboard(buttons);
        return keyboard;

    }

    public String getBotUsername() {
        return "Vitalii23Bot";
    }

    public String getBotToken() {
        return "";
    }
}
