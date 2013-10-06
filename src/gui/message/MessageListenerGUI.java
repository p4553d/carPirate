package gui.message;

import message.Message;
import message.MessageListener;
import collector.data.Filter;
import collector.data.Hit;
import collector.data.IFilter;
import collector.message.MessageCollector;

public abstract class MessageListenerGUI implements MessageListener {

	@Override
	public void receiveMessage(Message m) {
		if (m instanceof MessageGUI) {
			switch (m.getType()) {
			case HIT:
				addHit((Hit) m.getContent());
				break;
			default:
			}

		}

		if (m instanceof MessageCollector) {
			if (m.getContent() instanceof Filter) {
				switch (m.getType()) {
				case ADD:
					addFilter((Filter) m.getContent());
					break;
				case REMOVE:
					removeFilter((IFilter) m.getContent());
					break;
				default:
				}
			}
		}

	}

	public abstract void addHit(Hit h);

	public abstract void addFilter(Filter f);

	public abstract void removeFilter(IFilter f);
}
