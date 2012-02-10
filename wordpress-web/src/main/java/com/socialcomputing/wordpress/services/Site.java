/**
 * 
 */
package com.socialcomputing.wordpress.services;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
@Entity
@XmlRootElement
public class Site {
	public static final int QUOTA_UNDEFINED = -1;
	public static final int QUOTA_UNLIMITED = 0;
	
	@XmlElement
    private String url;
	
	@XmlElement
	private String latesturl;
	
	@XmlElement
	private DateTime created;
	
	@XmlElement
    private DateTime updated;
    
	@XmlElement
    private String quota;
	
	@XmlElement
	private int count;
	
	public Site(String url, 
				String latesturl,
				DateTime created,
				DateTime updated,
				int quota,
				int count) {
		this.url = url;
		this.latesturl = latesturl;
		this.created = created;
		this.updated = updated;
		this.count = count;
		
		// TODO: change this to a quota enum later
		switch(quota) {
			case QUOTA_UNDEFINED:
				this.quota = "undefined";
				break;
			case QUOTA_UNLIMITED:
				this.quota = "unlimited";
				break;
			default:
				this.quota = "" + quota;
		}
	}
	
	public Site(String url, 
			String latesturl,
			DateTime created,
			DateTime updated,
			int quota) {
		this(url, latesturl, created, updated, quota, 0);
	}
}