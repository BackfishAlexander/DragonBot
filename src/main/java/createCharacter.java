import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import Exceptions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class createCharacter implements Runnable {

    public ArrayList<String> deleteMe = new ArrayList<String>();
    MessageChannel channel;
    User owner;
    String username;
    private long timeToLive;

    public createCharacter(User owner, MessageChannel channel, String nickname) {
        this.owner = owner;
        this.channel = channel;
        username = nickname;
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

    private void promptMessage(String message, Color color) {
        EmbedBuilder rollInfo = new EmbedBuilder();
        //rollInfo.setTitle(title);
        rollInfo.addField("Character Creator", message, false);
        rollInfo.setColor(color);
        deleteMe.add(channel.sendMessage(rollInfo.build()).complete().getId());
    }

    private void deleteMessages() {
        for (int i = 0; i < deleteMe.size(); i++) {
            //System.out.println("Deleting message...");
            channel.deleteMessageById(deleteMe.get(i)).queue();
        }
        deleteMe.clear();
    }

    private String prompt(String message, Color color) throws creationError {

        creationError reason = new creationError();

        promptMessage("`" + this.username + "`, " + message, color);

        MessageRequest r = sendMessageRequest();

        while (timeToLive > System.nanoTime()) {
            if (r.isAnswered) {
                updateTime();
                commands.messageRequests.remove(r);
                deleteMe.add(r.message.getMessageId());
                return r.message.getMessage().getContentRaw();
            }
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {

            }
        }
        commands.messageRequests.remove(r);
        reason.reason = "Creation cancelled due to timeout";
        throw reason;
    }

    @Override
    public void run() {
        //System.out.println("Started Thread");
        try {
            deleteMessages();
            String name = prompt("please enter your character's name: ", Color.RED);
            deleteMessages();
            int level = Integer.parseInt(prompt("please enter `" + name + "`'s level: ", Color.YELLOW));
            deleteMessages();
            int maxHP = Integer.parseInt(prompt("please enter `" + name + "`'s maximum HP: ", Color.RED));
            deleteMessages();
            int ac = Integer.parseInt(prompt("please enter `" + name + "`'s armor class: ", Color.ORANGE));
            deleteMessages();
            int strength = Integer.parseInt(prompt("please enter `" + name + "`'s strength bonus: ", Color.DARK_GRAY));
            deleteMessages();
            int dexterity = Integer.parseInt(prompt("please enter `" + name + "`'s dexterity bonus: ", Color.GREEN));
            deleteMessages();
            int constitution = Integer.parseInt(prompt("please enter `" + name + "`'s constitution bonus: ", Color.WHITE));
            deleteMessages();
            int intelligence = Integer.parseInt(prompt("please enter `" + name + "`'s intelligence bonus: ", Color.BLUE));
            deleteMessages();
            int wisdom = Integer.parseInt(prompt("please enter `" + name + "`'s wisdom bonus: ", Color.CYAN));
            deleteMessages();
            int charisma = Integer.parseInt(prompt("please enter `" + name + "`'s charisma bonus: ", Color.CYAN));
            deleteMessages();
            //commands.sendEmbeddedMessage("Thanks, Gamer.", channel, Color.RED);
            //deleteMessages();

            DNDCharacter c = new DNDCharacter();
            c.name = name;
            c.level = level;
            c.maxHP = maxHP;
            c.ac = ac;
            c.HP = maxHP;
            c.strength = strength;
            c.dexterity = dexterity;
            c.constitution = constitution;
            c.intelligence = intelligence;
            c.wisdom = wisdom;
            c.charisma = charisma;
            c.ID = owner.getId();
            c.save();
            c.printCharacterSheet(channel);

        } catch (creationError e) {
            deleteMessages();
            commands.sendEmbeddedMessage(e.reason, channel, Color.RED);
        } catch (Exception e) {
            deleteMessages();
            commands.sendEmbeddedMessage(username + ", Invalid input!", channel, Color.RED);
        }

        //commands.messageRequests.remove(r);
        //System.out.println("Ended thread.");
    }
}
