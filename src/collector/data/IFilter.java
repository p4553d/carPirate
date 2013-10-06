/**
 * 
 */
package collector.data;

import java.util.Set;

/**
 * @author sucker
 *
 */
public interface IFilter {

	public abstract void setVal(String key, String value);

	public abstract String getVal(String key);

	public abstract Set<String> getKeys();

}