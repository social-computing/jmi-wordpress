/**
 * 
 */
package com.socialcomputing.wordpress.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class URLUtil {
	public static final Pattern WORDPRESS_PATH_REGEXP = Pattern.compile("([-a-zA-Z0-9+&@#/%=~_|]*/api/jmi)(.*)");
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
        Matcher m = WORDPRESS_PATH_REGEXP.matcher(u.getPath());
        if(!m.find()) {
        	throw new MalformedURLException("Invalid path in url, it should contain the /api/jmi subpath: " + u.getPath());
        }
        String path = m.group(1);
        LOG.debug("extracted path: {}, last part: {}", path, m.group(2));
        return u.getProtocol() + "://" + u.getHost() + (port != -1 && port != 80 ? ":" + port : "") + path;
    }
}
