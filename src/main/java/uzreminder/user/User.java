package uzreminder.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uzreminder.reminder.Reminder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String id;
    private UserState userState;
    private List<Reminder> reminders;
}
