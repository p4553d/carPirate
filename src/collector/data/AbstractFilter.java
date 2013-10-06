/**
 * 
 */
package collector.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Global representation for data in flow of query.
 * 
 * filter -> search filter -> search query -> html -> hit filter -> hit query ->
 * html -> hit
 * 
 * @author sucker
 * 
 */

public class AbstractFilter implements IFilter {

	protected Map<String, String> values;

	/**
	 * 
	 */
	public AbstractFilter() {
		super();
		this.values = new HashMap<String, String>();

	}

	/* (non-Javadoc)
	 * @see collector.data.IFilter#setVal(java.lang.String, java.lang.String)
	 */
	public void setVal(String key, String value) {
		if (value == null || value.equals("")) {
			values.remove(key);
		} else {
			values.put(key, value);
		}
	}

	/* (non-Javadoc)
	 * @see collector.data.IFilter#getVal(java.lang.String)
	 */
	public String getVal(String key) {
		return values.get(key);
	}

	/* (non-Javadoc)
	 * @see collector.data.IFilter#getKeys()
	 */
	public Set<String> getKeys() {
		return values.keySet();
	}

	@Override
	public String toString() {
		return values.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AbstractFilter)) {
			return false;
		}
		AbstractFilter f = (AbstractFilter) o;
		return (f.values.equals(this.values));
	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}

}