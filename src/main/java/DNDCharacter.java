import com.bernardomg.tabletop.dice.history.RollHistory;
import com.bernardomg.tabletop.dice.interpreter.DiceInterpreter;
import com.bernardomg.tabletop.dice.interpreter.DiceRoller;
import com.bernardomg.tabletop.dice.parser.DefaultDiceParser;
import com.bernardomg.tabletop.dice.parser.DiceParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DNDCharacter implements Serializable {
    public String name;
    public String url;
    public String ID;
    public int speed;
    public int HP;
    public int maxHP;
    public int level;
    public int GP;
    public int strength;
    public int dexterity;
    public int constitution;
    public int intelligence;
    public int wisdom;
    public int charisma;
    public int proficiency;
    public int initiative;
    public int ac;

    /*public DNDCharacter(String link, String ID)
    {
        //R.I.P. DnDBeyond Stats, Gone but not forgotten
        /*url = "https://www.dndbeyond.com/character/" + link + "/json";
        this.ID = ID;
        updateFromJson();
    }*/

    public DNDCharacter() {

    }



    public void updateFromJson()
    {
        JSONObject obj = new JSONObject();
        try {
            obj = new Scraper(url).character;
        } catch (Exception e) {
            System.out.println("ERROR READING JSON");
        }
        try {
            name = obj.get("name").toString();
        }
        catch (Exception e) {
            System.out.println("ERROR FETCHING NAME");
        }
        try {
            wipeStats();
            //System.out.println((obj.getJSONArray("stats").getJSONObject(0).get("value")));
            strength += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(0).get("value").toString()));
            dexterity += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(1).get("value").toString()));
            constitution += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(2).get("value").toString()));
            intelligence += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(3).get("value").toString()));
            wisdom += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(4).get("value").toString()));
            charisma += toBonus(Integer.parseInt(obj.getJSONArray("stats").getJSONObject(5).get("value").toString()));
            maxHP += Integer.parseInt(obj.get("baseHitPoints").toString());
            HP = maxHP; //DONT FORGET TO CHANGE THIS EVENTUALLY... OR MAYBE NOT. I GUESS RETROSPECTIVELY DOING THIS DOESNT MATTER. //IDK REGROUP WHEN FINISHED
            //level = Integer.parseInt(obj.getJSONObject("level").toString());

            JSONObject modifierList = obj.getJSONObject("modifiers");
            //System.out.println("Got modifiers array");
            //JSONArray modifierSub;
            //JSONObject modifier;
            System.out.println("Starting race");
            modScan(modifierList.getJSONArray("race"));
            System.out.println("Starting class");
            modScan(modifierList.getJSONArray("class"));
            System.out.println("Starting item");
            modScan(modifierList.getJSONArray("item"));
            System.out.println("Starting background");
            modScan(modifierList.getJSONArray("background"));
            System.out.println("Starting feat");
            modScan(modifierList.getJSONArray("feat"));
            System.out.println("Starting condition");
            modScan(modifierList.getJSONArray("condition"));
            /*for (int i = 0; i < modifierList.length(); i++)
            {
                modifierSub = modifierList.getJSONArray("race");
                System.out.println("Got race array");
                if (modifierSub.length() == 0)
                    return;
                for (int j = 0; j < modifierSub.length(); j++) {
                    modifier = modifierSub.getJSONObject(0);
                    System.out.println("Got modifiers in race array");
                    if (modifier.get("type").toString().equalsIgnoreCase("bonus")) {
                        int value = Integer.parseInt(modifier.get("value").toString());
                        System.out.println("Got value");
                        String bonusType = modifier.get("subType").toString();
                        System.out.println("Got Bonus Type");
                        switch (bonusType) {
                            case "strength-score":
                                strength += value;
                                break;
                            case "dexterity-score":
                                dexterity += value;
                                break;
                            case "speed":
                                speed += value;
                                break;
                            case "initiative":
                                initiative += value;
                                break;
                            case "constitution-score":
                                constitution += value;
                                break;
                            case "intelligence-score":
                                intelligence += value;
                                break;
                            case "wisdom-score":
                                wisdom += value;
                                break;
                            case "charisma-score":
                                charisma += value;
                                break;
                            default:
                                break;


                        }
                    }
                }
            }*/
        }
        catch (Exception e) {
            System.out.println("ERROR ASSIGNING STATS");
            e.printStackTrace();
        }
        try {
            save();
        }
        catch (Exception e) {
            System.out.println("ERROR SAVING FILE");
        }
    }

    private void modScan(JSONArray ja)
    {
        JSONObject modifier;
        if (ja.length() == 0)
            return;
        for (int j = 0; j < ja.length(); j++) {
            System.out.println("Modifier number: " + j);
            modifier = ja.getJSONObject(j);
            //System.out.println("Got modifiers in race array");
            if (modifier.get("type").toString().equalsIgnoreCase("bonus")) {
                //int value = Integer.parseInt(modifier.get("value").toString());
                //System.out.println("Got value");
                String bonusType = modifier.get("subType").toString();
                //System.out.println("Got Bonus Type");
                switch (bonusType) {
                    case "strength-score":
                        strength += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "dexterity-score":
                        dexterity += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "speed":
                        speed += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "initiative":
                        initiative += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "constitution-score":
                        constitution += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "intelligence-score":
                        intelligence += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "wisdom-score":
                        wisdom += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "charisma-score":
                        charisma += Integer.parseInt(modifier.get("value").toString());
                        break;
                    case "hit-points": //Todo: add a getMaxHP() counter and a hit-dice history and add these to the counter so getHP counts the dice
                        DiceParser parser = new DefaultDiceParser();
                        RollHistory rolls;
                        DiceInterpreter<RollHistory> roller = new DiceRoller();

                        rolls = parser.parse(modifier.getJSONObject("dice").get("diceString").toString(), roller);
                        maxHP += rolls.getTotalRoll();
                    default:
                        break;


                }
            }
        }
    }

    public int toBonus(int a)
    {
        return (int) Math.floor((a - 10.0) / 2.0);
    }

    private void wipeStats()
    {
        HP = 0;
        maxHP = 0;
        level = 0;
        strength = 0;
        dexterity = 0;
        constitution = 0;
        intelligence = 0;
        wisdom = 0;
        charisma = 0;
        proficiency = 0;
        initiative = 10;
        ac = 0;
        speed = 0;
    }

    public void printCharacterSheet(MessageChannel c) {
        EmbedBuilder sheet = new EmbedBuilder();
        sheet.setTitle(name + "'s `Level " + Integer.toString(level) + "` Sheet");

        sheet.addField("__Abilities__",
                String.format("Strength: `" + symbolString(strength) + "`\n" +
                        "Dexterity: `" + symbolString(dexterity) + "`\n" +
                        "Constitution: `" + symbolString(constitution) + "`\n" +
                        "Intelligence: `" + symbolString(intelligence) + "`\n" +
                        "Wisdom: `" + symbolString(wisdom) + "`\n" +
                        "Charisma: `" + symbolString(charisma) + "`"), true);
        sheet.addField("__Stats__",
                String.format("HP: `" + Integer.toString(HP) + "/" + Integer.toString(maxHP) + "`\n" +
                        "Gold: `" + Integer.toString(GP) + "`\n" +
                        "AC: `" + Integer.toString(ac) + "`\n" +
                        "Initiative: `" + symbolString(initiative) + "`"), true);
/*
        sheet.addField("Strength:          `" + symbolString(strength) + "`", "", false);
        sheet.addField("Dexterity:         `" + symbolString(dexterity) + "`", "", false);
        sheet.addField("Constitution:   `" + symbolString(constitution) + "`", "", false);
        sheet.addField("Intelligence:    `" + symbolString(intelligence) + "`", "", false);
        sheet.addField("Wisdom:          `" + symbolString(wisdom) + "`", "", false);
        sheet.addField("Charisma:        `" + symbolString(charisma) + "`", "", false);

 */
        c.sendMessage(sheet.build()).queue();

    }

    public static String symbolString(int a)
    {
        if (a > 0)
            return "+" + Integer.toString(a);
        else if (a < 0)
            return "" + Integer.toString(a);
        else
            return "+0";
    }

    public void save()
    {
        Save.WriteObjectToFile(this, "saves\\" + ID + ".save");
    }
}
