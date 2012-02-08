package com.socialcomputing.wordpress.services;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.socialcomputing.wordpress.persistence.Site;
import com.socialcomputing.wordpress.utils.HibernateUtil;
import com.socialcomputing.wordpress.utils.URLUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@Path("/sites")
public class SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);

    @GET
    @Path("record.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response record(@QueryParam("url") String url, 
    		               @QueryParam("login") String login, @QueryParam("password") String password) {
        try {
        	DateMidnight today = new DateMidnight();
        	LOG.debug("day of the call: {}", today.toString());
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            LOG.debug("received url: {}", url);
            String output = "";
            if(url != null) {
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
                String domainURL = URLUtil.getDomain(url.trim());
                LOG.debug("url to call: {}, domain part: {}", url, domainURL);
                
                
                Site site = (Site) session.get(Site.class, url);
                if(site == null) {
                    site = new Site(url);
                    session.save(site);
                }
                else {
                    site.incrementUpdate();
                    session.update(site);
                }
            }
            return Response.ok(output).build();
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        }
    }
}