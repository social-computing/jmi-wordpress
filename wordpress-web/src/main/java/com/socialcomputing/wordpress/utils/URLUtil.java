/**
 * 
 */
package com.socialcomputing.wordpress.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class URLUtil {
	private static final Logger LOG = LoggerFactory.getLogger(URLUtil.class);

	public URLUtil() {
		throw new UnsupportedOperationException();
	}
	
    public static final String getDomain(String url) throws MalformedURLException {
        URL u = new URL(url);
        return u.getHost();
    }
    
    public static final String normalizeUrl(String url) throws MalformedURLException {
        URL u = new URL(url);
        int port = u.getPort();
        String[] pathElements = u.getPath().substring(1).split("/");
        if(pathElements.length < 3) 
        	throw new MalformedURLException("Invalid path in url, it should contain at least 3 elements: " + u.getPath());
        String path = "/" + pathElements[0] + "/" + pathElements[1];
        LOG.debug("path: {}", path);
        return u.getProtocol() + "://" + u.getHost() + (port != -1 && port != 80 ? ":" + port : "") + path;
    }
}
