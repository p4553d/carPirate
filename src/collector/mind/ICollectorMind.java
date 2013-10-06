/**
 * 
 */
package collector.mind;

import collector.Collector;
import collector.data.Filter;
import collector.data.Hit;
import collector.data.SearchFilter;

/**
 * Interface to collector mind. Describes basic function needed to process data
 * along data flow.
 * 
 * @see Filter
 * @see Collector
 * @author sucker
 */
public interface ICollectorMind {
	/**
	 * Generate from search description a filter to build valid query for
	 * collector.
	 * 
	 * @param f
	 *            Filter, containing some keyword for search
	 * @return result filter with valid values for SearchFilter
	 * @throws Exception
	 * 
	 * @see Filter
	 * @see SearchFilter
	 */
	public abstract SearchFilter getSearchQuery(Filter f) throws Exception;

	/**
	 * Parse out of retrieved data an link to detailed description of an object.
	 * 
	 * @param s
	 *            HTML input to parse
	 * @return result filter with valid values for HitFilter
	 * @throws Exception
	 * 
	 * @see HitFilter
	 */
	public abstract SearchFilter getHitQuery(String s, Filter parent)
			throws Exception;

	/**
	 * Parse out of retrieved data an link to detailed description of an object.
	 * Skip first <b>offset>/b> occurrences.
	 * 
	 * @param s
	 *            HTML input to parse
	 * @return result filter with valid values for HitFilter
	 * @throws Exception
	 * 
	 * @see HitFilter
	 */
	public abstract SearchFilter getHitQuery(String s, Filter parent, int offset)
			throws Exception;

	/**
	 * Extract description of an object from HTML data
	 * 
	 * @param s
	 *            HTML input to parse
	 * @return Filter with specific collection of keywords of description
	 * @throws Exception
	 * 
	 * @see Hit
	 */
	public abstract Hit getHit(String s, Filter parent) throws Exception;

	public abstract int getPeriod();

	public abstract String getServiceId();

}