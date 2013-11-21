/*
 * Crawler crawls the web.
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
 * Crawler crawls the web.
 * @author Moti Mizrahi
 * @author Gil Mizrahi
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
            
        //add the first url to the list
            _urls.add(url);
            if(!_validator.Validate(_urls.peek(),new MyUrl(new URL(_urls.peek().getHost()+"/robots.txt")))) //checks if first url validated
            {
                  return;
            }
            /* run on the list of urls . download the url if it is validated
             * and pop it from the list
             */
            for(int numOfScanned=0; (limit != 0 || numOfScanned < limit) && !_urls.isEmpty();)
            {
                MyUrl check=_urls.poll();// pops the first url in list
                System.out.println("Downloading " + check.getAddress());
                List<MyUrl> urls_list=scanUrl(check);//scans the url for links and returns list of links
                for (MyUrl currentUrl : urls_list)
                {
                    if(_validator.Validate(currentUrl,new MyUrl(new URL(currentUrl.getHost()+"/robots.txt"))))
                    {
                        _urls.add(currentUrl);
                        System.out.println("Found new URL " + currentUrl.getAddress());
                        numOfScanned++;
                        if (limit != 0 && numOfScanned >= limit)
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
     *  @return a List<MyUrl> of the links from url
     */
    public List<MyUrl> scanUrl(MyUrl url)
    {      
        Pattern htmltag;
        Pattern link;
        
        htmltag = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
        link = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
        List<MyUrl> links = new ArrayList<>();
       
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
					
                    try {
                         links.add(new MyUrl(new URL(MyUrl.makeAbsolute(url, linkd))));
                    } 
                    catch(MalformedURLException e) { /* skip */ }
                }  
            }
        }catch(IOException a){
            /*System.out.println(a.toString());*/
        }
        return (links);
    }
}

