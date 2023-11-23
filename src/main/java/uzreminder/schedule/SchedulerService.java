package uzreminder.schedule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uzreminder.messageBuilder.MessageBuilder;
import uzreminder.reminder.Reminder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerService {
    private static SchedulerService service = null;
    private DefaultAbsSender sender;

    public void schedule(Reminder reminder, String chatId) {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        Runnable runnable =()->{
            try {
                sender.execute(MessageBuilder.sendReminderMessage(reminder,chatId));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
        long seconds = Duration.between(LocalDateTime.now(), reminder.getTriggerTime()).getSeconds();
        threadPool.schedule(runnable,seconds, TimeUnit.SECONDS);
    }

    public static SchedulerService getInstance(DefaultAbsSender sender) {
        if (service == null) {
            service = new SchedulerService();
            service.sender = sender;
        }
        return service;
    }
}
