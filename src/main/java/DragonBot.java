import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.FileInputStream;

public class DragonBot {
    public static JDA jda;
    public static String prefix = "/";
    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("token.txt"); //Gamer fact: Don't upload your discord token to github lol

        String token = fis.toString();
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new commands());

        //new Scraper();
    }
}