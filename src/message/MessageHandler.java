/**
 * 
 */
package message;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sucker
 * 
 */
public class MessageHandler implements MessageSender, MessageListener {

	private volatile static MessageHandler instance = null;

	private final BlockingQueue<Message> queue;
	private final Vector<MessageListener> listener;

	private final MessageHandlerThread tHandler;
	private boolean isShutdown;
	private int messages;

	public static synchronized MessageHandler getHandler() {
		if (instance == null) {
			instance = new MessageHandler();
		}
		return instance;
	}

	private MessageHandler() {
		queue = new LinkedBlockingQueue<Message>();
		listener = new Vector<MessageListener>();
		isShutdown = false;
		messages = 0;
		tHandler = new MessageHandlerThread();

		this.receiveMessage(new Message("Message handler started",
				MessageType.NOOP));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageSender#addListener(message.Listener.MessageListener)
	 */
	@Override
	public synchronized void addListener(MessageListener listener) {
		this.listener.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#ReciveMessage(message.Message)
	 */
	@Override
	public void receiveMessage(Message m) {
		synchronized (this) {
			if (isShutdown) {
				throw new IllegalStateException("Process MessageHandler ("
						+ this + ") is shutting down.");
			}
			messages++;
		}
		this.queue.add(m);

		// Shutdown if message is of type CLOSE
		if (m.getType() == MessageType.CLOSE) {
			stop();
		}
	}

	public void broadcast(String msg, MessageType type) {
		receiveMessage(new Message(msg, type));
	}

	public void start() {
		tHandler.start();
	}

	public void stop() {
		synchronized (this) {
			isShutdown = true;
		}
		tHandler.interrupt();
	}

	private class MessageHandlerThread extends Thread {

		public MessageHandlerThread() {
			super("MessageHandlerThread");
		}

		public MessageHandlerThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			while (true) {
				try {
					synchronized (MessageHandler.this) {
						if (isShutdown && messages == 0) {
							break;
						}
					}

					Message m = queue.take();
					for (MessageListener rx : listener) {
						try {
							rx.receiveMessage(m);
						} catch (Exception e) {
							// don't interrupt message handler with bad calls
							e.printStackTrace();
						}
					}
					synchronized (MessageHandler.this) {
						messages--;
					}

				} catch (InterruptedException e) {
					// do nothing - retry
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#getListener()
	 */
	@Override
	public MessageListener getListener() {
		return this;
	}
}
