package collector;

import java.net.URL;

import collector.data.Filter;

/**
 * Private class to organize information of queries.
 * 
 * @author sucker
 */
class CollectorQuery {
	private final Filter parent;
	private final URL u;
	private final String post;
	private final boolean permanent;
	private final ArrayQueue<Integer> processedHits;
	private int offset;

	public CollectorQuery(URL u, String post, boolean permanent, Filter parent) {
		this.u = u;
		this.post = post;
		this.permanent = permanent;
		this.processedHits = new ArrayQueue<Integer>(50);
		this.parent = parent;
		this.offset = 0;
	}

	public CollectorQuery(URL u, String post, boolean permanent) {
		this.u = u;
		this.post = post;
		this.permanent = permanent;
		this.processedHits = new ArrayQueue<Integer>(50);
		this.parent = null;
		this.offset = 0;
	}

	/**
	 * @return the u
	 */
	public URL getURL() {
		return u;
	}

	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}

	/**
	 * @return the permanent
	 */
	public boolean isPermanent() {
		return permanent;
	}

	public int getOffset() {
		return offset;
	}

	public boolean processed(int hash) {
		boolean res = this.processedHits.isProcessed(hash);
		if (res) {
			offset = 0;
		} else {
			offset = offset + 1;
		}
		return res;
	}

	public Filter getParent() {
		return this.parent;
	}

	public void refresh() {
		this.processedHits.clean();
		offset = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CollectorQuery) {
			if (this.parent == ((CollectorQuery) o).parent) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.parent.hashCode();
	}
}