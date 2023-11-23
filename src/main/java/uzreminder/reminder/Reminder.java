package uzreminder.reminder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {
    private LocalDateTime triggerTime;
    private String title;
    private String description;
    private boolean isFinished;
}
