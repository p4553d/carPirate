/**
 * 
 */
package collector.message;

import message.Message;
import message.MessageType;

/**
 * @author sucker
 * 
 */
public class MessageCollector extends Message {

	/**
	 * @param content
	 * @param type
	 */
	public MessageCollector(Object content, MessageType type) {
		super(content, type);
	}

}
