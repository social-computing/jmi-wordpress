package com.socialcomputing.wordpress.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.socialcomputing.wordpress.persistence.dao.SiteDailyDao;
import com.socialcomputing.wordpress.persistence.dao.SiteInfoDao;
import com.socialcomputing.wordpress.persistence.dao.hibernate.SiteDailyDaoHibernate;
import com.socialcomputing.wordpress.persistence.dao.hibernate.SiteInfoDaoHibernate;
import com.socialcomputing.wordpress.persistence.model.SiteDaily;
import com.socialcomputing.wordpress.persistence.model.SiteInfo;
import com.socialcomputing.wordpress.utils.StringUtils;
import com.socialcomputing.wordpress.utils.URLUtil;
import com.socialcomputing.wordpress.utils.log.DiagnosticContext;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.ReaderWriter;

@Path("/sites")
public class SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);
    private static final int DEFAULT_QUOTA = 300;
    
    // If the default quota property is not set, default daily value is set to 300
    private SiteInfoDao siteInfoDao = new SiteInfoDaoHibernate();
    private SiteDailyDao siteDailyDao = new SiteDailyDaoHibernate();
    
    
    @GET
    @Path("site.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response record(@HeaderParam(HttpHeaders.ACCEPT_ENCODING) String acceptEncoding,
    		               @QueryParam("url") String url, @QueryParam("login") String login, @QueryParam("password") String password) {
        try {
        	MDC.put(DiagnosticContext.ENTRY_POINT_CTX.name, "GET /sites/site.json?url=" + url);
        	DateTime today = new DateMidnight().toDateTime();
            
        	// Getting site information and nb of calls already done from db
            // String domainURL = URLUtil.getDomain(url);
            String normalizedURL = URLUtil.normalizeUrl(url);
            LOG.debug("normalized url: {}", normalizedURL);
        	SiteInfo siteInfo = this.siteInfoDao.findByURL(normalizedURL);
            SiteDaily siteDaily = this.siteDailyDao.findByURLAndDay(normalizedURL, today);
            	
        	// Checking quota information
            if(siteDaily != null) {
            	int quota = (siteInfo.getQuota() == -1) ? DEFAULT_QUOTA : siteInfo.getQuota();
            	if(quota != 0) {
            		if(siteDaily.getCount() >= quota) {
            			// Quota Exceeded send appropriate error
            			LOG.info("Quota exceeded for url: {}, today count value: {}", url, siteDaily.getCount());
            			SiteServiceError error = new SiteServiceError(01, "Quota exceeded");
            			return Response.ok(error.toJson()).build();
            		}
            	}
            }
        	
        	// If the url is provided, get the data from the remote url
        	Client client = Client.create();
        	
        	// if login and password are provided add authentication header
        	if(!StringUtils.isBlank(login) && !StringUtils.isBlank(password)) {
        		LOG.debug("login and password provided, adding authentication header with login: {}", login);
        		client.addFilter(new HTTPBasicAuthFilter(login, password));
        	}

        	LOG.debug("HTTP request Header {}: {}", HttpHeaders.ACCEPT_ENCODING, acceptEncoding);
        	ClientResponse clientResponse = client.resource(url)
        			                        .accept(MediaType.APPLICATION_JSON)
        			                        .header(HttpHeaders.ACCEPT_ENCODING, acceptEncoding)
        			                        .get(ClientResponse.class);
        			
    		if (clientResponse.getStatus() != Response.Status.OK.getStatusCode()) {
    			   throw new RuntimeException("Calling remote service failed with HTTP error code : " + clientResponse.getStatus());
    		}
        	
    		
    		// only update the database if data has been successfully read    
            if(siteInfo == null) {
            	siteInfo = new SiteInfo(normalizedURL, url);
            	this.siteInfoDao.create(siteInfo);
            }
            else {
            	siteInfo.updateLatestAccess(url);
            	this.siteInfoDao.update(siteInfo);
            }
            
            if(siteDaily == null) {
            	siteDaily = new SiteDaily(normalizedURL, today);
            	this.siteDailyDao.create(siteDaily);
            }
            else {
            	siteDaily.incrementUpdate();
            	this.siteDailyDao.update(siteDaily);
            }
            return this.proxy(clientResponse);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        }
        finally{
        	MDC.remove(DiagnosticContext.ENTRY_POINT_CTX.name);
        }
    }
    

    @GET
    @Path("top")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Site> top(@DefaultValue("0") @QueryParam("start") int start, @DefaultValue("-1") @QueryParam("max") int max) {
        try {
        	MDC.put(DiagnosticContext.ENTRY_POINT_CTX.name, "GET /sites/top");
        	DateTime today = new DateMidnight().toDateTime();
        	Collection<SiteInfo> latestSites = this.siteInfoDao.getLatest(start, max);
        	List<Site> sites = new ArrayList<Site>();
        	for(SiteInfo siteInfo : latestSites) {
        		SiteDaily siteDaily = this.siteDailyDao.findByURLAndDay(siteInfo.getUrl(), today);
        		int count = (siteDaily != null) ? siteDaily.getCount() : 0;
        		sites.add(new Site(siteInfo.getUrl(), 
        						   siteInfo.getLatestUrl(),
        						   siteInfo.getCreated(),
        						   siteInfo.getUpdated(),
        						   siteInfo.getQuota(),
        						   count));
        	}
        	return sites;
        }
        finally{
        	MDC.remove(DiagnosticContext.ENTRY_POINT_CTX.name);
        }
    }
    
    private Response proxy(ClientResponse clientResponse) {
		// A working solution but not the best
    	/* DEBUG */
		/*
		String contentEncoding = headers.get(HttpHeaders.CONTENT_ENCODING).get(0);
		InputStream stream = response.getEntityInputStream();
		ResponseBuilder rb = Response.ok(contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip") ? new GZIPInputStream(stream) : stream);
			*/
		
		MultivaluedMap<String, String> headers = clientResponse.getHeaders();
		LOG.debug("HTTP response Headers: {}", headers);
		
		// Copy response content from remote service 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
	        ReaderWriter.writeTo(clientResponse.getEntityInputStream(), baos);
	    } 
		catch(IOException ex) {
	        throw new ClientHandlerException(ex);
	    } 
		finally {
			try {
				clientResponse.getEntityInputStream().close();
			} 
			catch (IOException e) {
				throw new ClientHandlerException(e);
			}
	    }
	    ResponseBuilder rb = Response.ok(baos.toByteArray());
		
		// Also copy remote response headers
		for(Entry<String, List<String>> header : headers.entrySet()) {
			for(String value : header.getValue()) {
				rb.header(header.getKey(), value);
			}
		}
        return rb.build();
    }
    
}
