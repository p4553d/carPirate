package core;

import gui.MainFormOutline;
import gui.message.MessageGUI;

import java.awt.Insets;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import message.MessageHandler;
import message.MessageType;
import network.LinkListener;
import network.server.ServerConnection;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import collector.CollectorExecutor;
import collector.data.Filter;
import collector.data.IFilter;
import collector.message.MessageCollector;
import collector.mind.CollectorMindAuto;
import collector.mind.ICollectorMind;
import core.config.Config;

/**
 * 
 */

/**
 * @author sucker
 * 
 */
public class crawler {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static void main(String[] args) {

		final MessageHandler mh = MessageHandler.getHandler();

		// Log service
		mh.addListener(new LogListener());
		mh.addListener(new LinkListener());
		mh.start();

		// Init configuration
		if (args.length > 0) {
			Config.getConfig(args[0]);
		} else {
			Config.getConfig();
		}

		// Build up the pool of collectors
		CollectorExecutor ce = new CollectorExecutor();
		ce.start();
		Vector<String> services = ServerConnection.getConnection()
				.discoverService();

		for (String service : services) {
			try {
				ICollectorMind tmpCM = new CollectorMindAuto(ServerConnection
						.getConnection().getService(service));
				ce.addCollector(tmpCM);
			} catch (JDOMException e) {
				e.printStackTrace();
				MessageHandler.getHandler().broadcast(
						"Configuration for service [" + service
								+ "] is erroneous", MessageType.ERROR);
			}
		}

		// Build GUI
		// Look and Feel
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		Insets insets = (Insets) UIManager.getInsets("TabbedPane.tabInsets")
				.clone();
		if (insets != null) {
			insets.top = 6;
			UIManager.put("TabbedPane.tabInsets", insets);
		}

		try {
			UIManager.setLookAndFeel(Config.getConfig().getLaf());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		// Start main frame
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFormOutline mf;
				mf = new MainFormOutline();
				mh.addListener(mf.getListener());
				// mf.setSize(520, 420);
				mf.setVisible(true);

				// read saved filters
				crawler.readFilters("file:/"
						+ Config.getConfig().getFilterSource());
			}
		});

	}

	@SuppressWarnings("unchecked")
	public static void readFilters(String sourceFile) {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document sourceXML = builder.build(sourceFile);

			XPath filters = XPath.newInstance("/depository/filter");
			List<Element> listFilter = filters.selectNodes(sourceXML);

			for (Element e : listFilter) {
				IFilter tmpf = new Filter();
				List<Element> children = e.getChildren("set");

				for (Element set : children) {
					tmpf.setVal(set.getAttributeValue("key"), set.getValue());
				}
				MessageHandler.getHandler().receiveMessage(
						new MessageCollector(tmpf, MessageType.ADD));
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Saved filters are damaged",
							MessageType.ERROR));
		} catch (IOException e) {
			// e.printStackTrace();
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Missing file with saved filters",
							MessageType.NOOP));
		}
	}
}
