import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalTime;


public class MessageRequest {
    boolean isAnswered = false;
    User targetUser;
    //String message = "NONE";
    MessageReceivedEvent message;
    MessageChannel channel;
}
