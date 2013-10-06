/**
 * 
 */
package gui.tablemodel;

import gui.message.MessageGUI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import message.MessageHandler;
import message.MessageType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import collector.data.Filter;
import collector.message.MessageCollector;
import core.config.Config;

/**
 * @author sucker
 * 
 */
public class FilterTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6789591739579079950L;

	private Vector<Filter> filters;
	private String[] tableHead;
	private String[] tableCaption;

	@SuppressWarnings("unchecked")
	public FilterTableModel(URL url) {
		SAXBuilder builder = new SAXBuilder(false);
		Document combineXML;
		try {
			combineXML = builder.build(url);

			XPath keywords = XPath.newInstance("/gui.in/table/field");

			List<Element> keywordsList = keywords.selectNodes(combineXML);
			this.tableHead = new String[keywordsList.size()];
			this.tableCaption = new String[keywordsList.size()];

			Locale currLocale = new Locale(Config.getConfig().getLanguage());
			ResourceBundle captions = ResourceBundle.getBundle(
					"gui/bundle/gui", currLocale);

			for (int i = 0; i < keywordsList.size(); i++) {
				this.tableHead[i] = keywordsList.get(i).getAttributeValue(
						"name");
				this.tableCaption[i] = captions.getString("table."
						+ tableHead[i]);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Configuration is erroneous",
							MessageType.ERROR));
		} catch (IOException e) {
			e.printStackTrace();
			MessageHandler.getHandler().receiveMessage(
					new MessageGUI("Configuration is not available",
							MessageType.ERROR));
		}
		this.filters = new Vector<Filter>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.tableHead.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.tableCaption[columnIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.filters.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return (this.filters.get(rowIndex)).getVal(tableHead[columnIndex]);
	}

	public Filter getFilter(int rowIndex) {
		return this.filters.get(rowIndex);
	}

	public void addRow(Filter f) {
		this.filters.add(f);
		fireTableRowsInserted(filters.size() - 1, filters.size() - 1);
	}

	public void removeRow(int rowIndex) {
		MessageHandler.getHandler().receiveMessage(
				new MessageCollector(this.filters.get(rowIndex),
						MessageType.REMOVE));
		this.filters.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public void saveFilters() {
		Element depository = new Element("depository");
		for (Filter filter : this.filters) {
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
					.getFilterSource()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast("Can not save filters",
					MessageType.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast("Can not save filters",
					MessageType.ERROR);
		}
	}
}