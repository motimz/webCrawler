/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.util.Queue;

/**
 *
 * @author motimi
 */
public class Crawler {
    
    private Queue<MyUrl> _urls;
    private Validator _validator;
    /** Constructor for Crawler Class 
     *  @param validator that validates the url
     * @
     */
    public Crawler(Validator v)
    {
        _validator=v;
    }
    
    public void startCrawl(MyUrl url,int limit)
    {
        
    }
    
    public void scanUrl(MyUrl url)
    {
        
    }
    
}
