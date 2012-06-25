package com.socialcomputing.wordpress.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;

import com.socialcomputing.wordpress.services.SiteService;

@Entity
public class SiteInfo {
	@Id
    @Column(columnDefinition = "varchar(255)")
    private String url;
	private String latesturl;
	
    @XmlAttribute
    private long    count;
    @XmlAttribute
    private long     quota;
    @XmlAttribute
    private long    dailyCount;
    @XmlAttribute
    private Date    created;
    @XmlAttribute
    private Date    updated;
    
    // default constructor
    public SiteInfo() {
    	this.url = null;
    	this.latesturl = null;
    	this.count = 0;
    	this.dailyCount = 0;
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
        Date now = new Date();
        if( !SiteService.isNowSameDayAsDate( this.updated)) {
            this.dailyCount = 0;
        }
        this.latesturl = url;
        this.updated = new Date();
        this.count++;
        this.dailyCount++;
        this.updated = now;
        
    }

	public long getQuota() {
		return this.quota;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getLatestUrl() {
		return this.latesturl;
	}
	
	public Date getCreated() {
		return this.created;
	}
	
	public Date getUpdated() {
		return this.updated;
	}

	public long getDailyCount() {
        return this.dailyCount;
    }
}