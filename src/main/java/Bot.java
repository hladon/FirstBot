
import com.vdurmont.emoji.EmojiParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

                execute(sendOffers(getOffers(),update)); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else if(update.hasCallbackQuery()){
            try{
                SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
                if (update.getCallbackQuery().getData().equals("startSending")){
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("working");
                    execute(message);
                }

            }catch (TelegramApiException e){
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
                .setReplyMarkup(keybord2());
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

    //Creating inline keyboard
    private static InlineKeyboardMarkup keybord2() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<InlineKeyboardButton>();
        InlineKeyboardButton button1=new InlineKeyboardButton();
        button1.setText("Start mailing");
        button1.setCallbackData("startSending");
        InlineKeyboardButton button2=new InlineKeyboardButton();
        button2.setText("Stop mailing");
        button2.setCallbackData("stopSending");
        buttons.add(button1);
        buttons.add(button2);
        List<List<InlineKeyboardButton>> list=new ArrayList<List<InlineKeyboardButton>>();
        list.add(buttons);
        keyboard.setKeyboard(list);
        return keyboard;

    }

    private static SendMessage sendOffers(List<Article> list,Update update){
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(list.get(0).getName());
        return message;
    }
    private static List<Article> getOffers() {
        String url="https://freelance.ua/?orders=web-development&page=1&pc=1";
        List<Article> list=new ArrayList<Article>();
        try{
            Document doc= Jsoup.connect(url).get();
            Elements h1Elements=doc.getElementsByClass("l-project-title");
            h1Elements.forEach(h1element->{
                Element element=h1element.getElementsByTag("a").first();
                String localURL=element.attr("href");
                String name=element.text();
                list.add(new Article(localURL,name));
            });
        }catch (Exception e){
            System.out.println("Something wrong");
            return null;
        }

        return list;
    }

    public String getBotUsername() {
        return "Vitalii23Bot";
    }

    public String getBotToken() {
        return "927149308:AAGUlSpP4ZMC_GzAVXndO1SPTT4diNHj_F8";
    }
}
