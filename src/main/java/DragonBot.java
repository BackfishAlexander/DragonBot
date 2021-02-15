import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class DragonBot {
    public static JDA jda;
    public static String prefix = "/";
    public static void main(String[] args) throws Exception {

        jda = JDABuilder.createDefault("Nzc1NTAxMzk3NTA3OTY0OTc5.X6nP9Q.IDQFzQR3YRkKjboiKlwgStqfDlo").build();
        jda.addEventListener(new commands());

        //new Scraper();
    }
}