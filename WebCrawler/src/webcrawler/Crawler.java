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
    
    private LinkedList<URL> _urls;
    private Validator _validator;
    /** Constructor for Crawler Class 
     *  @param validator that validates the url
     * @
     */
    public Crawler(Validator v)
    {
        _validator=v;   
        _urls=new LinkedList<URL>();
    }
    
    public void startCrawl(URL url,int limit)
    {
        List<String> urls_list=new ArrayList<String>();
        try{
        //add the first url to the list
            _urls.add(url);
            if(!_validator.Validate(_urls.peek())) //checks if first url validated
            {
                  return;
            }
            /* run on the list of urls . download the url if it is validated
             * and pop it from the list
             */
            for(int i=0;i<limit && !_urls.isEmpty();i++)
            {
                    URL check=_urls.poll();// pops the first url in list
                    System.out.println("Downloading " + check.toString());
                    urls_list=scanUrl(check);//scans the url for links and returns list of links
                    for(int j=0;j<urls_list.size();j++) //run on the list of scanned links
                    {
                        
                        if(_validator.Validate(new URL(urls_list.get(j))))
                        {
                            System.out.println("scanned " + urls_list.get(j).toString());
                            _urls.add(new URL(urls_list.get(j))); 
                        }
                        
                    }
                
        }
        }catch(Exception a)
        {
            System.out.println(a.toString());
        }
        
    }
    
    public List<String> scanUrl(URL url)
    {      
        Pattern htmltag;
        Pattern link;
        
        htmltag = Pattern.compile("<a\\b[^>]*href=\"[^>]*>(.*?)</a>");
        link = Pattern.compile("href=\"[^>]*\">");
        List<String> links = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream()));
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
                    links.add(makeAbsolute(url.toString(), linkd));  
                }
                
            }  
            return (links); 
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
      return (links); 
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
