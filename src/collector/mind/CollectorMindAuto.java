/**
 * 
 */
package collector.mind;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import network.server.ServerConnection;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import collector.data.Filter;
import collector.data.Hit;
import collector.data.SearchFilter;

/**
 * @author sucker
 * 
 */
public class CollectorMindAuto implements ICollectorMind {

	protected final Document serviceXML;

	private final String serviceId;
	private final int period;
	private final Pattern hqRegex;
	private final String preUrl;
	private final String postUrl;
	private final int groupUrl;
	private final String preGet;
	private final String postGet;
	private final int groupGet;
	private final int groupHash;

	public CollectorMindAuto(Document serviceFile) throws JDOMException {

		this.serviceXML = serviceFile;

		String speriod = (String) XPath.selectSingleNode(serviceXML,
				"string(/service/period)");
		if (speriod != null) {
			this.period = Integer.valueOf(speriod);
		} else {
			this.period = 1500;
		}

		this.serviceId = (String) XPath.selectSingleNode(serviceXML,
				"string(/service/@name)");

		// getHitQuery
		XPath xRegex = XPath.newInstance("string(/service/parse/regex)");
		String regex = (String) xRegex.selectSingleNode(this.serviceXML);
		this.hqRegex = Pattern.compile(regex.trim());
		// URL
		XPath xPreURL = XPath.newInstance("string(/service/parse/url/prefix)");
		preUrl = ((String) xPreURL.selectSingleNode(serviceXML)).trim();

		XPath xPostURL = XPath
				.newInstance("string(/service/parse/url/postfix)");
		postUrl = ((String) xPostURL.selectSingleNode(serviceXML)).trim();

		XPath xGroupURL = XPath
				.newInstance("string(/service/parse/url/@group)");
		String sGroupUrl = (String) xGroupURL.selectSingleNode(serviceXML);
		groupUrl = Integer.parseInt(sGroupUrl);
		// GET
		XPath xGroupGet = XPath
				.newInstance("string(/service/parse/get/@group)");
		String sGroupGet = (String) xGroupGet.selectSingleNode(serviceXML);

		if (!sGroupGet.equals("")) {
			XPath xPreGet = XPath
					.newInstance("string(/service/parse/get/prefix)");
			preGet = ((String) xPreGet.selectSingleNode(serviceXML)).trim();

			XPath xPostGet = XPath
					.newInstance("string(/service/parse/get/postfix)");
			postGet = ((String) xPostGet.selectSingleNode(serviceXML)).trim();

			groupGet = Integer.parseInt(sGroupGet);
		} else {
			preGet = "";
			postGet = "";
			groupGet = -1;

		}
		// Hash
		XPath xGroupHash = XPath
				.newInstance("string(/service/parse/hash/@group)");
		String sGroupHash = (String) xGroupHash.selectSingleNode(serviceXML);

		if (!sGroupHash.equals("")) {
			groupHash = Integer.parseInt(sGroupHash);
		} else {
			groupHash = -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.factory.CollectorMind#getHitQuery(java.lang.String)
	 */
	@Override
	public SearchFilter getHitQuery(String s, Filter parent, int offset)
			throws JDOMException {

		Matcher matcher = hqRegex.matcher(s);
		SearchFilter f = null;
		int currMatching = 0;

		// skip offset
		while (currMatching < offset && matcher.find()) {
			currMatching++;
		}

		if (matcher.find()) {
			f = new SearchFilter(parent);
			// URL
			String resUrl = preUrl + matcher.group(groupUrl).trim() + postUrl;

			// GET
			if (groupGet != -1) {
				String resGet = preGet + matcher.group(groupGet).trim()
						+ postGet;
				f.setVal("get", resGet);
			}

			// Hash
			if (groupHash != -1) {
				String resHash = matcher.group(groupHash).trim();
				f.setVal("hash", resHash);
			}
			f.setVal("url", resUrl);
			return f;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.factory.CollectorMind#getHit(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Hit getHit(String s, Filter parent) throws JDOMException {
		XPath xRegex = XPath.newInstance("/service/extract/regex");
		List<Element> regex = xRegex.selectNodes(serviceXML);

		Hit h = new Hit(parent);
		for (Element e : regex) {

			Pattern p = Pattern.compile(e.getTextTrim(), Pattern.DOTALL);
			Matcher m = p.matcher(s);

			if (m.find()) {
				String key = e.getAttributeValue("name");
				int group = Integer.parseInt(e.getAttributeValue("group"));

				String prefix = e.getChildText("prefix").trim();
				String postfix = e.getChildText("postfix").trim();

				String value = prefix + m.group(group).trim() + postfix;

				h.setVal(key, value);
			}

		}
		return h;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.mind.ICollectorMind#getPerioold()
	 */
	@Override
	public int getPeriod() {
		return this.period;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.mind.ICollectorMind#getSearchQuery(collector.data.Filter)
	 */
	@Override
	public SearchFilter getSearchQuery(Filter f) throws Exception {

		SearchFilter sf = new SearchFilter(f);
		sf.setVal("type", "search");
		sf.setVal("url", ServerConnection.getConnection().buildQuery(f,
				serviceId));
		return sf;

	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.mind.ICollectorMind#getHitQuery(java.lang.String,
	 * collector.data.Filter, int)
	 */
	@Override
	public SearchFilter getHitQuery(String s, Filter parent) throws Exception {
		return getHitQuery(s, parent, 0);
	}
}
