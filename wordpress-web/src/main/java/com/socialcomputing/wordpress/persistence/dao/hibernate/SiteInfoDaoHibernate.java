/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao.hibernate;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.socialcomputing.wordpress.persistence.dao.SiteInfoDao;
import com.socialcomputing.wordpress.persistence.model.SiteInfo;
import com.socialcomputing.wordpress.utils.HibernateUtil;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class SiteInfoDaoHibernate implements SiteInfoDao {

	 private static final Logger LOG = LoggerFactory.getLogger(SiteInfoDaoHibernate.class);
			 
	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteInfoDao#update(com.socialcomputing.wordpress.persistence.model.SiteInfo)
	 */
	public void update(SiteInfo siteInfo) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.update(siteInfo);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteInfoDao#create(com.socialcomputing.wordpress.persistence.model.SiteInfo)
	 */
	public void create(SiteInfo siteInfo) {
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	     session.save(siteInfo);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteInfoDao#findByDomain(java.lang.String)
	 */
	public SiteInfo findByDomain(String domain) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return (SiteInfo) session.get(SiteInfo.class, domain);
	}
}
