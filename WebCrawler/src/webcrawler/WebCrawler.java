/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author motimi
 * @author gilmi
 */
public class WebCrawler {

    private static final int NUM_OF_ARGS = 3;
    /**
     * @param args: args[0] = url, args[1] = limit and the rest are keywords
     */
    public static void main(String[] args) {
        // check usage
        /*
        if (args.length < NUM_OF_ARGS)
        {
            System.out.println("usage: java WebCrawler <url> <limit> <keywords>");
            return;
        }
         */
        // create keywords array
        ArrayList<String> keywords = new ArrayList<>();
        /*
        for (int i = 2; i < args.length; i++)
            keywords.add(args[i]);
        */
        try
        {
        int limit =30;
        MyUrl url=new MyUrl(new URL("http://docs.oracle.com/cd/E22289_01/html/821-1274/configuring-the-default-jvm-and-java-arguments.html"));
        keywords.add("walla");
        // create Crawler and start
        Crawler crawler = new Crawler(new Validator(keywords));
        crawler.startCrawl(url, limit);
        }
        catch(MalformedURLException e)
        {
            
        }
        
        
        
    }
}
