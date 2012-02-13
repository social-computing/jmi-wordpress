/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao.hibernate;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import com.socialcomputing.wordpress.persistence.dao.SiteInfoDao;
import com.socialcomputing.wordpress.persistence.model.SiteInfo;
import com.socialcomputing.wordpress.utils.HibernateUtil;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class SiteInfoDaoHibernate implements SiteInfoDao {

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
	public SiteInfo findByURL(String url) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return (SiteInfo) session.get(SiteInfo.class, url);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteInfoDao#getLatest()
	 */
	public Collection<SiteInfo> getLatest(int start, int max) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM SiteInfo ORDER BY updated DESC");
        query.setFirstResult(start);
        max = (max == -1) ? SiteInfoDao.MAX_NB_RESULTS : Math.min(max, SiteInfoDao.MAX_NB_RESULTS);
        query.setMaxResults(max);
		return query.list();
	}
}
