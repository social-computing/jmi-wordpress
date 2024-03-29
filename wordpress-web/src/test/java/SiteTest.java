import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.socialcomputing.wordpress.persistence.model.SiteInfo;

/**
 * 
 */

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 *
 */
public class SiteTest {

	public static void main(String[] args) {
		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(SiteInfo.class);
		config.configure("hibernate.cfg.xml");
		new SchemaExport(config).create(true, true);
	}
}
