/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;


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
        List<String> urls_list=new ArrayList<String>();
        try{
        //add the first url to the list
            _urls.add(url);
            if(!_validator.Validate(_urls.peek(),new MyUrl(new URL(_urls.peek().getHost()+"/robots.txt")))) //checks if first url validated
            {
                  return;
            }
            /* run on the list of urls . download the url if it is validated
             * and pop it from the list
             */
            for(int i=0;i<limit && !_urls.isEmpty();i++)
            {
                    MyUrl check=_urls.poll();// pops the first url in list
                    System.out.println("Downloading " + check.getAddress());
                    urls_list=scanUrl(check);//scans the url for links and returns list of links
                    for(int j=0;j<urls_list.size();j++) //run on the list of scanned links
                    {
                        
                        if(_validator.Validate(new MyUrl(new URL(urls_list.get(j))),new MyUrl(new URL(urls_list.get(j)+"/robots.txt"))))
                        {
                            System.out.println("scanned " + urls_list.get(j).toString());
                            _urls.add(new MyUrl(new URL(urls_list.get(j))));
                        }
                        
                    }
                
        }
        }catch(Exception a)
        {
            System.out.println(a.toString());
        }
        
    }
    
    public List<String> scanUrl(MyUrl url)
    {      
        Pattern htmltag;
        Pattern link;
        
        htmltag = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
        link = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
        List<String> links = new ArrayList<String>();
       
        try{
            String builder=url.getString();
            String linkd=new String();
            
            Matcher tagmatch = htmltag.matcher(builder.toString());
            while (tagmatch.find()) {
                String href = tagmatch.group(1); // href
                String linkText = tagmatch.group(2); // link text
                
                Matcher matcher = link.matcher(href);
                while (matcher.find())
                {
                 linkd = matcher.group(1); // link
                 linkd = linkd.replaceAll("'", "");
                 linkd = linkd.replaceAll("\"", "");
                 
                if (valid(linkd)) {
                    links.add(makeAbsolute(url.getAddress(), linkd));  
                }
                
            }  
            }
            return (links); 
        }catch(IOException a){
            System.out.println(a.toString());
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
    if (link.matches("http://.*") || link.matches("https://.*")) {
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
