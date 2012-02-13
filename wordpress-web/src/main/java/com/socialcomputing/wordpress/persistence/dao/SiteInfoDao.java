/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao;

import java.util.Collection;

import com.socialcomputing.wordpress.persistence.model.SiteInfo;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public interface SiteInfoDao {
    static final int MAX_NB_RESULTS = 100;

    void update(SiteInfo siteInfo);
    
    void create(SiteInfo siteInfo);
    
    SiteInfo findByURL(String url);
    
    Collection<SiteInfo> getLatest(int start, int max);
}
