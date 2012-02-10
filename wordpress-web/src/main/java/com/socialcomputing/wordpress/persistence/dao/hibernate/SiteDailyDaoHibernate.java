/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import com.socialcomputing.wordpress.persistence.dao.SiteDailyDao;
import com.socialcomputing.wordpress.persistence.model.SiteDaily;
import com.socialcomputing.wordpress.utils.HibernateUtil;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class SiteDailyDaoHibernate implements SiteDailyDao {

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteDailyDao#update(com.socialcomputing.wordpress.persistence.model.SiteDaily)
	 */
	public void update(SiteDaily siteDaily) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.update(siteDaily);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteDailyDao#create(com.socialcomputing.wordpress.persistence.model.SiteDaily)
	 */
	public void create(SiteDaily siteDaily) {
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	     session.save(siteDaily);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteDailyDao#findByDomain(java.lang.String)
	 */
	public SiteDaily findByURL(String url) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return (SiteDaily) session.get(SiteDaily.class, url);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteDailyDao#findByDomainAndDay(java.lang.String, java.util.Date)
	 */
	public SiteDaily findByURLAndDay(String url, DateTime day) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM SiteDaily WHERE url = :url AND day = :day");
		query.setString("url", url);
		query.setDate("day", day.toDate());
		List<SiteDaily> results = query.list();
		if(results == null || results.size() == 0) return null;
		return results.get(0);
	}
}