/**
 * 
 */
package com.socialcomputing.wordpress.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class URLUtil {

	public URLUtil() {
		throw new UnsupportedOperationException();
	}
	
    public static final String getDomain(String url) throws MalformedURLException {
        URL u = new URL(url);
        return u.getHost();
    }
    
    public String normalizeUrl(String url) throws MalformedURLException {
        URL u = new URL(url);
        int port = u.getPort();
        return u.getProtocol() + "://" + u.getHost()+ (port != -1 && port != 80 ? ":" + port : "") + u.getPath();
    }
}
