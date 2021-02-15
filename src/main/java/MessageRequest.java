import net.dv8tion.jda.api.entities.User;
import java.time.LocalTime;


public class MessageRequest {
    boolean isAnswered = false;
    long id;
    User targetUser;
    String message = "NONE";
    LocalTime startTime;
}
