package uzreminder.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uzreminder.handler.UpdateHandler;

public class ReminderBot extends TelegramLongPollingBot {
    private final UpdateHandler updateHandler = new UpdateHandler(this);
    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handle(update);
    }

    @Override
    public String getBotToken() {
        return "6624837251:AAGoGZsXeidt2tJmI05NLz-wW6uO5RsYC-I";
    }

    @Override
    public String getBotUsername() {
        return " t.me/cruelaReminder_bot";
    }
}
