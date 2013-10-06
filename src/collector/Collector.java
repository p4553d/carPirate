package collector;

import gui.message.MessageGUI;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import message.MessageHandler;
import message.MessageListener;
import message.MessageType;

import org.jdom.JDOMException;

import collector.data.Filter;
import collector.data.Hit;
import collector.data.SearchFilter;
import collector.message.MessageListenerCollector;
import collector.mind.ICollectorMind;

/**
 * Class to collect information from sources. Building of queries and evaluation
 * of data is given trough collector mind. Implements Runnable.
 * 
 * @see Filter
 * @see ICollectorMind
 * 
 * @author sucker
 * 
 */

public class Collector extends MessageListenerCollector {

	private final ICollectorMind mind; // evaluate data
	private final BlockingQueue<CollectorQuery> queries; // list of queries
	private final CollectorThread cThread;

	private boolean collecting; // state keeping variable

	/**
	 * Default private constructor. Can be invoked only through factory.
	 * 
	 * @param mind
	 *            Instance of CollectorMind to build and evaluate queries.
	 * @param mHandler
	 *            Core message handler to integrate all units
	 * 
	 */
	public Collector(ICollectorMind mind) {
		this.mind = mind;
		this.queries = new LinkedBlockingQueue<CollectorQuery>();
		this.cThread = new CollectorThread("CollectorThread "
				+ mind.getServiceId());
	}

	public synchronized void start() {
		if (!collecting) {
			collecting = true;
			this.cThread.start();
		}
	}

	@Override
	public void stop() {
		synchronized (this) {
			cThread.interrupt();
			collecting = false;
		}
	}

	/**
	 * Internal function to add queries. Can mark some queries as non-permanent.
	 * 
	 * @param f
	 *            Filter containing data to build query. Must have fields "url",
	 *            "get" and "post" be set.
	 * @param permanent
	 *            Flag for permanent search query
	 * 
	 * @throws MalformedURLException
	 * @throws JDOMException
	 */
	private void addQuery(SearchFilter f, boolean permanent) {
		synchronized (this) {
			if (!collecting) {
				throw new IllegalStateException("Collector is not collecting!");
			}
		}

		if (f != null && f.getVal("url") != null) {
			String sURL = f.getVal("url");
			if (f.getVal("get") != null) {
				sURL = sURL + "?" + f.getVal("get");
			}
			try {
				URL u = new URL(sURL);
				String post = f.getVal("post");
				CollectorQuery q;

				q = new CollectorQuery(u, post, permanent, f.getParent());

				this.queries.add(q);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Add query builded from data of filter.
	 * 
	 * @param f
	 *            Filter containing data to build query. Must have fields "url",
	 *            "get" and "post" be set.
	 * 
	 * @throws MalformedURLException
	 * @throws JDOMException
	 */
	@Override
	public void addQuery(Filter f) {
		SearchFilter res;
		try {
			res = mind.getSearchQuery(f);

			if (res != null) {
				addQuery(res, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeQuery(Filter f) {
		f.setPoisoned();
	}

	/**
	 * Thread control. Gives state of thread back.
	 * 
	 * @return state of collector.
	 */
	public synchronized boolean isRunning() {
		return collecting;
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

	private class CollectorThread extends Thread {

		public CollectorThread() {
			super("CollectorThread");
		}

		public CollectorThread(String name) {
			super(name);
		}

		/**
		 * Main function of collector. Implements run() from Thread.
		 */
		@Override
		public void run() {
			while (true) {
				try {
					synchronized (Collector.this) {
						if (!collecting) {
							break;
						}
					}

					CollectorQuery cq = null;
					while (cq == null || cq.getParent().isPoisoned()) {
						cq = queries.take();
					}

					// get data to current query
					URL queryUrl = cq.getURL();
					String postData = cq.getPost();
					boolean permanent = cq.isPermanent();
					StringBuffer inputBuffer = new StringBuffer();
					OutputStreamWriter out = null;
					InputStreamReader in = null;

					try {
						URLConnection connect = queryUrl.openConnection();

						if (postData != null && !postData.equals("")) {
							connect.setDoOutput(true);
							OutputStream raw = connect.getOutputStream();
							OutputStream buffered = new BufferedOutputStream(
									raw);
							out = new OutputStreamWriter(buffered, "8859_1");
							out.write(postData);
							out.flush();
							out.close();
						}

						connect.connect();

						InputStream raw = connect.getInputStream();
						in = new InputStreamReader(raw, "UTF8");

						int c;
						while ((c = in.read()) != -1) {
							inputBuffer.append((char) c);
						}
						in.close();
					} catch (IOException e) {
						// TODO only interesting thing get through
						//e.printStackTrace();
					} finally {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					}
					// process retrieved data

					// if query was not permanent delete it
					if (permanent) {
						// permanent - search query
						// parse data to hit query
						SearchFilter hf = mind.getHitQuery(inputBuffer
								.toString(), cq.getParent());

						// if not seen - queue up to look after
						while (hf != null && !cq.processed(hf.hashCode())) {
							addQuery(hf, false);
							hf = mind.getHitQuery(inputBuffer.toString(), cq
									.getParent(), cq.getOffset());
						}

						// re-queue query if not poisoned
						if (!cq.getParent().isPoisoned()) {
							queries.add(cq);
						}
					} else {
						// hit search performed
						// process data to hit and remove from query queue

						Hit h = mind.getHit(inputBuffer.toString(), cq
								.getParent());
						if (h.getVal("url") == null) {
							h.setVal("url", cq.getURL().toExternalForm());
						}
						h.setVal("source", mind.getServiceId());
						// further processing data. Generate message/event
						MessageHandler.getHandler().receiveMessage(
								new MessageGUI(h, MessageType.HIT));

					}

					Thread.sleep(mind.getPeriod());

				} catch (InterruptedException ie) {
					(Collector.this).stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * collector.message.MessageListenerCollector#refresh(collector.data.Filter)
	 */
	@Override
	public final synchronized void refreshQuery(final Filter f) {
		for (CollectorQuery cq : queries) {
			if (cq.getParent().equals(f)) {
				cq.refresh();
				return;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * collector.message.MessageListenerCollector#visitURL(collector.data.Filter
	 * )
	 */
	@Override
	public void visitURL(Filter f) {
		SearchFilter res;
		try {
			res = mind.getSearchQuery(f);

			if (res != null) {
				MessageHandler.getHandler().broadcast(res.getVal("url"),
						MessageType.VISITURL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
