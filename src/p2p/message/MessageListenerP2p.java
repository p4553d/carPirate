package p2p.message;

import message.Message;
import message.MessageListener;
import collector.data.Filter;
import collector.data.IFilter;

public abstract class MessageListenerP2p implements MessageListener {

	@Override
	public void receiveMessage(Message m) {
		if (m instanceof MessageP2p) {
			if (m.getContent() instanceof Filter) {
				addQuery((IFilter) m.getContent());
			}
		}
	}

	public abstract void addQuery(IFilter f);
}
