import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class createCharacter implements Runnable {

    MessageChannel channel;
    User owner;
    private long timeToLive;

    public createCharacter(User owner, MessageChannel channel) {
        this.owner = owner;
        this.channel = channel;
        updateTime();
        Thread t = new Thread (this);
        t.start();
    }

    private void updateTime() {
        this.timeToLive = System.nanoTime() + 30_000_000_000L;
    }

    private MessageRequest sendMessageRequest() {
        MessageRequest request = new MessageRequest();
        request.targetUser = this.owner;
        request.channel = this.channel;
        commands.messageRequests.add(request);
        return request;
    }

    @Override
    public void run() {
        //System.out.println("Started Thread");
        MessageRequest r = new MessageRequest();
        r.targetUser = this.owner;
        r.channel = this.channel;
        commands.messageRequests.add(r);
        while (timeToLive > System.nanoTime()) {
            if (r.isAnswered) {
                System.out.println("The user said \"" + r.message.getMessage().toString() + "\"");
                break;
            }
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {

            }
        }
        commands.messageRequests.remove(r);
        //System.out.println("Ended thread.");
    }
}
