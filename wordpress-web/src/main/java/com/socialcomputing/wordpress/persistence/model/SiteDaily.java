package com.socialcomputing.wordpress.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class SiteDaily {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Index(name = "urlIndex")
    @Column(columnDefinition = "varchar(255)")
    private String url;

	@Index(name = "dayIndex")
	@Column
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime day;
	
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
    public SiteDaily(String url, DateTime day) {
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