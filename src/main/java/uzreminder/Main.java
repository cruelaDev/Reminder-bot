package uzreminder;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uzreminder.bot.ReminderBot;

import java.time.format.DateTimeFormatter;

public class Main {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @SneakyThrows
    public static void main(String[] args) {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        ReminderBot reminderBot=new ReminderBot();
        api.registerBot(reminderBot);
    }
}