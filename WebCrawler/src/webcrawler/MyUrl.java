/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyURL class
 * @author gilmi
 */
public class MyUrl
{
    private final URL _url;
    
    public MyUrl(URL url)
    {
        
        _url=url;
    }
    /**
     * connects to url and returns the content of the url
     * ############# TODO: Documentation! ############
     * 
     * @return url content String
     * @throws java.io.IOException
     * @throw TODO
     */
    public String getString() throws IOException
    { 
        URLConnection connection = _url.openConnection();
        BufferedReader in;
        in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
    
    // ============== overriding equals ===============
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyUrl)
            return this.getAddress().equals(((MyUrl)obj).getAddress()); 
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this._url);
        return hash;
    }
    // ================================================
    
    /**
     * @return url path as string
     */
    public String getPath() { return _url.getPath(); }
    /**
     * @return url host as string
     */    
    public String getHost() { return "http://" + _url.getHost(); }
     /**
     * @return url address as string
     */
    public String getAddress() { return _url.toString(); }
    /**
     * find the current host + folder of the url
     * @return an absolute list to folder
     */
    public String getHostPath()
    {
            String pattern = "(.*)(/.*?)";
            String urladdr = getAddress();
            String urlhost = getHost();
            if (!urladdr.equals(urlhost))
            {
                Matcher matcher = Pattern.compile(pattern).matcher(urladdr);

               if (matcher.find())
                   return matcher.group(0); 
            }
           
           return urlhost + "/";
        
    }
    public String getProtocol()
    {
        return _url.getProtocol();
    }
    /**
     * @return the file type of the url
     *  @throws java.io.IOException if can't connect
     */
    public String getType() throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)_url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        
         String type = connection.getContentType();
        connection.disconnect();
        if (type == null)
            type = "";
        
         return type;
    }
    /**
     * 
     * @return a MyUrl to it's robots file 
     */
    public MyUrl getRobots()
    {
        MyUrl myRobots = null;
        try
        {
            myRobots = new MyUrl(new URL(getHost()+"/robots.txt"));
        }
        catch(MalformedURLException e ) { }
        return myRobots;
    }
}
