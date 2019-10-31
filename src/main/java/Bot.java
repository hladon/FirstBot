
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Bot extends TelegramLongPollingBot {


    private static final ScheduledExecutorService schedule= Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private Long str=null;


    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
            message=generateMessage(update);

            try {
                str=update.getMessage().getChatId();
                Runnable beeper = new Runnable() {
                    private SendMessage mes;
                    public void run() { sendNews(); }
                };
                final ScheduledFuture<?> beeperHandle =
                        scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);


                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private  void sendNews(){
        SendMessage message =new SendMessage()
                .setChatId(str)
                .setText("My update");
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
        return "927149308:AAGUlSpP4ZMC_GzAVXndO1SPTT4diNHj_F8";
    }
}
