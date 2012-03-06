/**
 * 
 */
package com.socialcomputing.wordpress.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
        String path = "";
        if(!m.find()) {
        	Map<String, String> parameters = getParameters(u.getQuery());
        	if(parameters.containsKey("json")) {
        		String value = parameters.get("json");
        		if(!StringUtils.isBlank(value) && value.startsWith("jmi")) {
        			path = u.getPath() + "?json=jmi";
        			LOG.debug("extracted path: {}, last part: {}", u.getPath(), "?json=jmi");
        		}
        		else {
            		throw new MalformedURLException("Invalid url, it should contain json parameter with a value stating by jmi: " + value);
            	}
        	}
        	else {
        		throw new MalformedURLException("Invalid path in url, it should contain the /api/jmi subpath: " + u.getPath());
        	}
        }
        else {
        	path = m.group(1);
        	LOG.debug("extracted path: {}, last part: {}", path, m.group(2));
        }
        return u.getProtocol() + "://" + u.getHost() + (port != -1 && port != 80 ? ":" + port : "") + path;
    }
    
    public static final Map<String,String> getParameters(String queryPart) {
    	Map<String, String> paramsMap = new HashMap<String,String>();
        String params[] = queryPart.split("&");
        for (String param : params) {
            String temp[] = param.split("=");
            try {
            	paramsMap.put(temp[0], ((temp.length > 1) ? java.net.URLDecoder.decode(temp[1], "UTF-8") : ""));
			} 
            catch (UnsupportedEncodingException e) {
				// Todo ... Could this happen ?
			}
        }
        return paramsMap;
    }
}
