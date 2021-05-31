import com.bernardomg.tabletop.dice.history.RollHistory;
import com.bernardomg.tabletop.dice.interpreter.DiceInterpreter;
import com.bernardomg.tabletop.dice.interpreter.DiceRoller;
import com.bernardomg.tabletop.dice.parser.DefaultDiceParser;
import com.bernardomg.tabletop.dice.parser.DiceParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;


public class commands implements EventListener {

    public DiceParser parser;
    public RollHistory rolls;
    public DiceInterpreter<RollHistory> roller;

    public static ArrayList<MessageRequest> messageRequests = new ArrayList<MessageRequest>();

    public commands() {
        parser = new DefaultDiceParser();
        roller = new DiceRoller();
    }

    public static void sendMessage(String msg, MessageChannel c)
    {
        c.sendMessage(msg).queue();
    }

    public static void sendMessage(MessageEmbed msg, MessageChannel c)
    {
        c.sendMessage(msg).queue();
    }

    public static void sendEmbeddedMessage(String msg, MessageChannel c, Color color)
    {
        EmbedBuilder rollInfo = new EmbedBuilder();
        rollInfo.setTitle(msg);
        rollInfo.setColor(color);
        c.sendMessage(rollInfo.build()).queue();
    }
    public static void sendEmbeddedTitleMessage(String title, String msg, MessageChannel c, Color color)
    {
        EmbedBuilder rollInfo = new EmbedBuilder();
        //rollInfo.setTitle(title);
        rollInfo.addField(title, msg, false);
        rollInfo.setColor(color);
        c.sendMessage(rollInfo.build()).queue();
    }

    public int greater(int a, int b)
    {
        if (a > b)
            return a;
        else
            return b;
    }

    public int d20(boolean adv)
    {
        int a;
        int b = -20;

        rolls = parser.parse("1d20", roller);
        a = rolls.getTotalRoll();
        if (adv) {
            rolls = parser.parse("1d20", roller);
            b = rolls.getTotalRoll();
        }
        return greater(a, b);
    }


    @Override
    public void onEvent(GenericEvent event)
    {
        if (event instanceof MessageReceivedEvent)
        {
            MessageReceivedEvent mEvent = (MessageReceivedEvent) event;
            String[] args = mEvent.getMessage().getContentRaw().split(" ");
            MessageChannel channel = mEvent.getChannel();
            for (int i = 0; i < messageRequests.size(); i++) {
                //System.out.println("Checking request...");
                if (messageRequests.get(i).targetUser.getAvatarId().equals(mEvent.getAuthor().getAvatarId())) {
                    if (messageRequests.get(i).channel.getId().equals(mEvent.getChannel().getId())) {
                        //System.out.println("User matches request");
                        if (messageRequests.get(i).isAnswered == false) {
                            //System.out.println("Unanswered request... Answering...");
                            messageRequests.get(i).message = mEvent;
                            messageRequests.get(i).isAnswered = true;
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase(DragonBot.prefix + "help")) { //   Example: /help
                sendMessage("Hey gamer!", channel);
            }
            if (args[0].equalsIgnoreCase(DragonBot.prefix + "roll")) { //   Example: /roll d20

                //sendMessage("Rolling dice...", channel);
                rolls = parser.parse(args[1], roller);
                System.out.println(rolls.getRollResults().iterator().next());

                EmbedBuilder rollInfo = new EmbedBuilder();
                rollInfo.setTitle(mEvent.getMember().getNickname() + " rolled a `" + rolls.getTotalRoll().toString() + "`");
                //StringBuilder sRoles = new StringBuilder();

                //rollInfo.addField(mEvent.getAuthor().getName() + "Rolled", "", false);
                rollInfo.setColor(Color.RED);

                sendMessage(rollInfo.build(), channel);

                //sendMessage(rolls.getTotalRoll().toString(), channel);


            }
            //if (args[0].equalsIgnoreCase(DragonBot.prefix + "beyond")) {
              //  String link = args[1];
                //link = link.replaceAll("https://", "");
                //link = link.replaceAll("http://", "");
                //link = link.replaceAll("www.dndbeyond.com/", "");
                //link = link.replaceAll("www.dndbeyond.com/profile/.*/characters/", "");
                //System.out.println("`" + link + "`");
                //DNDCharacter guy = new DNDCharacter(link, mEvent.getAuthor().getId());
                //guy.printCharacterSheet(channel);
                //mEvent.getMessage().delete().queue();
                //Save.WriteObjectToFile(guy, "saves\\" + mEvent.getAuthor().getId() + ".save");
            //}
            if (args[0].equalsIgnoreCase(DragonBot.prefix + "create")) {
                //mEvent.getAuth
                new createCharacter(mEvent.getAuthor(), mEvent.getChannel());
            }
            if (args[0].equalsIgnoreCase(DragonBot.prefix + "sheet"))
            {
                try {
                    DNDCharacter guy = Save.ReadObjectFromFile("saves\\" + mEvent.getAuthor().getId() + ".save");
                    guy.printCharacterSheet(channel);
                }
                catch (Exception e) {
                    sendEmbeddedMessage("Character not found. Try /beyond <dndbeyond link>", channel, Color.YELLOW);
                }
            }
            if (args[0].equalsIgnoreCase(DragonBot.prefix + "check")) {
                boolean advantage;
                if ((args.length > 2) && (args[2].toLowerCase().equals("advantage") || args[2].toLowerCase().equals("adv")))
                    advantage = true;
                else
                    advantage = false;
                int r = d20(advantage);
                try {
                    DNDCharacter guy = Save.ReadObjectFromFile("saves\\" + mEvent.getAuthor().getId() + ".save");
                    String stat = args[1].toLowerCase();
                    if (stat.equals("strength") || stat.equals("str")) {
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Strength Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.strength) + "` = `" + Integer.toString(guy.strength + r) + "`", channel, Color.BLUE);
                    }
                    else if (stat.equals("dexterity") || stat.equals("dex"))
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Dexterity Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.dexterity) + "` = `" + Integer.toString(guy.dexterity + r) + "`", channel, Color.BLUE);
                    else if (stat.equals("constitution") || stat.equals("con"))
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Constitution Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.constitution) + "` = `" + Integer.toString(guy.constitution + r) + "`", channel, Color.BLUE);
                    else if (stat.equals("intelligence") || stat.equals("int"))
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Intelligence Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.intelligence) + "` = `" + Integer.toString(guy.intelligence + r) + "`", channel, Color.BLUE);
                    else if (stat.equals("wisdom") || stat.equals("wis"))
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Wisdom Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.wisdom) + "` = `" + Integer.toString(guy.wisdom + r) + "`", channel, Color.BLUE);
                    else if (stat.equals("charisma") || stat.equals("char") || stat.equals("cha"))
                        sendEmbeddedTitleMessage(mEvent.getMember().getNickname() + "'s Charisma Check",
                                "`" + Integer.toString(r) + "` + `" + Integer.toString(guy.charisma) + "` = `" + Integer.toString(guy.charisma + r) + "`", channel, Color.BLUE);
                    else
                        sendEmbeddedMessage("Error. Syntax: /check <skill> |advantage|", channel, Color.RED);
                }
                catch (Exception e) {
                    sendEmbeddedMessage("Character not found. Try /beyond <dndbeyond link>", channel, Color.YELLOW);
                }
            }
        }
    }
}
