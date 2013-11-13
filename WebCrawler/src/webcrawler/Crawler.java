/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;


import java.io.IOException;
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
    
    final private LinkedList<MyUrl> _urls; //linked list of MyUrl
    final private Validator _validator;//the validator
    /** Constructor for Crawler Class 
     *  @param v that validates the url
     * @
     */
    public Crawler(Validator v)
    {
        _validator=v;   
        _urls=new LinkedList<>();
    }
    
    /** This function starts the crawl
     *  @param url of type MyUrl stands for my first URL 
     *  @param limit stands for the limit of scanned URLs
     */
    public void startCrawl(MyUrl url,int limit)
    {
        try{
            List<MyUrl> urls_list=new ArrayList<MyUrl>();
        //add the first url to the list
            _urls.add(url);
            if(!_validator.Validate(_urls.peek(),new MyUrl(new URL(_urls.peek().getHost()+"/robots.txt")))) //checks if first url validated
            {
                  return;
            }
            /* run on the list of urls . download the url if it is validated
             * and pop it from the list
             */
            for(int numOfScanned=0; numOfScanned < limit && !_urls.isEmpty();)
            {
                MyUrl check=_urls.poll();// pops the first url in list
                System.out.println("Downloading " + check.getAddress());
                urls_list=scanUrl(check);//scans the url for links and returns list of links
                //for(int j=0;j<urls_list.size();j++) //run on the list of scanned links
                for (MyUrl currentUrl : urls_list)
                {
                    if(_validator.Validate(currentUrl,new MyUrl(new URL(currentUrl.getHost()+"/robots.txt"))))
                    {
                        _urls.add(currentUrl);
                        System.out.println(numOfScanned + " scanned " + currentUrl.getAddress());
                        numOfScanned++;
                        if (numOfScanned > limit)
                            break;
                    }

                }
            }
        }catch(MalformedURLException a)
        {
            System.out.println(a.toString());
        }
        
    }
    /** This function scans url and returns a list of the links in it
     *  @param url of type MyUrl stands for a URL
     */
    public List<MyUrl> scanUrl(MyUrl url)
    {      
        Pattern htmltag;
        Pattern link;
        
        htmltag = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
        link = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
        List<MyUrl> links = new ArrayList<MyUrl>();
       
        try{
            String builder=url.getString();
            String linkd;
            
            Matcher tagmatch = htmltag.matcher(builder.toString());
            while (tagmatch.find()) 
            {
                String href = tagmatch.group(1); // href
                
                Matcher matcher = link.matcher(href);
                while (matcher.find())
                {
                 linkd = matcher.group(1); // link
                 linkd = linkd.replaceAll("'", "");
                 linkd = linkd.replaceAll("\"", "");
                 
                if (valid(linkd)) 
                    links.add(new MyUrl(new URL(makeAbsolute(url.getAddress(), linkd)))); 
                    
                
                }  
            }
            return (links); 
        }catch(IOException a){
            System.out.println(a.toString());
        }
        return (links);
    }
    /** This function check if a string is mail or javascript
     *  @param s of type String stands for a string.
     */
    private boolean valid(String s) {
    if (s.matches("javascript:.*|mailto:.*")) {
      return false;
    }
    return true;
  }

    /** This Function creates the link by the way it is given 
     * (path , www. , http) and its domain
     *  @param url of type String stands for the url domain.
     *  @param link of type String stands for the url checked inside the domain.
     */
   //
  private String makeAbsolute(String url, String link) {
    //url= http://google.com link=http://maps.google.co.il/maps?hl=iw&tab=wl   
      try{
    if (link.matches("http://.*") || link.matches("https://.*")) {
      return link;
    }
    
    if (link.matches("/.*") && url.matches(".*$[^/]")) {
      return url + "/" + link;
    }
    //url = http://www.vogella.com/articles/JavaRegularExpressions/article.html link=#top
    if (link.matches("[^/].*") && url.matches(".*[^/]")) {
      return url + "/" + link;
    }
    
    if (link.matches("/.*") && url.matches(".*[/]")) {
      return url + link;
    }
    //link = /search?    url = http://www.google.com
    if (link.matches("/.*") && url.matches(".*[^/]")) {
      return url + link;
    }
    if (link.matches("") && url.matches(".*[^/]")) {
      return url + link;
    }
    throw new RuntimeException("Cannot make the link absolute. Url: " + url
        + " Link " + link);
      }catch(RuntimeException e){}
      return url;
  }  
}
