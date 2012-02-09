package com.socialcomputing.wordpress.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity
public class SiteDaily {
	@Id
    @Column(columnDefinition = "varchar(255)")
    private String domain;

	private Date day;
	
    @Index(name = "countIndex")
    private int count;


    // default constructor
    public SiteDaily() {
    	this.domain = null;
    	this.count = 1;
    }
    
    /**
     * Constructor with site access information
     * 
     * @param domain   
     * @param day
     */
    public SiteDaily(String domain, Date day) {
        super();
        this.domain = domain;
        this.day = day;
        this.count = 1;
    }

    public void incrementUpdate() {
        this.count++;
    }

	public int getCount() {
		return this.count;
	}
}