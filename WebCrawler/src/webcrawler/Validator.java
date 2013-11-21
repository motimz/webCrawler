/**
 * This class functions as a Validator for urls.
 * It validates MyUrls for the use of the crawler.
 * it will check duplication with previous MyUrls seen, if a url is allowed,
 * if a url is of the right type and does it has any of the keywords i it's title
 * 
 */
package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates MyUrls for the use of the crawler.
 * @author Gil Mizrahi
 * @author Moti Mizrahi
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
           _keywords += "\\b" + keyword + "\\b|";
        
        // remove last "|" and replace it with ").*"
        _keywords = _keywords.substring(0, _keywords.length()-1);
        _keywords += ").*";
        
    }
    /** Validate Function; checks 4 things:
     * <ol>
     * <li> have we seen this url?</li>
     * <li> is it disallowed?</li>
     * <li>is it the wrong filetype?</li>
     * <li>it none of the keywords specified are in it's title?</li>
     * </ol>
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
     * checks if the url is not disallowed in robots file.
     * @param url to be checked
     * @param robots robots.txt file to be checked in
     * @return boolean
     */
    private static boolean isDisallowed(MyUrl url, MyUrl robots)
    {
        try {    
           String robo = robots.getString();
           String urlPath = url.getPath();
           String pattern = "Disallow:\\s*" + urlPath + "\b";
           Matcher matcher = Pattern.compile(pattern).matcher(robo);
           
           if (matcher.find() && urlPath.compareTo("/")!=0 && urlPath.compareTo("")!=0)
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
            if (url.getType().contains(FILETYPE))
            {
                return true;
            } 
         } catch (IOException e) { /* skip */ }     
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
