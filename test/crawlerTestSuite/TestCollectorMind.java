/**
 * 
 */
package crawlerTestSuite;

import java.util.Vector;

import junit.framework.TestCase;
import message.Message;
import message.MessageHandler;
import message.MessageListener;
import message.MessageType;
import network.server.ServerConnection;
import collector.Collector;
import collector.data.Filter;
import collector.data.Hit;
import collector.mind.CollectorMindAuto;
import collector.mind.ICollectorMind;

/**
 * @author sucker
 * 
 */
public class TestCollectorMind extends TestCase {

	private static boolean mhRunning = false;

	public void testMake() throws Exception {
		Filter f = new Filter();
		f.setVal("make", "Volkswagen");
		f.setVal("model", "");

		Vector<String> services = ServerConnection.getConnection()
				.discoverService();

		MessageHandler mh = MessageHandler.getHandler();
		tearUp();
		System.out.println("TestCollectorMind.testMake() start MessageHandler");
		System.out.println("TestCollectorMind.testMake() search for " + f);

		for (String service : services) {

			// Init message handler
			HoldListener spl = new HoldListener();
			mh.addListener(spl);

			// Create collector
			ICollectorMind tmpCM = new CollectorMindAuto(ServerConnection
					.getConnection().getService(service));
			Collector coll = new Collector(tmpCM);
			mh.addListener(coll);
			coll.start();

			coll.addQuery(f);

			System.out.print("TestCollectorMind.testMake() waiting...");
			Thread.sleep(5000);
			System.out.println("done");
			coll.stop();

			Hit h = spl.getHit();
			if (h != null) {
				System.out
						.println("[" + service + "] Hit=" + h.getVal("title"));
			} else {
				throw new Exception("Service [" + service + "] returns null");
			}
		}
	}

	public void testMakeModel() throws Exception {
		Filter f = new Filter();
		f.setVal("make", "Volkswagen");
		f.setVal("model", "Golf");

		Vector<String> services = ServerConnection.getConnection()
				.discoverService();

		MessageHandler mh = MessageHandler.getHandler();
		tearUp();
		System.out
				.println("TestCollectorMind.testMakeModel() start MessageHandler");
		System.out.println("TestCollectorMind.testMakeModel() search for " + f);

		for (String service : services) {

			// Init message handler
			HoldListener spl = new HoldListener();
			mh.addListener(spl);

			// Create collector
			ICollectorMind tmpCM = new CollectorMindAuto(ServerConnection
					.getConnection().getService(service));
			Collector coll = new Collector(tmpCM);
			mh.addListener(coll);
			coll.start();

			coll.addQuery(f);

			System.out.print("TestCollectorMind.testMakeModel() waiting...");
			Thread.sleep(5000);
			System.out.println("done");
			coll.stop();

			Hit h = spl.getHit();
			if (h != null) {
				System.out
						.println("[" + service + "] Hit=" + h.getVal("title"));
			} else {
				throw new Exception("Service [" + service + "] returns null");
			}
		}
	}

	public void testPower() throws Exception {
		Filter f = new Filter();
		f.setVal("make", "Volkswagen");
		f.setVal("model", "Golf");
		f.setVal("powerTo", "66kw");
		f.setVal("powerForm", "55kw");

		Vector<String> services = ServerConnection.getConnection()
				.discoverService();

		MessageHandler mh = MessageHandler.getHandler();
		tearUp();
		System.out
				.println("TestCollectorMind.testPower() start MessageHandler");
		System.out.println("TestCollectorMind.testPower() search for " + f);

		for (String service : services) {

			// Init message handler
			HoldListener spl = new HoldListener();
			mh.addListener(spl);

			// Create collector
			ICollectorMind tmpCM = new CollectorMindAuto(ServerConnection
					.getConnection().getService(service));
			Collector coll = new Collector(tmpCM);
			mh.addListener(coll);
			coll.start();

			coll.addQuery(f);

			System.out.print("TestCollectorMind.testPower() waiting...");
			Thread.sleep(5000);
			System.out.println("done");
			coll.stop();

			Hit h = spl.getHit();
			if (h != null) {
				System.out.println("[" + service + "] Hit=" + h.getVal("title")
						+ " power=" + h.getVal("power"));
			} else {
				System.out.println("[" + service + "] Hit=null");
				throw new Exception("Service [" + service + "] returns null");
			}
		}
	}

	private void tearUp() {
		MessageHandler mh = MessageHandler.getHandler();
		if (!mhRunning) {
			mh.start();
			mhRunning = true;
		}
	}

	private class HoldListener implements MessageListener {

		private Hit h;

		public HoldListener() {
			h = null;
		}

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
		 * @see message.MessageListener#receiveMessage(message.Message)
		 */
		@Override
		public void receiveMessage(Message m) {
			if (m.getType() == MessageType.HIT) {
				h = (Hit) m.getContent();
				// MessageHandler.getHandler().broadcast("TestListenerDown",
				// MessageType.CLOSE);
			}
		}

		public Hit getHit() {
			return h;
		}
	}
}
