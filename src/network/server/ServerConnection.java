/**
 * 
 */
package network.server;

import gui.message.MessageGUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Vector;

import message.Message;
import message.MessageHandler;
import message.MessageType;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import collector.data.Filter;
import core.config.Config;

/**
 * @author sucker
 * 
 */
public class ServerConnection {

	private final String server;
	private String sessionid = null;
	private Vector<String> services = null;

	private static volatile ServerConnection instance = null;

	public static synchronized ServerConnection getConnection() {
		if (instance == null) {
			instance = new ServerConnection();
		}
		return instance;
	}

	private ServerConnection() {
		// set server
		this.server = Config.getConfig().getServer();

		// get id
		getSessionId();

	}

	public String getSessionId() {
		synchronized (this) {
			if (sessionid == null) {
				String response = serverRequest("op=reg&iid="
						+ Config.getConfig().getInstanceID());

				MessageHandler.getHandler().receiveMessage(
						new Message("Session response " + response,
								MessageType.NOOP));
				String code = response.substring(0, 2);
				String message = response.substring(3);

				if (code.equals("ER")) {
					MessageHandler.getHandler().receiveMessage(
							new MessageGUI("Server error\n" + message,
									MessageType.ERROR));
					MessageHandler.getHandler().receiveMessage(
							new Message("Exit due to server error",
									MessageType.CLOSE));
				} else {
					// regular session assumed
					sessionid = message;
				}
			}
		}
		if (sessionid == null || sessionid.equals("")) {
			MessageHandler.getHandler()
					.receiveMessage(
							new MessageGUI("Server refuses session",
									MessageType.ERROR));
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit due to no session is available",
							MessageType.CLOSE));
		}
		return sessionid;
	}

	public Vector<String> discoverService() {
		if (services == null) {
			services = new Vector<String>();
			String reply = serverRequest("op=discover&sid=" + getSessionId());
			if (!reply.equals("")) {
				String[] a_services = reply.split(":");
				for (int i = 0; i < a_services.length; i++) {
					services.add(a_services[i].trim());
				}
			} else {
				this.sessionid = null;
			}

		}
		return services;

	}

	public Document getService(String serviceid) {
		String reply = serverRequest("op=service&sid=" + getSessionId()
				+ "&srid=" + serviceid);
		Document resDoc = null;
		if (!reply.equals("")) {
			try {
				resDoc = (new SAXBuilder()).build(new StringReader(reply));
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.sessionid = null;
		}
		return resDoc;
	}

	public String buildQuery(Filter f, String serviceid) {
		try {
			String reply = serverRequest("op=build&sid=" + getSessionId()
					+ "&srid=" + serviceid + "&filter="
					+ URLEncoder.encode(f.toString(), "utf8"));
			if (reply.equals("")) {
				this.sessionid = null;
			}
			return reply.trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast(
					"Server returned unsupported encoding", MessageType.ERROR);
		}
		return null;

	}

	private String serverRequest(String request) {
		StringBuffer inputBuffer = new StringBuffer();
		URLConnection connection = null;
		Writer out = null;
		try {
			URL requestURL = new URL(this.server);
			connection = requestURL.openConnection();
			connection.setDoOutput(true);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(request);
			out.flush();
			out.close();

			InputStream bInput = connection.getInputStream();

			// Reply
			int c;
			while ((c = bInput.read()) != -1) {
				inputBuffer.append((char) c);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast(
					"Can not connect to server\nUnknown Host",
					MessageType.ERROR);
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit server refuses connection",
							MessageType.CLOSE));
		} catch (IOException e) {
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Server refuse connection",
							MessageType.ERROR));
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit server refuse connection",
							MessageType.CLOSE));
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return inputBuffer.toString();
	}
}
