package com.socialcomputing.wordpress.utils.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

/**
 * @author "Jonathan Dray <jonathan@social-computing.com>"
 * @see : http://blog.bdoughan.com/2011/05/jaxb-and-joda-time-dates-and-times.html
 */
public class DateTimeAdapter
		extends XmlAdapter<String, DateTime> {
	@Override
	public DateTime unmarshal(String v) throws Exception {
		return new DateTime(v);
	}

	@Override
	public String marshal(DateTime v) throws Exception {
		return v.toString();
	}
}