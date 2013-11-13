/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * Stub MyURL class
 * @author gilmi
 */
public class MyUrl
{
    private URL _url;
    
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
        BufferedReader in = new BufferedReader(
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
    
    public String getPath() { return _url.getPath(); }
    public String getHost() { return "http://" + _url.getHost(); }
    public String getAddress() { return _url.toString(); }
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
}
