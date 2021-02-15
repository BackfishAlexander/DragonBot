import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.json.JSONObject;

import java.util.Scanner;

public class Scraper {
    public JSONObject character;
    public Scraper(String url) {
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        //client.getOptions().setUseInsecureSSL(true);

        try {
            UnexpectedPage page = client.getPage(url);

            Scanner s = new Scanner(page.getInputStream()).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            character = new JSONObject(result);
            //System.out.println(obj.get("name")); //Gets name
            //System.out.println(obj.getJSONArray("stats").get());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
