/**
 * 
 */
package message;

/**
 * @author sucker
 * 
 */
public interface MessageListener {
	public void receiveMessage(Message m);

	@Deprecated
	/*
	 * Obsolete function
	 */
	public MessageListener getListener();
}
