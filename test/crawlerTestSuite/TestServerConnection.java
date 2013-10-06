/**
 * 
 */
package crawlerTestSuite;

import java.util.Vector;

import junit.framework.TestCase;
import network.server.ServerConnection;
import collector.data.Filter;

/**
 * @author sucker
 * 
 */
public class TestServerConnection extends TestCase {

	public void testSession() {
		ServerConnection sc = ServerConnection.getConnection();
		System.out.println("TestServerConnection.testSession() sid="
				+ sc.getSessionId());
	}

	public void testDiscover() {
		Vector<String> services = ServerConnection.getConnection()
				.discoverService();
		System.out.println("TestServerConnection.testDiscover() \n" + services);
	}

	public void testServices() {
		Vector<String> services = ServerConnection.getConnection()
				.discoverService();
		System.out.println("TestServerConnection.testServices()");
		for (String service : services) {
			System.out.println("\nService ["
					+ service
					+ "]\n"
					+ ServerConnection.getConnection().getService(service)
							.toString());
		}

	}

	public void testBuild() throws Exception {
		Filter f = new Filter();
		f.setVal("make", "MercedesBenz");
		f.setVal("model", "");
		// f.setVal("allrad", "true");
		// f.setVal("klima", "true");

		Vector<String> services = ServerConnection.getConnection()
				.discoverService();

		for (String service : services) {
			String response = ServerConnection.getConnection().buildQuery(f,
					service);
			System.out.println("\nService [" + service + "]\n" + response);
			if (response == null || response.equals("")) {
				throw new Exception("Service [" + service
						+ "] returns null or empty string");
			}
		}

	}
}
