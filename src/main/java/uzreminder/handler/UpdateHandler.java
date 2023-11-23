package uzreminder.handler;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uzreminder.Main;
import uzreminder.messageBuilder.MessageBuilder;
import uzreminder.reminder.Reminder;
import uzreminder.schedule.SchedulerService;
import uzreminder.user.User;
import uzreminder.user.UserRepository;
import uzreminder.user.UserState;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateHandler {
    public UpdateHandler(DefaultAbsSender sender) {
        this.sender = sender;
        this.service = SchedulerService.getInstance(sender);
    }

    private final DefaultAbsSender sender;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final SchedulerService service;

    @SneakyThrows
    public void handle(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        Optional<User> optionalUser = userRepository.findById(chatId);
        User user;
        if (optionalUser.isEmpty()) {
            userRepository.save(chatId);
            user = userRepository.findById(chatId).get();
            sender.execute(MessageBuilder.helloMessage(user.getId()));
        } else {
            user = optionalUser.get();
            switch (user.getUserState()) {
                case MAIN -> {
                    String text = update.getMessage().getText();
                    switch (text) {
                        case "➕ Add reminder" -> {
                            user.setUserState(UserState.ADD_TITLE);
                            SendMessage sendMessage = MessageBuilder.enterReminderTitleMessage(user.getId());
                            sender.execute(sendMessage);
                        }
                        case "⏰ My reminders" -> {
                            SendMessage sendMessage = MessageBuilder.myRemindersMessage(user.getId(), user.getReminders());
                            sender.execute(sendMessage);
                        }
                        default -> {
                            SendMessage sendMessage = MessageBuilder.chooseCommandMessage(user.getId());
                            sender.execute(sendMessage);
                        }
                    }
                }
                case ADD_TITLE -> {
                    String title = update.getMessage().getText();
                    Reminder reminder = new Reminder(null, title, null, false);
                    user.getReminders().add(reminder);
                    user.setUserState(UserState.ADD_DESCRIPTION);
                    sender.execute(MessageBuilder.enterReminderDescriptionMessage(user.getId()));
                }
                case ADD_DESCRIPTION -> {
                    String description = update.getMessage().getText();
                    Reminder reminder = user.getReminders().get(user.getReminders().size() - 1);
                    reminder.setDescription(description);
                    user.setUserState(UserState.ADD_TRIGGER_TIME);
                    sender.execute(MessageBuilder.enterReminderTriggerTimeMessage(user.getId()));
                }
                case ADD_TRIGGER_TIME -> {
                    String triggerTimeString = update.getMessage().getText();
                    try {
                        LocalDateTime triggerTime = LocalDateTime.parse(triggerTimeString, Main.formatter);
                        Reminder reminder = user.getReminders().get(user.getReminders().size() - 1);
                        reminder.setTriggerTime(triggerTime);
                        user.setUserState(UserState.MAIN);
                        sender.execute(MessageBuilder.reminderCreatedMessage(user.getId()));

                        if (triggerTime.isAfter(LocalDateTime.now())) {
                            service.schedule(reminder, user.getId());
                        }

                    } catch (DateTimeException e) {
                        sender.execute(MessageBuilder.enterValidTriggerTimeMessage(user.getId()));
                    }
                }
            }
        }
    }
}
