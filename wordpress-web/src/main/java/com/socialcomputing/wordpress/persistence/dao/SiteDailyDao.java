/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao;

import java.util.Date;

import com.socialcomputing.wordpress.persistence.model.SiteDaily;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public interface SiteDailyDao {
	
    void update(SiteDaily siteDaily);
    
    void create(SiteDaily siteDaily);
    
    SiteDaily findByDomain(String domain);
    
    SiteDaily findByDomainAndDay(String domain, Date day);
}
