/**
 * 
 */
package p2p.message;

import message.Message;
import message.MessageType;

/**
 * @author sucker
 * 
 */
public class MessageP2p extends Message {

	/**
	 * @param content
	 * @param type
	 */
	public MessageP2p(Object content, MessageType type) {
		super(content, type);
	}

}
