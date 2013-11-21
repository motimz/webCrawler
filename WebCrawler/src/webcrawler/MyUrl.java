/**
 * MyURL class wraps Java's URL.
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
 * MyURL class wraps Java's URL
 * @author Gil Mizrahi
 * @author Moti Mizrahi
 */
public class MyUrl
{
    private final URL _url;
    /**
     * Constructor
     * @param url Java's URL
     */
    public MyUrl(URL url)
    {
        
        _url=url;
    }
    /**
     * connects to url and returns the content of the url
     * @return url content String
     * @throws java.io.IOException
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

        // read while you can
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
    /** This function check if a string is mail or javascript
     *  @param s of type String stands for a string.
     *  @return boolean
     */
    private static boolean isJSOrMail(String s) 
    {
        return s.matches("javascript:.*|mailto:.*");
    }
    /** This Function creates the link by the way it is given 
     * (path , www. , http) and its domain
     *  @param url of type MyUrl of the current url.
     *  @param link of type String stands for the url checked inside the domain.
     *  @return Absolute path of url.
     * @throws java.net.MalformedURLException
     */
    public static String makeAbsolute(MyUrl url, String link) throws MalformedURLException
    {
        if (isJSOrMail(link))
            throw new MalformedURLException(link);
        
        // if link is absolute including protocol, return it
        if (link.matches("http://.*") || link.matches("https://.*")) 
            return link;
        // if link is absolute without protocol, add current protocol and return it
        else if (link.matches("//.*"))
            return url.getProtocol() + ":" + link;
        // if link doesn't use a kind of http protocol throw exception
        else if (link.matches(".*://"))
            throw new MalformedURLException(link);
        
        // id in current address is the url
        else if (link.startsWith("#"))
            return url.getAddress();
        
        // unimportant slash
        else if (link.startsWith("/"))
            link = link.substring(1);
        
        return url.getHostPath() + link;
    } 
   /**
     * Path of URL
     * @return url path as string
     */
    public String getPath() { return _url.getPath(); }
    /** Host of URL
     * @return url host as string
     */    
    public String getHost() { return getProtocol() + "://" + _url.getHost(); }
     /**
      * Address of URL
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
		// else
		return urlhost + "/";
    }
	/**
     * Returns the protocol of the URL
     * @return The protocol the URL connects through
     */
    public String getProtocol()
    {
        return _url.getProtocol();
    }
    /**
     * Returns the type of the file in the URL
     * @return the file type of the url
     * @throws IOException if can't connect
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
     * returns the robots file of the site of the url
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
