/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author motimi
 */
public class Crawler {
    
    private LinkedList<MyUrl> _urls;
    private Validator _validator;
    /** Constructor for Crawler Class 
     *  @param validator that validates the url
     * @
     */
    public Crawler(Validator v)
    {
        _validator=v;   
        _urls=new LinkedList<MyUrl>();
    }
    
    public void startCrawl(MyUrl url,int limit)
    {
        try{
        _urls.add(url);
        for(int i=0;i<limit && _urls.isEmpty()==false && i<3;i++)
        {
            if(_validator.Validate(_urls.peek()))
            {
                MyUrl check=_urls.peek();
                scanUrl(check);
                int a =5; 
            }
        }
        }catch(Exception a)
        {
            System.out.println(a.toString());
        }
    }
    
    public void scanUrl(MyUrl url)
    {      
        Pattern htmltag;
        Pattern link;
        
        htmltag = Pattern.compile("<a\\b[^>]*href=\"[^>]*>(.*?)</a>");
        link = Pattern.compile("href=\"[^>]*\">");
        List<String> links = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url.getURL().toString()).openStream()));
            String s;
            StringBuilder builder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                builder.append(s);
            }

            Matcher tagmatch = htmltag.matcher(builder.toString());
            while (tagmatch.find()) {
                Matcher matcher = link.matcher(tagmatch.group());
                matcher.find();
                String linkd = matcher.group().replaceFirst("href=\"", "")
                .replaceFirst("\">", "")
                .replaceFirst("\"[\\s]?target=\"[a-zA-Z_0-9]*", "");
                if (valid(linkd)) {
                    links.add(makeAbsolute(url.getURL().toString(), linkd));
                    System.out.println(links.toString());
                }
            }
            
               
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private boolean valid(String s) {
    if (s.matches("javascript:.*|mailto:.*")) {
      return false;
    }
    return true;
  }

  private String makeAbsolute(String url, String link) {
    if (link.matches("http://.*")) {
      return link;
    }
    if (link.matches("/.*") && url.matches(".*$[^/]")) {
      return url + "/" + link;
    }
    if (link.matches("[^/].*") && url.matches(".*[^/]")) {
      return url + "/" + link;
    }
    if (link.matches("/.*") && url.matches(".*[/]")) {
      return url + link;
    }
    if (link.matches("/.*") && url.matches(".*[^/]")) {
      return url + link;
    }
    throw new RuntimeException("Cannot make the link absolute. Url: " + url
        + " Link " + link);
  }
    
}
