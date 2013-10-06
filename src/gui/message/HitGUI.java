/**
 * 
 */
package gui.message;

import java.util.Set;

import collector.data.Hit;
import collector.data.IFilter;

/**
 * @author sucker
 * 
 */
public class HitGUI extends Hit {

	private final Hit parent;
	private boolean seen;
	private boolean fav;

	public HitGUI(Hit h) {
		this.parent = h;
		this.seen = false;
		this.fav = false;
	}

	public String getVal(String key) {
		return parent.getVal(key);
	}

	public IFilter getParent() {
		return parent.getParent();
	}

	/**
	 * @return the seen
	 */
	public boolean isSeen() {
		return seen;
	}

	public void wasSeen() {
		this.seen = true;
	}

	/**
	 * @return the fav
	 */
	public final boolean isFav() {
		return fav;
	}

	public void setFav(final boolean f) {
		this.fav = f;
	}

	@Override
	public String toString() {
		return "HitGUI: " + parent.getVal("title");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.data.IFilter#getKeys()
	 */
	@Override
	public Set<String> getKeys() {
		return parent.getKeys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see collector.data.IFilter#setVal(java.lang.String, java.lang.String)
	 */
	@Override
	public void setVal(String key, String value) {
		// nothing to do, write only wrapper
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof HitGUI)) {
			return false;
		}
		HitGUI f = (HitGUI) o;
		return (f.parent.equals(this.parent));
	}

}
