/**
 * 
 */
package com.socialcomputing.wordpress.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class SiteServiceError {
	private static final Logger LOG = LoggerFactory.getLogger(SiteServiceError.class);
	
	private final long code;
	private final String message, stacktrace;
	
	public SiteServiceError(long code, String message, String stacktrace) {
		this.code = code;
		this.message = message;
		this.stacktrace = stacktrace;
	}
	
	public SiteServiceError(long code, String message) {
		this(code, message, null);
	}
	
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"error\":{");
		sb.append("\"code\":");
		sb.append(this.code);
		sb.append(",\"message\":\"");
		sb.append(this.message);
		sb.append("\",\"trace\":\"");
		sb.append(this.stacktrace);
		sb.append("\"}}");
		String res = sb.toString();
		LOG.debug("generated json : {}", res);
		return res;
	}
}
