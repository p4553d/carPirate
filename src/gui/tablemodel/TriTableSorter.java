/**
 * 
 */
package gui.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author sucker
 * @param <M>
 * 
 */
public class TriTableSorter<M extends TableModel> extends TableRowSorter<M> {

	public TriTableSorter(M m) {
		super(m);
	}

	public void toggleSortOrder(int column) {
		if (isSortable(column)) {
			List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
			SortKey sortKey;
			int sortIndex;
			for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
				if (keys.get(sortIndex).getColumn() == column) {
					break;
				}
			}
			if (sortIndex == -1) {
				// Key doesn't exist
				sortKey = new SortKey(column, SortOrder.ASCENDING);
				keys.add(0, sortKey);
			} else if (sortIndex == 0) {
				// It's the primary sorting key, toggle it
				keys.set(0, toggle(keys.get(0)));
			} else {
				// It's not the first, but was sorted on, remove old
				// entry, insert as first with ascending.
				keys.remove(sortIndex);
				keys.add(0, new SortKey(column, SortOrder.ASCENDING));
			}
			if (keys.size() > getMaxSortKeys()) {
				keys = keys.subList(0, getMaxSortKeys());
			}
			setSortKeys(keys);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultRowSorter#toggleSortOrder(int)
	 */
	private SortKey toggle(SortKey key) {
		switch (key.getSortOrder()) {
		case ASCENDING:
			return new SortKey(key.getColumn(), SortOrder.DESCENDING);
		case DESCENDING:
			return new SortKey(key.getColumn(), SortOrder.UNSORTED);
		default:
			return new SortKey(key.getColumn(), SortOrder.ASCENDING);
		}
	}
}
