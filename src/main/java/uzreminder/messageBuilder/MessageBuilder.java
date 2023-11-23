package uzreminder.messageBuilder;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uzreminder.Main;
import uzreminder.reminder.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public class MessageBuilder {
    public static SendMessage helloMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Welcome )) Choose a command");
        sendMessage.setReplyMarkup(getMainButtons());
        return sendMessage;
    }

    public static SendMessage chooseCommandMessage(String chatId) {
        return new SendMessage(chatId, "❗\uFE0F Enter a valid command");
    }

    public static SendMessage myRemindersMessage(String chatId, List<Reminder> reminders) {
        StringBuilder builder = new StringBuilder();

        if (reminders.size()==0){
            return new SendMessage(chatId,"❌ You've not reminders yet");
        }
        for (int i = 1; i <= reminders.size(); i++) {
            Reminder reminder = reminders.get(i - 1);
            String status = reminder.getTriggerTime().isAfter(LocalDateTime.now())
                    ? "\uD83D\uDD04 Waiting..."
                    : "✅ Done";

            builder.append("*").append(i).append(". ").append(reminder.getTitle())
                    .append("*").append("\n").append(reminder.getDescription())
                    .append("\n").append(Main.formatter.format(reminder.getTriggerTime())).append(" ").append(status)
                    .append("\n")
                    .append("\n");
        }
        SendMessage sendMessage = new SendMessage(chatId, builder.toString());
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }

    public static SendMessage enterReminderTitleMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "\uD83D\uDCDD Enter reminder's title: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        return sendMessage;
    }

    public static SendMessage enterReminderDescriptionMessage(String chatId) {
        return new SendMessage(chatId, "\uD83D\uDCDD Enter reminder's description: ");
    }

    public static SendMessage enterReminderTriggerTimeMessage(String chatId) {
        return new SendMessage(chatId, "⌛\uFE0F Enter the reminder time (Ex: 30-07-2023 19:45): ");
    }

    public static SendMessage enterValidTriggerTimeMessage(String chatId) {
        return new SendMessage(chatId, "⌛\uFE0F Enter valid reminder time: ");
    }

    public static SendMessage reminderCreatedMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "✅ Reminder created");
        sendMessage.setReplyMarkup(getMainButtons());
        return sendMessage;
    }

    public static SendMessage sendReminderMessage(Reminder reminder, String chatId) {
        return new SendMessage(chatId, """
                %s
                                
                %s
                """.formatted(reminder.getTitle(), reminder.getDescription()));
    }

    private static ReplyKeyboardMarkup getMainButtons() {
        KeyboardButton addReminder = new KeyboardButton("➕ Add reminder");
        KeyboardButton myReminders = new KeyboardButton("⏰ My reminders");

        KeyboardRow row = new KeyboardRow(List.of(addReminder, myReminders));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(row));
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

}
