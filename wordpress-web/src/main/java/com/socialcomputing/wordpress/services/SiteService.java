package com.socialcomputing.wordpress.services;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.socialcomputing.wordpress.persistence.model.SiteInfo;
import com.socialcomputing.wordpress.utils.HibernateUtil;
import com.socialcomputing.wordpress.utils.StringUtils;
import com.socialcomputing.wordpress.utils.URLUtil;
import com.socialcomputing.wordpress.utils.log.DiagnosticContext;
import com.socialcomputing.wps.server.planDictionnary.connectors.datastore.StoreHelper;
import com.socialcomputing.wps.server.planDictionnary.connectors.utils.UrlHelper;

@Path("/sites")
public class SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);
    private static final int DEFAULT_QUOTA = 500;
    private static final int MAX_NB_RESULTS = 100;
    
    
    @GET
    @Path("site.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response record(@HeaderParam("user-agent") String userAgent,
    		               @QueryParam("url") String url, @QueryParam("login") String login, @QueryParam("password") String password) {
        try {
        	MDC.put(DiagnosticContext.ENTRY_POINT_CTX.name, "GET /sites/site.json?url=" + url);

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            String normalizedURL = URLUtil.normalizeUrl(url);
            LOG.debug("normalized url: {}", normalizedURL);
            SiteInfo siteInfo = (SiteInfo) session.get(SiteInfo.class, normalizedURL);
            if( siteInfo == null) {
                siteInfo = new SiteInfo(normalizedURL,url);
                session.save( siteInfo);
            }
            else {
                siteInfo.updateLatestAccess(url);
                session.update( siteInfo);
            }
            // 0 is unlimited
            if( siteInfo.getQuota() != 0) {
                long quota = siteInfo.getQuota() == -1 ? DEFAULT_QUOTA : siteInfo.getQuota();
                if( siteInfo.getDailyCount() > quota) {
                    String error = StoreHelper.ErrorToJson(1000, "Your daily quota of " + quota + " maps has exceeded. Please <a title=\"Contact us\" target=\"_blank\" href=\"http://www.social-computing.com/en/contact/\">contact us</a>.", null);
                    return Response.ok(error).build();
                }
            }

            // Proxy
            UrlHelper urlHelper = new UrlHelper( url);
            urlHelper.addHeader( HttpHeaders.ACCEPT_ENCODING, "gzip");
            urlHelper.addHeader(HttpHeaders.USER_AGENT, userAgent);
            
            // if login and password are provided add authentication header
            if(!StringUtils.isBlank(login) && !StringUtils.isBlank(password)) {
                LOG.debug("login and password provided, adding authentication header with login: {}", login);
                urlHelper.setBasicAuth(login, password);
            }
            urlHelper.openConnections();
            HttpURLConnection connection = (HttpURLConnection) urlHelper.getConnection();
            return Response.ok( urlHelper.getResult(), urlHelper.getContentType())
                .lastModified( new Date( connection.getLastModified()))
                .status( connection.getResponseCode())
                .header( "Content-Length", connection.getContentLength())
                .build();
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Response.ok(StoreHelper.ErrorToJson(e)).build();
        }
        finally{
        	//MDC.remove(DiagnosticContext.ENTRY_POINT_CTX.name);
        }
    }
    

    @GET
    @Path("top.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteInfo> top(@DefaultValue("0") @QueryParam("start") int start, @DefaultValue("-1") @QueryParam("max") int max) {
        List<SiteInfo> sites = null;
        try {
        	MDC.put(DiagnosticContext.ENTRY_POINT_CTX.name, "GET /sites/top");
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Query query = session.createQuery("FROM SiteInfo ORDER BY updated DESC");
            query.setFirstResult(start);
            max = (max == -1) ? SiteService.MAX_NB_RESULTS : Math.min(max, SiteService.MAX_NB_RESULTS);
            query.setMaxResults(max);
            sites = query.list();
        }
        catch (Exception e) {
            Response.status( HttpServletResponse.SC_BAD_REQUEST);
        }
        finally{
        	MDC.remove(DiagnosticContext.ENTRY_POINT_CTX.name);
       }
        return sites;
    }
    
    public static boolean isNowSameDayAsDate(Date d) {
        Calendar now = Calendar.getInstance();
        SiteService.clearTime(now);
        Calendar copy = Calendar.getInstance();
        copy.setTime(d);
        SiteService.clearTime(copy);
        return copy.getTimeInMillis() - now.getTimeInMillis() == 0;
    }
    
    private static void clearTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
