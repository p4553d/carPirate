/**
 * 
 */
package message;

/**
 * @author sucker
 * 
 */
public class Message {

	private final Object content;
	private final MessageType type;

	/**
	 * Construct message
	 * 
	 * @param content
	 * @param type
	 */
	public Message(Object content, MessageType type) {
		this.content = content;
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public MessageType getType() {
		return type;
	}

}