/**
 * 
 */
package collector.data;

/**
 * Data for building a query. Values for keys "url", "get" and "post" are
 * mandatory.
 * 
 * @see Filter
 * 
 * @author sucker
 */

public class SearchFilter extends AbstractFilter {
	private final Filter parent;

	public SearchFilter(Filter f) {
		this.parent = f;
	}

	/**
	 * @return the parent
	 */
	public Filter getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		String hash = this.getVal("hash");

		if (hash == null || hash.equals("")) {
			if (this.getVal("get") != null) {
				hash = this.getVal("url") + this.getVal("get");
			} else {
				hash = this.getVal("url");
			}
		}
		return hash.hashCode();
	}
}
