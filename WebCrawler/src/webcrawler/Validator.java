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
 */
public class Validator {
    private Set<MyUrl> _visited = new HashSet();
    private String _keywords = "";
    private static final String FILETYPE = "text/html";
    
    /** Constructor for Validator class
     * @param keywords that needs to be in the title
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
     * 1. have we seen this url?
     * 2. is it disallowed?
     * 3. is it the wrong filetype?
     * 4. it none of the keywords specified are in it's title?
     * 
     * if it answers "false" on one of these questions, it returns false
     * otherwise, it returns true
     * 
     * @param url 
     * @param robotsFile 
     * @return Boolean 
     */
    public Boolean Validate(MyUrl url, MyUrl robotsFile) 
    {
        if (isVisited(url))
            return false;

        if (isDisallowed(url, robotsFile))
            return false;
        
        if (!isFileType(url))
            return false;
        
        return hasKeywordInTitle(url);
    }
    
    private Boolean isVisited(MyUrl url)
    {
        if (_visited.contains(url))
            return true;
        
        _visited.add(url);
        return false;
         
    }
    
    private static Boolean isDisallowed(MyUrl url, MyUrl robots)
    {
        try {    
           String robo = robots.getString();
           String pattern = "Disallow:\\s*" + url.getPath();
           Matcher matcher = Pattern.compile(pattern).matcher(robo);
           
           if (matcher.find() && pattern.compareTo("Disallow:\\s*")!=0)
               return true;
        }
        catch (IOException e) {} // no robots? no problem.
        
        return false;
    }
    
    private static Boolean isFileType(MyUrl url)
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
    private Boolean hasKeywordInTitle(MyUrl url)
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
        
        //return false;
        return false;
    }
}
