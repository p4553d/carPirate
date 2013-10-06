package collector.message;

import message.Message;
import message.MessageListener;
import message.MessageType;
import collector.data.Filter;

public abstract class MessageListenerCollector implements MessageListener {

	@Override
	public void receiveMessage(Message m) {
		if (m instanceof MessageCollector) {

			switch (m.getType()) {
			case ADD:
				if (m.getContent() instanceof Filter) {
					addQuery((Filter) m.getContent());
				}
				break;

			case REMOVE:
				if (m.getContent() instanceof Filter) {
					removeQuery((Filter) m.getContent());
				}
				break;

			case REFRESH:
				if (m.getContent() instanceof Filter) {
					refreshQuery((Filter) m.getContent());
				}
				break;

			case VISITURL:
				if (m.getContent() instanceof Filter) {
					visitURL((Filter) m.getContent());
				}
				break;

			default:
				// Do nothing
				break;
			}
		}

		if (m.getType() == MessageType.CLOSE) {
			stop();
		}
	}

	/**
	 * @param content
	 */
	public abstract void refreshQuery(Filter f);

	public abstract void addQuery(Filter f);

	public abstract void removeQuery(Filter f);

	public abstract void visitURL(Filter f);

	public abstract void stop();
}
