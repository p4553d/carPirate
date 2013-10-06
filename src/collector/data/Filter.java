/**
 * 
 */
package collector.data;

/**
 * Global representation for data in flow of query.
 * 
 * filter -> search filter -> search query -> html -> hit filter -> hit query ->
 * html -> hit
 * 
 * @author sucker
 * 
 */

public class Filter extends AbstractFilter {
	private static int serialseed = 0;

	private int serialid;
	private boolean poisoned;

	public Filter() {
		this.serialid = serialseed++;
		this.poisoned = false;
	}

	public int getId() {
		return this.serialid;
	}

	/**
	 * @param poisoned
	 *            the poisoned to set
	 */
	public synchronized void setPoisoned() {
		this.poisoned = true;
	}

	/**
	 * @return the poisoned
	 */
	public synchronized boolean isPoisoned() {
		return poisoned;
	}
}
