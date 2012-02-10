package com.socialcomputing.wordpress.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SiteInfo {
	
	@Id
    @Column(columnDefinition = "varchar(255)")
    private String url;
	private String latesturl;
    private Date created;
    private Date updated;
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
        super();
        this.url = url;
        this.latesturl = latesturl;
        this.created = new Date();
        this.updated = new Date();
        this.quota = -1;
    }

    /**
     * Update the site information with the current call
     * 
     * @param url     the complete url of the service to call
     */
    public void updateLatestAccess(String url) {
        this.latesturl = url;
        this.updated = new Date();
    }

	public int getQuota() {
		return this.quota;
	}
}