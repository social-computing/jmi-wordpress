package com.socialcomputing.wordpress.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class SiteInfo {
	@Id
    @Column(columnDefinition = "varchar(255)")
    private String url;
	private String latesturl;
	
	@Column
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime created;
    
	@Column
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime updated;
    
    private int quota;
    
    // default constructor
    public SiteInfo() {
    	this.url = null;
    	this.latesturl = null;
    }
    
    /**
     * Constructor with url and domain of the site 
     * 
     * @param url           the service base url to call
     * @param latesturl     the complete url of the service to call
     */
    public SiteInfo(String url, String latesturl) {
        this.url = url;
        this.latesturl = latesturl;
        this.created = new DateTime();
        this.updated = new DateTime();
        this.quota = -1;
    }

    /**
     * Update the site information with the current call
     * 
     * @param url     the complete url of the service to call
     */
    public void updateLatestAccess(String url) {
        this.latesturl = url;
        this.updated = new DateTime();
    }

	public int getQuota() {
		return this.quota;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getLatestUrl() {
		return this.latesturl;
	}
	
	public DateTime getCreated() {
		return this.created;
	}
	
	public DateTime getUpdated() {
		return this.updated;
	}
}