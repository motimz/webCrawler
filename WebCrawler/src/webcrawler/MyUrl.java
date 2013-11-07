/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Stub MyURL class
 * @author gilmi
 */
public class MyUrl {
    private URL _url;
    
    public MyUrl(URL url)
    {
        _url=url;
    }
    
    public String getString() { 
        try{
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
        }catch(IOException a){
        return a.toString();}
    }
    
    public URL getURL() { return _url; }
}
