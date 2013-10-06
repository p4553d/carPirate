/**
 * 
 */
package collector;

import java.util.Vector;

import message.MessageHandler;
import collector.mind.ICollectorMind;

/**
 * @author sucker
 * 
 */
public class CollectorExecutor {

	private final Vector<Collector> collectorPool;
	private boolean running;

	public CollectorExecutor() {
		this.collectorPool = new Vector<Collector>();
	}

	public void addCollector(ICollectorMind mind) {
		Collector tmp_c = new Collector(mind);
		collectorPool.add(tmp_c);
		MessageHandler.getHandler().addListener(tmp_c);

		if (running) {
			tmp_c.start();
		}
	}

	public void start() {
		if (!running) {
			running = true;
			for (Collector c : collectorPool) {
				c.start();
			}
		}
	}

	public void stop() {
		for (Collector c : collectorPool) {
			c.stop();
		}
	}
}
