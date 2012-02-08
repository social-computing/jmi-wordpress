package com.socialcomputing.wordpress.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Index;

@Entity
public class Site {

	@Id
    @Column(columnDefinition = "varchar(255)")
    private String url;

    @Index(name = "countIndex")
    private int count;

    private Date created;

    private Date updated;
    
    // default constructor
    public Site() {
    	this.url = null;
    	this.count = 1;
    }
    
    public Site(String url) {
        super();
        this.url = url;
        this.count = 1;
        this.created = new Date();
        this.updated = new Date();
    }

    public void incrementUpdate() {
        this.count++;
        this.updated = new Date();
    }
}