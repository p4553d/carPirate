/**
 * 
 */
package collector;

/**
 * @author sucker
 * 
 *         Class to save last <b>size</b> values
 * 
 *         Based on an array with cyclic pointer to current cell of array.
 *         Oldest elements on the queue are dropped without warnings and
 *         exceptions.
 */
public class ArrayQueue<E> {

	private final E[] collection;
	private final int size;
	private int current;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            of array
	 */
	@SuppressWarnings("unchecked")
	public ArrayQueue(int size) {
		this.size = size;
		this.collection = (E[]) new Object[size];
		this.current = 0;
	}

	/**
	 * Put new element <b>e</b> on queue
	 * 
	 * @param e
	 */
	public void add(E e) {
		collection[current] = e;
		current = (current + 1) % size;
	}

	/**
	 * Determine if element e is in the queue
	 * 
	 * @param e
	 * @return <b>true</b> if is on the queue, else <b>false</b>
	 */
	public boolean contains(E e) {
		for (int i = 0; i < collection.length; i++) {
			E tmpE = collection[(current - i + size) % size];
			if (tmpE != null && tmpE.equals(e)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add if not already exist
	 * 
	 * @param e
	 * @return <b>true</b> if value is already proceeded, <br/>
	 *         else <b>false</b> and add value to collection
	 */
	public boolean isProcessed(E e) {
		if (!contains(e)) {
			add(e);
			return false;
		}
		return true;
	}

	/**
	 * Gives size of queue back
	 * 
	 * @return
	 */
	public int size() {
		return size;
	}

	public void clean() {
		for (int i = 0; i < collection.length; i++) {
			collection[i] = null;
		}
	}
}
