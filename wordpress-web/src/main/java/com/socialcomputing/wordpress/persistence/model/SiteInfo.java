package com.socialcomputing.wordpress.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SiteInfo {
	
	@Id
    @Column(columnDefinition = "varchar(255)")
    private String domain;
	private String latesturl;
    private Date created;
    private Date updated;
    private int quota;
    
    // default constructor
    public SiteInfo() {
    	this.domain = null;
    	this.latesturl = null;
    }
    
    /**
     * Constructor with url and domain of the site 
     * 
     * @param domain  the domain part of the service to call
     * @param url     the complete url of the service to call
     */
    public SiteInfo(String domain, String url) {
        super();
        this.domain = domain;
        this.latesturl = url;
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