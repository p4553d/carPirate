/**
 * 
 */
package gui.message;

import message.MessageListener;
import collector.data.Filter;
import collector.data.Hit;
import collector.data.IFilter;

/**
 * @author sucker
 * 
 */
public class MainFrameListener extends MessageListenerGUI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.message.MessageListenerGUI#addFilter(collector.data.Filter)
	 */
	@Override
	public void addFilter(Filter f) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.message.MessageListenerGUI#addHit(collector.data.Hit)
	 */
	@Override
	public void addHit(Hit h) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gui.message.MessageListenerGUI#removeFilter(collector.data.AbstractFilter
	 * )
	 */
	@Override
	public void removeFilter(IFilter f) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageListener#getListener()
	 */
	@Override
	public MessageListener getListener() {
		return this;
	}

}
