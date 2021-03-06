/**
 * The WebCrawler crawls the web in search of links.
 */
package webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The WebCrawler crawls the web in search of links
 * @author motimi
 * @author gilmi
 */
public class WebCrawler {

    private static final int NUM_OF_ARGS = 3;
    /**
     * creates a Crawler instance and starts to crawl
     * @param args: args[0] = url, args[1] = limit and the rest are keywords
     */
    public static void main(String[] args) {
        // check usage
        
        if (args.length < NUM_OF_ARGS)
        {
            System.err.println("usage: java WebCrawler <url> <limit> <keywords>");
            return;
        }
         
        // create keywords array
        ArrayList<String> keywords = new ArrayList<>();
        
        for (int i = 2; i < args.length; i++)
            keywords.add(args[i]);
        
        try
        {
            int limit = Integer.parseInt(args[1]);
            System.out.println("Maximum number of pages: " + limit);
            MyUrl url=new MyUrl(new URL(args[0]));
            // create Crawler and start
            Crawler crawler = new Crawler(new Validator(keywords));
            crawler.startCrawl(url, limit);
        }
        catch(NumberFormatException e)
        {
            System.err.println("Bad limit number");
        }
        catch(MalformedURLException e)
        {
            System.err.println("Malformed URL: " + args[0]);
        }       
    }
}
