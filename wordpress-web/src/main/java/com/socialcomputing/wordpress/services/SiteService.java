package com.socialcomputing.wordpress.services;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.hibernate.Session;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.socialcomputing.wordpress.persistence.dao.SiteDailyDao;
import com.socialcomputing.wordpress.persistence.dao.SiteInfoDao;
import com.socialcomputing.wordpress.persistence.dao.hibernate.SiteDailyDaoHibernate;
import com.socialcomputing.wordpress.persistence.dao.hibernate.SiteInfoDaoHibernate;
import com.socialcomputing.wordpress.persistence.model.SiteDaily;
import com.socialcomputing.wordpress.persistence.model.SiteInfo;
import com.socialcomputing.wordpress.utils.HibernateUtil;
import com.socialcomputing.wordpress.utils.URLUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@Path("/sites")
public class SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);    
    private static final PropertiesConfiguration CONFIG;
    
    /*
     *  Reading configuration file, only once and not for each Site service initialization 
     */
    static {
    	try {
			CONFIG = new PropertiesConfiguration("wordpress-web.properties");
			FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
	    	reloadingStrategy.setRefreshDelay(60000);
	    	CONFIG.setReloadingStrategy(reloadingStrategy);
		} 
    	catch (ConfigurationException e) {
    		LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
    }

    // If the default quota property is not set, default daily value is set to 300
    private int defaultQuota = 300;
    private SiteInfoDao siteInfoDao = new SiteInfoDaoHibernate();
    private SiteDailyDao siteDailyDao = new SiteDailyDaoHibernate();
    
    
    @GET
    @Path("site.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response record(@QueryParam("url") String url, 
    		               @QueryParam("login") String login, @QueryParam("password") String password) {
        try {
        	DateMidnight today = new DateMidnight();
        	LOG.debug("day of the call: {}", today.toString());
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            LOG.debug("received url: {}", url);
            String output = "";
            
        	// Getting site information and nb of calls already done from db
            String domainURL = URLUtil.getDomain(url);
            Date todayDate = today.toDate();
            LOG.debug("url to call: {}, domain part: {}", url, domainURL);
        	SiteInfo siteInfo = this.siteInfoDao.findByDomain(domainURL);
            SiteDaily siteDaily = this.siteDailyDao.findByDomainAndDay(domainURL, todayDate);
            	
        	// Checking quota information
            if(siteDaily != null) {
            	int quota = (siteInfo.getQuota() == -1) ? CONFIG.getInt("defaultQuota", this.defaultQuota) : siteInfo.getQuota();
            	if(quota != 0) {
            		if(siteDaily.getCount() >= quota) {
            			// Quota Exceeded send appropriate error
            			LOG.error("Quota exceeded, authoized: {}, current: {}", quota, siteDaily.getCount());
            			
            			// TODO : return the appropriate error message for the rest connector
            		}
            	}
            }
        	
        	// if the url is provided, get the data from the remote url
        	Client client = Client.create();
        	WebResource webResource = client.resource(url);
        	
        	// if login and password are provided add authentication header
        	if(login != null && password != null) {
        		client.addFilter(new HTTPBasicAuthFilter(login, password));
        	}
        	ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
    		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
    			   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    		}
    		
    		output = response.getEntity(String.class);
    		LOG.debug("response from remote service: {}", output);
    		
    		// only update the database if data has been successfully read          
            if(siteInfo == null) {
            	siteInfo = new SiteInfo(domainURL, url);
            	this.siteInfoDao.create(siteInfo);
            }
            else {
            	siteInfo.updateLatestAccess(url);
            	this.siteInfoDao.update(siteInfo);
            }
            
            if(siteDaily == null) {
            	siteDaily = new SiteDaily(domainURL, todayDate);
            	this.siteDailyDao.create(siteDaily);
            }
            else {
            	siteDaily.incrementUpdate();
            	this.siteDailyDao.update(siteDaily);
            }
            return Response.ok(output).build();
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        }
    }
}