/**
 * 
 */
package com.socialcomputing.wordpress.persistence.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

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
	public SiteDaily findByDomain(String domain) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return (SiteDaily) session.get(SiteDaily.class, domain);
	}

	/* (non-Javadoc)
	 * @see com.socialcomputing.wordpress.persistence.dao.SiteDailyDao#findByDomainAndDay(java.lang.String, java.util.Date)
	 */
	public SiteDaily findByDomainAndDay(String domain, Date day) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM SiteDaily WHERE domain = :domain AND day = :day");
		query.setString("domain", domain);
		query.setDate("day", day);
		List<SiteDaily> results = query.list();
		if(results == null || results.size() == 0) return null;
		return results.get(0);
	}
}
