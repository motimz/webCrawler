/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
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
        //MyUrl url=new MyUrl(new URL("http://www.vogella.com/articles/JavaRegularExpressions/article.html"));
        MyUrl url=new MyUrl(new URL("http://www.google.com"));
        keywords.add("Google");
        // create Crawler and start
        Crawler crawler = new Crawler(new Validator(keywords));
        crawler.startCrawl(url, limit);
        }
        catch(MalformedURLException e)
        {
            
        }
        catch(IOException e)
        {
            
        }
        
        
        
    }
}
