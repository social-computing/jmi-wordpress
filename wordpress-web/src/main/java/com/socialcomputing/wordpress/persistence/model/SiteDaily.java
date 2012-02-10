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
    private String url;

	private Date day;
	
    @Index(name = "countIndex")
    private int count;


    // default constructor
    public SiteDaily() {
    	this.url = null;
    	this.count = 1;
    }
    
    /**
     * Constructor with site access information
     * 
     * @param url   
     * @param day
     */
    public SiteDaily(String url, Date day) {
        super();
        this.url = url;
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