/**
 * 
 */
package gui.tablemodel;

import gui.message.HitGUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import message.MessageHandler;
import message.MessageType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import collector.data.Hit;
import core.config.Config;

/**
 * @author sucker
 * 
 */
public class FavoritTableModel extends AbstractHitsModel {

	/**
	 * Auto-generated serial uid
	 */
	private static final long serialVersionUID = 7502268968823883000L;

	/**
	 * @param extractFile
	 * @throws JDOMException
	 * @throws IOException
	 */
	public FavoritTableModel(String extractFile) {
		super(extractFile);
	}

	public void saveFavorits() {
		Element depository = new Element("depository");
		depository.setAttribute("version", Integer.toString(Config.getConfig()
				.getVersion()));
		for (HitGUI filter : this.rowData) {
			Element xmlFilter = new Element("filter");
			for (String key : filter.getKeys()) {
				Element xmlSet = new Element("set");
				xmlSet.setAttribute("key", key);
				xmlSet.setText(filter.getVal(key));

				xmlFilter.addContent(xmlSet);
			}

			depository.addContent(xmlFilter);
		}

		Document doc = new Document(depository);
		XMLOutputter out = new XMLOutputter();
		try {
			// out.setFormat(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(Config.getConfig()
					.getFavSource()));
		} catch (FileNotFoundException e) {
			MessageHandler.getHandler().broadcast("Can not save favorits",
					MessageType.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			MessageHandler.getHandler().broadcast("Can not save favorits",
					MessageType.ERROR);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void readFavorits() {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document sourceXML = builder.build("file:/"
					+ Config.getConfig().getFavSource());

			XPath favorits = XPath.newInstance("/depository/filter");
			List<Element> listFav = favorits.selectNodes(sourceXML);

			for (Element e : listFav) {
				Hit tmpf = new Hit(null);
				// tmpf.setVal("type", "filter");
				List<Element> children = e.getChildren("set");

				for (Element set : children) {
					tmpf.setVal(set.getAttributeValue("key"), set.getValue());
				}

				addRow(new HitGUI(tmpf));
			}
		} catch (JDOMException e) {
			MessageHandler.getHandler().broadcast("Can not read favorits",
					MessageType.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			MessageHandler.getHandler().broadcast("Can not read favorits",
					MessageType.NOOP);
			// e.printStackTrace();
		}
	}
}
