package p2p;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Tx extends Thread {

	private final Vector<String> buffer; // Buffer for Messages
	private final Vector<String> recepients;

	public Tx() {
		buffer = new Vector<String>();
		recepients = new Vector<String>();
	}

	public void newRecepient(String addr) {
		synchronized (recepients) {
			recepients.add(addr);
		}
		System.out.println("Tx.newRecepient() count " + recepients.size());
	}

	public void remRecepient(String iaddr) {
		synchronized (recepients) {
			recepients.remove(iaddr);
		}
	}

	public void remAllRecepient() {
		synchronized (recepients) {
			recepients.removeAllElements();
		}
	}

	public void putMessage(String message) {
		synchronized (buffer) {
			buffer.add(message);
			buffer.notifyAll();
		}
	}

	public void run() {
		while (true) {
			String tmpMessage;

			// wait after messages
			synchronized (buffer) {
				while (buffer.isEmpty()) {
					try {
						buffer.wait();
					} catch (InterruptedException e) {
						// Do nothing
					}
				}

				tmpMessage = buffer.remove(0);
			}

			System.out.println("Tx.run() sende " + tmpMessage);
			synchronized (recepients) {
				for (String sAddr : recepients) {
					// TODO: optimization to runtime
					// Check against malformed addresses
					String[] aAddr = sAddr.split(":");
					String host = aAddr[0];
					int port = Integer.parseInt(aAddr[1]);
					try {
						Socket connection = new Socket(host, port);

						Writer out = new OutputStreamWriter(connection
								.getOutputStream());

						out.write(tmpMessage);
						out.flush();

						connection.close();

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}
}
