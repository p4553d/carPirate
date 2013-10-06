/**
 * 
 */
package core;

import javax.swing.JOptionPane;

import message.Message;
import message.MessageListener;
import message.MessageType;

/**
 * @author sucker
 * 
 */
public class LogListener implements MessageListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#getListener()
	 */
	@Override
	public MessageListener getListener() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#reciveMessage(message.Message)
	 */
	@Override
	public void receiveMessage(Message m) {
		if (m.getType() != MessageType.HIT) {
			System.out.println(new java.util.Date().toString() + ": ["
					+ m.getType() + "] " + m.getContent());
		}

		if (m.getType() == MessageType.ERROR) {
			showError(m.getContent());
		}

	}

	public void showError(Object s) {
		JOptionPane.showMessageDialog(null, s, "Fehler",
				JOptionPane.ERROR_MESSAGE);
	}

}