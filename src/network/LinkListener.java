/**
 * 
 */
package network;

import java.io.IOException;

import message.Message;
import message.MessageListener;
import message.MessageType;

/**
 * @author sucker
 * 
 */
public class LinkListener implements MessageListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageListener#getListener()
	 */
	@Override
	public MessageListener getListener() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageListener#reciveMessage(message.Message)
	 */
	@Override
	public void receiveMessage(Message m) {
		if (m.getType() == MessageType.VISITURL && m.getContent() != null
				&& m.getContent() instanceof String) {
			visitURL(m.getContent().toString());
		}

	}

	private void visitURL(String url) {
		try {
			// FIXME: not platform independent, path must be configurable
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
