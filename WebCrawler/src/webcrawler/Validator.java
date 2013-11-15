/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gilmi
 * 
 * validates MyUrls for the use of the crawler.
 * it will check duplication with previous MyUrls seen, if a url is allowed,
 * if a url is of the right type and does it has any of the keywords i it's title
 */
public class Validator {
    private Set<MyUrl> _visited = new HashSet(); // previously visited urls
    private String _keywords = ""; // keywords that needs to be in each url title
    private static final String FILETYPE = "text/html"; // url must be this type
    
    /** Constructor for Validator class
     * @param keywords which keywords needs to be in the title for validation?
     */
    public Validator(ArrayList<String> keywords) 
    {
        // add keywords to pattern string
        _keywords += ".*(";
        for (String keyword : keywords)
           _keywords += keyword + "|";
        
        // remove last "|" and replace it with ").*"
        _keywords = _keywords.substring(0, _keywords.length()-1);
        _keywords += ").*";
        
    }
    /** Validate Function. checks 4 things:
     * 1. have we seen this url?\n
     * 2. is it disallowed?\n
     * 3. is it the wrong filetype?\n
     * 4. it none of the keywords specified are in it's title?\n
     * \n
     * if it answers "false" on one of these questions, it returns false
     * otherwise, it returns true
     * 
     * @param url url to check
     * @param robotsFile robots file to check if url is not disallowed
     * @return boolean 
     */
    public boolean Validate(MyUrl url, MyUrl robotsFile) 
    {
        if (isVisited(url))
            return false;

        if (isDisallowed(url, robotsFile))
            return false;
        
        if (!isFileType(url))
            return false;
        
        return hasKeywordInTitle(url);
    }
    
    /**
     * checks if the url has already been visited
     * @param url url to check
     * @return boolean
     */
    private boolean isVisited(MyUrl url)
    {
        if (_visited.contains(url))
            return true;
        
        _visited.add(url);
        return false;
         
    }
    /**
     * checks if the url is not disallowed in robots
     * @param url to be checked
     * @param robots robots.txt file to be checked in
     * @return boolean
     */
    private static boolean isDisallowed(MyUrl url, MyUrl robots)
    {
        try {    
           String robo = robots.getString();
           String pattern = "Disallow:\\s*" + url.getPath();
           Matcher matcher = Pattern.compile(pattern).matcher(robo);
           
           if (matcher.find() && url.getPath().compareTo("/")!=0 && url.getPath().compareTo("")!=0)
               return true;
        }
        catch (IOException e) {} // no robots? no problem.
        
        return false;
    }
    /**
     * checks if the file is of the right file type
     * @param url to be checked
     * @return boolean
     */
    private static boolean isFileType(MyUrl url)
    {
        try
        {
            if(url.getType()==null)
                return false;
            if (url.getType().contains(FILETYPE))
            {
                return true;
            } 
         } catch (IOException e) { System.err.println("can't connect");}      
         return false;
     }

    /**
     * checks if the url has one or more of the keywords in it's title
     * @param url to be checked
     * @return boolean
     */
    private boolean hasKeywordInTitle(MyUrl url)
    {
        try 
        {
            String pattern = "(<title>)(" + _keywords + ")(</title>)";
            String urlstr = url.getString();
            Matcher matcher = Pattern.
                   compile(pattern, Pattern.CASE_INSENSITIVE).
                   matcher(urlstr);

           if (matcher.find())
               return true;
        }
        catch (IOException e) {}
        
        return false;
    }
}
