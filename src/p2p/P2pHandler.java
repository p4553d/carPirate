package p2p;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;
import message.MessageListener;

public class P2pHandler implements Runnable {

	private ServerSocket listenSocket;
	private Tx transmitter;

	public P2pHandler(int port) throws IOException {

		listenSocket = null;
		try {
			listenSocket = new ServerSocket(port);

			System.out.println("Handler.Handler(): Listen on Port "
					+ listenSocket.getLocalPort());
		} catch (BindException ex) {
			System.err.println("Already bound on port " + port + "\n Exit!");
		}
		transmitter = new Tx();

	}

	public void run() {
		System.out.println("Handler.run() started!");

		// Start transmitter to send data out
		transmitter.start();

		// Listen on incoming connections
		while (listenSocket != null) {
			try {
				Socket connection = listenSocket.accept();

				Rx rxThread = new Rx(connection);
				rxThread.start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	public void sendMessage(Message message) {
		// if (message.getType() == MessageType.P2P_SEND)
		// transmitter.putMessage(message.getContent().toString());
	}

	public void newRecepient(Message message) {
		// if (message.getType() == MessageType.P2P_RECEPIENT)
		// transmitter.newRecepient(message.getContent().toString());
	}

	public void closeConnection(Message m) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#getListener()
	 */
	public MessageListener getListener() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.Listener.MessageListener#reciveMessage(message.Message)
	 */
	public void reciveMessage(Message m) {
		// do nothing
	}
}
