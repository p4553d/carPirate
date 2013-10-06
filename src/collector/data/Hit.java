/**
 * 
 */
package collector.data;

/**
 * Elaborate description of search object.
 * 
 * @see Filter
 * 
 * @author sucker
 * 
 */
public class Hit extends AbstractFilter {

	private final Filter parent;

	protected Hit() {
		super();
		this.parent = null;
	}

	public Hit(Filter parent) {
		super();
		this.parent = parent;
	}

	public IFilter getParent() {
		return parent;
	}

	@Override
	public String getVal(String key) {
		String res = super.getVal(key);

		if (res == null && parent != null) {
			res = parent.getVal(key);
		}

		return res;
	}
}
