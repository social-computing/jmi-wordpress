/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao;

import com.socialcomputing.wordpress.persistence.model.SiteInfo;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public interface SiteInfoDao {

    void update(SiteInfo siteInfo);
    
    void create(SiteInfo siteInfo);
    
    SiteInfo findByURL(String url);
}
