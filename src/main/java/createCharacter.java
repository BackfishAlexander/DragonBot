import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class createCharacter implements Runnable {

    public static long id = 1;
    MessageChannel channel;
    User owner;
    private long timeToLive;

    public createCharacter(User owner) {
        this.owner = owner;
        this.timeToLive = System.nanoTime() + 30_000_000_000L;
        Thread t = new Thread (this);
        t.start();
    }

    @Override
    public void run() {
        MessageRequest r = new MessageRequest();
        long newID = this.id;
        r.id = newID;
        this.id += 1;
        r.targetUser = this.owner;
        commands.messageRequests.add(r);
        while (timeToLive > System.nanoTime()) {
            MessageRequest r2 = commands.isAnswered(newID);
            if (Objects.nonNull(r2)) {
                System.out.println(r2.message);
                break;
            }
            //else {
            //    System.out.println(r.message);
            //}
        }
        System.out.println("Ended thread...");
    }
}
