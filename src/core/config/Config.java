/**
 * 
 */
package core.config;

import gui.MainFormOutline;
import gui.message.MessageGUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import message.Message;
import message.MessageHandler;
import message.MessageType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * @author sucker
 * 
 */
public class Config {

	private volatile static Config instance = null;

	private String hitsTable = null;
	private String filterSaved = null;
	private String favSaved = null;
	private String dataDirectory = null;
	private String laf = null;
	private String server = null;
	private String instanceid = null;
	private String language = null;
	private boolean sound = false;
	private boolean trayMessage = false;
	private int version = 0;

	public static Config getConfig() {
		return getConfig("config.xml");
	}

	public static synchronized Config getConfig(String configFile) {
		if (instance == null) {
			instance = new Config(configFile);
		}
		return instance;
	}

	private Config(String configFile) {

		MessageHandler.getHandler()
				.receiveMessage(
						new Message("Read config from " + configFile,
								MessageType.NOOP));

		// read variables from configs
		try {
			initVars(configFile);
		} catch (JDOMException e) {
			e.printStackTrace();
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Configuration is erroneous",
							MessageType.ERROR));
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit due to bad configuration",
							MessageType.CLOSE));
		} catch (IOException e) {
			e.printStackTrace();

			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Can not read configuration",
							MessageType.ERROR));
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit due to bad configuration",
							MessageType.CLOSE));
		} catch (NullPointerException e) {
			e.printStackTrace();

			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Missing elements", MessageType.ERROR));
			MessageHandler.getHandler().receiveMessage(
					new Message("Exit due to bad configuration",
							MessageType.CLOSE));
		}

	}

	private void initVars(String configFile) throws JDOMException, IOException {

		SAXBuilder builder = new SAXBuilder(false);
		Document configXML = builder
				.build(Config.class.getResource(configFile));

		this.server = (String) XPath.selectSingleNode(configXML,
				"string(/config/server/@host)");

		this.instanceid = (String) XPath.selectSingleNode(configXML,
				"string(/config/@id)");

		this.version = Integer.parseInt((String) XPath.selectSingleNode(
				configXML, "string(/config/@version)"));
		// data order
		this.dataDirectory = System.getProperty("user.home") + "/carcw";
		(new File(this.dataDirectory)).mkdirs();

		// disposing of saved filters and favorites
		this.filterSaved = this.dataDirectory + "/filters.xml";
		this.favSaved = this.dataDirectory + "/fav.xml";
		this.hitsTable = this.dataDirectory + "/gui.out.xml";

		// gui.out
		Document guiDoc = null;
		try {
			guiDoc = builder.build("file:/" + hitsTable);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (guiDoc == null
					|| ((String) XPath.selectSingleNode(guiDoc,
							"string(/gui.out/@version)")).equals("")
					|| Integer.parseInt((String) XPath.selectSingleNode(guiDoc,
							"string(/gui.out/@version)")) < this.version) {
				guiDoc = builder.build(MainFormOutline.class
						.getResource("gui.out.xml"));
				XMLOutputter xmlOut = new XMLOutputter();
				xmlOut.output(guiDoc, new FileOutputStream(hitsTable));
			}
		}

		XPath lafPath = XPath.newInstance("/gui.out/lookandfeel");
		Element lafE = (Element) lafPath.selectSingleNode(guiDoc);
		this.laf = lafE.getValue();

		this.language = (String) XPath.selectSingleNode(guiDoc,
				"string(/gui.out/language)");

		this.sound = Boolean.parseBoolean((String) XPath.selectSingleNode(
				guiDoc, "string(/gui.out/sound)"));
		
		this.trayMessage = Boolean.parseBoolean((String) XPath.selectSingleNode(
				guiDoc, "string(/gui.out/trayMessage)"));
	}

	public String getHitsTableConf() {
		return hitsTable;
	}

	public String getFilterSource() {
		return this.filterSaved;
	}

	public String getFavSource() {
		return favSaved;
	}

	public String getLaf() {
		return laf;
	}

	public String getServer() {
		return this.server;
	}

	public String getInstanceID() {
		return this.instanceid;
	}

	public String getLanguage() {
		return this.language;
	}

	public boolean getSound() {
		return this.sound;
	}

	public boolean getTrayMessage() {
		return trayMessage;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

}
