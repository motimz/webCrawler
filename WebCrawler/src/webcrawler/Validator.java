/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gilmi
 * 
 */
public class Validator {
    private Set<URL> _visited = new HashSet();
    private String _keywords;
    private String _fileType;
    
    /** Constructor for Validator class
     * @param keywords that needs to be in the title
     */
    public Validator(ArrayList<String> keywords) 
    {
        
    }
    /** STUB Validate Function */
    public Boolean Validate(URL url) { 
        return true;
            
    }
    
}
